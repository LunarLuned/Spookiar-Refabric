package net.lunarluned.spookiar.registry.entities.living_entities.ghost;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.lunarluned.spookiar.SpookiarTags;
import net.lunarluned.spookiar.registry.entities.registry.ModMemoryModules;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class GhostBrain {

	private static final UniformInt TIME_BETWEEN_ATTACKING = UniformInt.of(80, 160);

	@SuppressWarnings("all")
	public static Brain<?> makeBrain(Ghost ghost, Brain<Ghost> brain) {
		addCoreActivities(brain);
		addIdleActivities(brain);
		addFightActivity(ghost, brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}

	private static void addCoreActivities(Brain<Ghost> brain) {
		brain.addActivity(Activity.CORE, 0, ImmutableList.of(
				new LookAtTargetSink(10, 30),
				new MoveToTargetSink(),
				new CountDownCooldownTicks(ModMemoryModules.GHOST_SPAWNING_WISP_COOLDOWN),
				StopBeingAngryIfTargetDead.create()
		));
	}

	public static void addMemories(Ghost ghost, RandomSource randomSource) {
		ghost.getBrain().setMemory(ModMemoryModules.GHOST_SPAWNING_WISP_COOLDOWN, TIME_BETWEEN_ATTACKING.sample(randomSource));
	}

	@SuppressWarnings("all")
	private static void addIdleActivities(Brain<Ghost> brain) {
		brain.addActivityWithConditions(Activity.IDLE,
				ImmutableList.of(
						Pair.of(0, StartAttacking.create(GhostBrain::canAttack, GhostBrain::findNearestValidAttackTarget)),
						Pair.of(1, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0f, UniformInt.of(30, 60))),
						Pair.of(2, new RunOne(ImmutableList.of(
								Pair.of(RandomStroll.stroll(0.8f), 2),
								Pair.of(SetWalkTargetFromLookTarget.create(1.0f, 3), 2),
								Pair.of(new DoNothing(30, 60), 1))))), ImmutableSet.of());
	}

	private static void addFightActivity(Ghost ghost, Brain<Ghost> brain) {
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10,
				ImmutableList.of(
						SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
						StopAttackingIfTargetInvalid.create((livingEntity) -> !isNearestValidAttackTarget(ghost, livingEntity)),
						SetEntityLookTarget.create((livingEntity) -> isTarget(ghost, livingEntity), 10.0F),
						//new SpawnWispTask(),
						MeleeAttack.create(20),
						BackUpIfTooClose.create(5, 0.75F)), MemoryModuleType.ATTACK_TARGET);
	}

	public static void updateActivity(Ghost ghost) {
		Brain<Ghost> brain = ghost.getBrain();
		brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
	}

	private static boolean canAttack(LivingEntity livingEntity) {
		return livingEntity.isAlive();
	}

	private static boolean isTarget(Ghost ghost, LivingEntity livingEntity) {
		return ghost.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter((livingEntity2) -> livingEntity2 == livingEntity).isPresent();
	}

	private static boolean isNearestValidAttackTarget(Ghost ghost, LivingEntity livingEntity) {
		return findNearestValidAttackTarget(ghost).filter((livingEntity2) -> livingEntity2 == livingEntity).isPresent();
	}

	@SuppressWarnings("all")
	private static Optional<? extends LivingEntity> findNearestValidAttackTarget(Ghost ghost) {
		Brain<Ghost> brain = ghost.getBrain();
		Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(ghost, MemoryModuleType.ANGRY_AT);
		if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(ghost, optional.get())) {
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
				return optional3.isPresent() && Sensor.isEntityAttackable(ghost, optional3.get()) ? optional3 : Optional.empty();
			}
		}
	}

	public static boolean isWearingCloak(LivingEntity livingEntity) {
		Iterable<ItemStack> iterable = livingEntity.getArmorSlots();
		for (ItemStack itemStack : iterable) {
			if (!(itemStack.is(SpookiarTags.CLOAKS))) continue;
			return true;
		}
		return false;
	}
}
