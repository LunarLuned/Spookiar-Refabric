package net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit.ai;

import com.google.common.collect.ImmutableMap;
import net.lunarluned.spookiar.registry.entities.living_entities.ghost.Ghost;
import net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit.PrimeSpirit;
import net.lunarluned.spookiar.registry.entities.living_entities.wisp.Wisp;
import net.lunarluned.spookiar.registry.entities.registry.ModEntities;
import net.lunarluned.spookiar.registry.entities.registry.ModMemoryModules;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;

public class SummonGhostAttack extends Behavior<PrimeSpirit> {
	public SummonGhostAttack() {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, ModMemoryModules.GHOST_SPAWNING_GHOST_COOLDOWN, MemoryStatus.VALUE_ABSENT));
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel serverLevel, PrimeSpirit primeSpirit) {
		LivingEntity livingEntity = primeSpirit.getTarget();
		return BehaviorUtils.canSee(primeSpirit, livingEntity) && primeSpirit.closerThan(livingEntity, 10) && primeSpirit.getBrain().getMemory(ModMemoryModules.GHOST_SPAWNING_GHOST_COOLDOWN).isEmpty() && primeSpirit.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent();
	}

		public static OneShot<Mob> create(int i, PrimeSpirit primeSpirit) {
			return BehaviorBuilder.create((instance) -> {
				return instance.group(instance.registered(MemoryModuleType.LOOK_TARGET), instance.present(MemoryModuleType.ATTACK_TARGET), instance.absent(MemoryModuleType.ATTACK_COOLING_DOWN), instance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(instance, (memoryAccessor, memoryAccessor2, memoryAccessor3, memoryAccessor4) -> {
					return (world, mob, l) -> {
						LivingEntity livingEntity = (LivingEntity)instance.get(memoryAccessor2);
						if (primeSpirit.getBrain().getMemory(ModMemoryModules.GHOST_SPAWNING_GHOST_COOLDOWN).isEmpty()) {
						if (((NearestVisibleLivingEntities)instance.get(memoryAccessor4)).contains(livingEntity)) {
							memoryAccessor.set(new EntityTracker(livingEntity, true));
							primeSpirit.level().broadcastEntityEvent(primeSpirit, (byte) 68); // Judgement Animation State Entity Event
							Ghost ghost;
							for (int w = 0; w < 2; w++){
							ghost = ModEntities.GHOST.create(mob.level());

							ghost.setPos(primeSpirit.getX(), primeSpirit.getY(), primeSpirit.getZ());

							mob.level().addFreshEntity(ghost);
						}
							primeSpirit.level().playSound(null, primeSpirit.getOnPos(), ModSoundEvents.ENTITY_PRIME_SPIRIT_RISE, SoundSource.HOSTILE, 5, 1);
							primeSpirit.getBrain().setMemory(ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_COOLDOWN, UniformInt.of(100, 200).sample(primeSpirit.level().getRandom()));
							primeSpirit.getBrain().setMemory(ModMemoryModules.GHOST_SPAWNING_GHOST_COOLDOWN, UniformInt.of(1000, 1200).sample(primeSpirit.level().getRandom()));
							primeSpirit.getBrain().setMemory(ModMemoryModules.GHOST_SPAWNING_WISP_COOLDOWN, UniformInt.of(400, 500).sample(primeSpirit.level().getRandom()));
						}
							memoryAccessor3.setWithExpiry(true, (long)i);
							return true;
						} else {
							return false;
						}
					};
				});
			});
		}

}
