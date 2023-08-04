package net.lunarluned.spookiar.util;

import net.lunarluned.spookiar.registry.items.custom.GrimSteelSickleItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.SweepingEdgeEnchantment;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class OffhandAttack {
	public static void offhandAttack(Player player, Entity target) {
		if (!target.isAttackable()) {
			return;
		}
		if (target.skipAttackInteraction(player)) {
			return;
		}
		target.invulnerableTime = 0;

		float f = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
		float g = target instanceof LivingEntity ? EnchantmentHelper.getDamageBonus(player.getOffhandItem(), ((LivingEntity) target).getMobType())
				: EnchantmentHelper.getDamageBonus(player.getOffhandItem(), MobType.UNDEFINED);
		float h = ((PlayerAccessor) player).getAttackCooldownForOffhand(0.5f);
		g *= h;
		((PlayerAccessor) player).resetOffhandAttackTicks();
		if ((f *= 0.2f + h * h * 0.8f) > 0.0f || g > 0.0f) {
			ItemStack itemStack = player.getItemInHand(InteractionHand.OFF_HAND);
			boolean bl = h > 0.9f;
			boolean bl2 = false;
			int i = 0;
			i += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.KNOCKBACK, itemStack);
			if (player.isSprinting() && bl) {
				player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, player.getSoundSource(),
						1.0f, 1.0f);
				++i;
				bl2 = true;
			}
			boolean bl3 = bl && player.fallDistance > 0.0f && !player.onGround() && !player.onClimbable() && !player.isInWater()
					&& !player.hasEffect(MobEffects.BLINDNESS) && !player.isVehicle() && target instanceof LivingEntity;
			bl3 = bl3 && !player.isSprinting();
			if (bl3) {
				f *= 1.5f;
			}
			f += g;
			boolean bl42 = false;
			double d = player.walkDist - player.walkDistO;
			if (bl && !bl3 && !bl2 && player.onGround() && d < (double) player.getSpeed() && itemStack.getItem() instanceof GrimSteelSickleItem) {
				bl42 = true;
			}
			float j = 0.0f;
			boolean bl5 = false;
			int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, itemStack);

			if (target instanceof LivingEntity) {
				j = ((LivingEntity) target).getHealth();
				if (k > 0 && !target.isOnFire()) {
					bl5 = true;
					target.setSecondsOnFire(1);
				}
			}
			Vec3 vec3d = target.getDeltaMovement();
			boolean bl6 = target.hurt(target.damageSources().playerAttack((Player) (Object) player), f);
			if (bl6) {
				if (i > 0) {
					if (target instanceof LivingEntity) {
						((LivingEntity) target).knockback((float) i * 0.5f, Mth.sin(player.getYRot() * ((float) Math.PI / 180)),
								-Mth.cos(player.getYRot() * ((float) Math.PI / 180)));
					} else {
						target.push(-Mth.sin(player.getYRot() * ((float) Math.PI / 180)) * (float) i * 0.5f, 0.1,
								Mth.cos(player.getYRot() * ((float) Math.PI / 180)) * (float) i * 0.5f);
					}
					player.setDeltaMovement(player.getDeltaMovement().multiply(0.6, 1.0, 0.6));
					player.setSprinting(false);
				}
				if (bl42) {
					float l = 1.0f + SweepingEdgeEnchantment.getSweepingDamageRatio(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SWEEPING_EDGE, itemStack)) * f;
					List<LivingEntity> list = player.level().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(1.0, 0.25, 1.0));
					for (LivingEntity livingEntity : list) {
						if (livingEntity == player || livingEntity == target || player.isAlliedTo(livingEntity)
								|| livingEntity instanceof ArmorStand && ((ArmorStand) livingEntity).isMarker() || !(player.distanceToSqr(livingEntity) < 9.0))
							continue;
						livingEntity.knockback(0.4f, Mth.sin(player.getYRot() * ((float) Math.PI / 180)), -Mth.cos(player.getYRot() * ((float) Math.PI / 180)));
						livingEntity.hurt(livingEntity.damageSources().playerAttack(player), l);
					}
					player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(),
							1.0f, 1.0f);

					double posOne = -Mth.sin(player.getYRot() * ((float) Math.PI / 180));
					double posTwo = Mth.cos(player.getYRot() * ((float) Math.PI / 180));
					// TODO: replace with a mirrored sweep attack particle later
					((ServerLevel) player.level()).sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + posOne, player.getY(0.5D), player.getZ() + posTwo, 0,
							posOne, 0.0D, posTwo, 0.0D);
				}
				if (target instanceof ServerPlayer && target.hurtMarked) {
					((ServerPlayer) target).connection.send(new ClientboundSetEntityMotionPacket(target));
					target.hurtMarked = false;
					target.setDeltaMovement(vec3d);
				}
				if (bl3) {
					player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, player.getSoundSource(),
							1.0f, 1.0f);
					player.crit(target);
				}
				if (!bl3 && !bl42) {
					if (bl) {
						player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_STRONG,
								player.getSoundSource(), 1.0f, 1.0f);
					} else {
						player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, player.getSoundSource(),
								1.0f, 1.0f);
					}
				}
				if (g > 0.0f) {
					(player).magicCrit(target);
				}
				player.setLastHurtMob(target);
				if (target instanceof LivingEntity) {
					EnchantmentHelper.doPostHurtEffects((LivingEntity) target, player);
				}
				EnchantmentHelper.doPostDamageEffects(player, target);
				ItemStack itemStack2 = player.getOffhandItem();
				Entity entity = target;
				if (target instanceof EnderDragonPart) {
					entity = ((EnderDragonPart) target).parentMob;
				}
				if (!player.level().isClientSide() && !itemStack2.isEmpty() && entity instanceof LivingEntity) {
					itemStack2.hurtEnemy((LivingEntity) entity, player);
					if (itemStack2.isEmpty()) {
						player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
					}
				}
				if (target instanceof LivingEntity) {
					float m = j - ((LivingEntity) target).getHealth();
					(player).awardStat(Stats.DAMAGE_DEALT, Math.round(m * 10.0f));
					if (k > 0) {
						target.setSecondsOnFire(k * 4);
					}
					if (player.level() instanceof ServerLevel && m > 2.0f) {
						int n = (int) ((double) m * 0.5);
						((ServerLevel) player.level()).sendParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getY(0.5), target.getZ(), n, 0.1, 0.0, 0.1, 0.2);
					}
					target.invulnerableTime = 0;
				}
				(player).causeFoodExhaustion(0.1f);
			} else {
				player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, player.getSoundSource(),
						1.0f, 1.0f);
				if (bl5) {
					target.extinguishFire();
				}
			}
		}
	}

}
