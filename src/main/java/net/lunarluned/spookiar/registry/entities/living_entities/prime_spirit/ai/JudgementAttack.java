package net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit.ai;

import com.google.common.collect.ImmutableMap;
import net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit.PrimeSpirit;
import net.lunarluned.spookiar.registry.entities.registry.ModMemoryModules;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;

public class JudgementAttack extends Behavior<PrimeSpirit> {
	public JudgementAttack() {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_COOLDOWN, MemoryStatus.VALUE_ABSENT, ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_DELAY, MemoryStatus.VALUE_ABSENT));
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel serverLevel, PrimeSpirit primeSpirit) {
		LivingEntity livingEntity = primeSpirit.getTarget();
		return BehaviorUtils.canSee(primeSpirit, livingEntity) && primeSpirit.closerThan(livingEntity, 10) && primeSpirit.getBrain().getMemory(ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_COOLDOWN).isEmpty() && primeSpirit.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent();
	}

		public static OneShot<Mob> create(int i, PrimeSpirit primeSpirit) {
			return BehaviorBuilder.create((instance) -> {
				return instance.group(instance.registered(MemoryModuleType.LOOK_TARGET), instance.present(MemoryModuleType.ATTACK_TARGET), instance.absent(MemoryModuleType.ATTACK_COOLING_DOWN), instance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(instance, (memoryAccessor, memoryAccessor2, memoryAccessor3, memoryAccessor4) -> {
					return (world, mob, l) -> {
						LivingEntity livingEntity = (LivingEntity)instance.get(memoryAccessor2);
						if (primeSpirit.getBrain().getMemory(ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_COOLDOWN).isEmpty()) {
						if (((NearestVisibleLivingEntities)instance.get(memoryAccessor4)).contains(livingEntity)) {
							memoryAccessor.set(new EntityTracker(livingEntity, true));
							mob.teleportTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
							primeSpirit.level().playSound(null, primeSpirit.getOnPos(), ModSoundEvents.ENTITY_PRIME_SPIRIT_JUDGEMENT, SoundSource.HOSTILE, 5, 1);

							if (primeSpirit.getBrain().getMemory(ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_DELAY).isEmpty()) {
								primeSpirit.level().broadcastEntityEvent(primeSpirit, (byte)64); // Judgement Animation State Entity Event
								primeSpirit.getNavigation().stop();
								mob.level().explode(null, primeSpirit.getX(), primeSpirit.getY(), primeSpirit.getZ(), 1, Level.ExplosionInteraction.NONE);
								mob.teleportTo(livingEntity.getRandomX(3), livingEntity.getY(), livingEntity.getRandomZ(3));
								primeSpirit.getBrain().setMemory(ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_DELAY, UniformInt.of(20, 30).sample(primeSpirit.level().getRandom()));

							}
							primeSpirit.getBrain().setMemory(ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_COOLDOWN, UniformInt.of(100, 200).sample(primeSpirit.level().getRandom()));
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
