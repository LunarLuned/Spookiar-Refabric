package net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit.ai;

import com.google.common.collect.ImmutableMap;
import net.lunarluned.spookiar.registry.entities.living_entities.ghost.Ghost;
import net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit.PrimeSpirit;
import net.lunarluned.spookiar.registry.entities.living_entities.wisp.Wisp;
import net.lunarluned.spookiar.registry.entities.registry.ModMemoryModules;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class AbsorbNearbyAlliesAttack extends Behavior<PrimeSpirit> {
	public AbsorbNearbyAlliesAttack() {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, ModMemoryModules.PRIME_SPIRIT_ABSORB_COOLDOWN, MemoryStatus.VALUE_ABSENT));
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel serverLevel, PrimeSpirit primeSpirit) {
		LivingEntity livingEntity = primeSpirit.getTarget();
		return BehaviorUtils.canSee(primeSpirit, livingEntity) && primeSpirit.closerThan(livingEntity, 10) && primeSpirit.getBrain().getMemory(ModMemoryModules.WEAKENED_NEARBY_ENEMIES_COOLDOWN).isEmpty() && primeSpirit.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent();
	}

		public static OneShot<Mob> create(int i, PrimeSpirit primeSpirit) {
			return BehaviorBuilder.create((instance) -> {
				return instance.group(instance.registered(MemoryModuleType.LOOK_TARGET), instance.present(MemoryModuleType.ATTACK_TARGET), instance.absent(MemoryModuleType.ATTACK_COOLING_DOWN), instance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(instance, (memoryAccessor, memoryAccessor2, memoryAccessor3, memoryAccessor4) -> {
					return (world, mob, l) -> {
						LivingEntity livingEntity = (LivingEntity)instance.get(memoryAccessor2);
						if (primeSpirit.getBrain().getMemory(ModMemoryModules.PRIME_SPIRIT_ABSORB_COOLDOWN).isEmpty()) {
						if (((NearestVisibleLivingEntities)instance.get(memoryAccessor4)).contains(livingEntity)) {
							memoryAccessor.set(new EntityTracker(livingEntity, true));
							primeSpirit.level().broadcastEntityEvent(primeSpirit, (byte) 70); // Weaken Animation State Entity Event

							BlockPos blockPos = primeSpirit.getOnPos();
							int m;
							int lo;
							int k = blockPos.getX();
							int j = 16;

							// Scans the area for nearby players

							AABB aABB = new AABB(k, lo = blockPos.getY(), m = blockPos.getZ(), k + 1, lo + 1, m + 1).inflate(j).expandTowards(0.0, primeSpirit.level().getHeight(), 0.0);
							List<Ghost> nearbyGhosts = primeSpirit.level().getEntitiesOfClass(Ghost.class, aABB);
							List<Wisp> nearbyWisps = primeSpirit.level().getEntitiesOfClass(Wisp.class, aABB);

							for (LivingEntity livingEntity2 : nearbyGhosts)  {
								livingEntity2.addEffect(new MobEffectInstance(MobEffects.HARM, 5, 99), mob);
							}

							for (LivingEntity livingEntity3 : nearbyWisps)  {
								livingEntity3.addEffect(new MobEffectInstance(MobEffects.HARM, 5, 99), mob);
							}

							int healAmt = nearbyGhosts.size() + nearbyWisps.size() * 2;
							primeSpirit.heal(healAmt);

							primeSpirit.level().playSound(null, primeSpirit.getOnPos(), ModSoundEvents.ENTITY_GHOST_EMPOWERED, SoundSource.HOSTILE, 5, 1);
							primeSpirit.getBrain().setMemory(ModMemoryModules.PRIME_SPIRIT_ABSORB_COOLDOWN, UniformInt.of(2000, 2100).sample(primeSpirit.level().getRandom()));

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
