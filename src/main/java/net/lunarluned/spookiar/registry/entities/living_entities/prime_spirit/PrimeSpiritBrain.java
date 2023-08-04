package net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit.ai.*;
import net.lunarluned.spookiar.registry.entities.registry.ModMemoryModules;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Optional;

public class PrimeSpiritBrain {

	private static final UniformInt TIME_BETWEEN_STOMPING = UniformInt.of(400, 500);
	private static final UniformInt TIME_BETWEEN_SUMMONING = UniformInt.of(400, 500);
	private static final UniformInt TIME_BETWEEN_SUMMONING_GHOST = UniformInt.of(1000, 1200);
	private static final UniformInt TIME_BETWEEN_JUDGEMENT = UniformInt.of(100, 200);
	private static final UniformInt DELAY_DURING_JUDGEMENT = UniformInt.of(150, 250);
	private static final UniformInt DELAY_DURING_WEAKEN = UniformInt.of(600, 750);
	private static final UniformInt DELAY_BETWEEN_ABSORB = UniformInt.of(2000, 2100);



	@SuppressWarnings("all")
	public static Brain<?> makeBrain(PrimeSpirit primeSpirit, Brain<PrimeSpirit> brain) {
		addCoreActivities(brain);
		addIdleActivities(brain);
		addFightActivity(primeSpirit, brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}

	private static void addCoreActivities(Brain<PrimeSpirit> brain) {
		brain.addActivity(Activity.CORE, 0, ImmutableList.of(
				new LookAtTargetSink(10, 30),
				new MoveToTargetSink(),
				new CountDownCooldownTicks(ModMemoryModules.GHOST_SPAWNING_WISP_COOLDOWN),
				new CountDownCooldownTicks(ModMemoryModules.GHOST_SPAWNING_GHOST_COOLDOWN),
				new CountDownCooldownTicks(ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_COOLDOWN),
				new CountDownCooldownTicks(ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_DELAY),
				new CountDownCooldownTicks(ModMemoryModules.WEAKENED_NEARBY_ENEMIES_COOLDOWN),
				new CountDownCooldownTicks(ModMemoryModules.PRIME_SPIRIT_ABSORB_COOLDOWN),
				new CountDownCooldownTicks(ModMemoryModules.PRIME_SPIRIT_STOMPING_COOLDOWN),
				StopBeingAngryIfTargetDead.create()
		));
	}

	public static void addMemories(PrimeSpirit primeSpirit, RandomSource randomSource) {
		primeSpirit.getBrain().setMemory(ModMemoryModules.GHOST_SPAWNING_GHOST_COOLDOWN, TIME_BETWEEN_SUMMONING_GHOST.sample(randomSource));
		primeSpirit.getBrain().setMemory(ModMemoryModules.GHOST_SPAWNING_WISP_COOLDOWN, TIME_BETWEEN_SUMMONING.sample(randomSource));
		primeSpirit.getBrain().setMemory(ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_COOLDOWN, TIME_BETWEEN_JUDGEMENT.sample(randomSource));
		primeSpirit.getBrain().setMemory(ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_DELAY, DELAY_DURING_JUDGEMENT.sample(randomSource));
		primeSpirit.getBrain().setMemory(ModMemoryModules.WEAKENED_NEARBY_ENEMIES_COOLDOWN, DELAY_DURING_WEAKEN.sample(randomSource));
		primeSpirit.getBrain().setMemory(ModMemoryModules.PRIME_SPIRIT_ABSORB_COOLDOWN, DELAY_BETWEEN_ABSORB.sample(randomSource));
		primeSpirit.getBrain().setMemory(ModMemoryModules.PRIME_SPIRIT_STOMPING_COOLDOWN, TIME_BETWEEN_STOMPING.sample(randomSource));
	}

	@SuppressWarnings("all")
	private static void addIdleActivities(Brain<PrimeSpirit> brain) {
		brain.addActivityWithConditions(Activity.IDLE,
				ImmutableList.of(
						Pair.of(0, StartAttacking.create(PrimeSpiritBrain::canAttack, PrimeSpiritBrain::findNearestValidAttackTarget)),
						Pair.of(1, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0f, UniformInt.of(30, 60))),
						Pair.of(2, new RunOne(ImmutableList.of(
								Pair.of(RandomStroll.stroll(0.8f), 2),
								Pair.of(SetWalkTargetFromLookTarget.create(1.0f, 3), 2),
								Pair.of(new DoNothing(30, 60), 1))))), ImmutableSet.of());
	}

	private static void addFightActivity(PrimeSpirit primeSpirit, Brain<PrimeSpirit> brain) {
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10,
				ImmutableList.of(
						SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
						StopAttackingIfTargetInvalid.create((livingEntity) -> !isNearestValidAttackTarget(primeSpirit, livingEntity)),
						SetEntityLookTarget.create((livingEntity) -> isTarget(primeSpirit, livingEntity), 10.0F),
						SummonWispAttack.create(20, primeSpirit),
						AbsorbNearbyAlliesAttack.create(20, primeSpirit),
						SummonGhostAttack.create(20, primeSpirit),
						JudgementAttack.create(20, primeSpirit),
						StompAttack.create(20, primeSpirit),
						WeakenNearbyEnemiesAttack.create(20, primeSpirit),
						MeleeAttack.create(20)), MemoryModuleType.ATTACK_TARGET);
						//BackUpIfTooClose.create(5, 0.75F)), MemoryModuleType.ATTACK_TARGET);
	}

	public static void updateActivity(PrimeSpirit primeSpirit) {
		Brain<PrimeSpirit> brain = primeSpirit.getBrain();
		brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
	}

	private static boolean canAttack(LivingEntity livingEntity) {
		return livingEntity.isAlive();
	}

	private static boolean isTarget(PrimeSpirit primeSpirit, LivingEntity livingEntity) {
		return primeSpirit.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter((livingEntity2) -> livingEntity2 == livingEntity).isPresent();
	}

	private static boolean isNearestValidAttackTarget(PrimeSpirit primeSpirit, LivingEntity livingEntity) {
		return findNearestValidAttackTarget(primeSpirit).filter((livingEntity2) -> livingEntity2 == livingEntity).isPresent();
	}

	@SuppressWarnings("all")
	private static Optional<? extends LivingEntity> findNearestValidAttackTarget(PrimeSpirit primeSpirit) {
		Brain<PrimeSpirit> brain = primeSpirit.getBrain();
		Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(primeSpirit, MemoryModuleType.ANGRY_AT);
		if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(primeSpirit, optional.get())) {
			return optional;
		} else {
			Optional optional2;
			if (brain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
				optional2 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
				if (optional2.isPresent()) {
					return optional2;
				}
			}
			optional2 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
			if (optional2.isPresent()) {
				return optional2;
			} else {
				Optional<Player> optional3 = brain.getMemory(ModMemoryModules.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_CLOAK);
				return optional3.isPresent() && Sensor.isEntityAttackable(primeSpirit, optional3.get()) ? optional3 : Optional.empty();
			}
		}
	}
}
