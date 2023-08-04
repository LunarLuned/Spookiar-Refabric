package net.lunarluned.spookiar.mixin.items.sickle;

import com.mojang.authlib.GameProfile;
import net.lunarluned.spookiar.util.OffhandAttack;
import net.lunarluned.spookiar.util.PlayerAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, priority = 1007)
public abstract class PlayerDualSickleMixin extends LivingEntity implements PlayerAccessor {

	@Unique
	private int lastAttackedOffhandTicks;

	public PlayerDualSickleMixin(Level level, BlockPos pos, float yaw, GameProfile profile) {
		super(EntityType.PLAYER, level);
	}
	@Inject(method = "Lnet/minecraft/world/entity/player/Player;tick()V", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Player;attackStrengthTicker:I", ordinal = 0))
	private void spookiar_tick(CallbackInfo info) {
		lastAttackedOffhandTicks++;
	}


	public float getAttackCooldownProgressPerTick() {
		return 1.0F;
	}

	@Override
	public void offHandAttack(Entity target) {
		OffhandAttack.offhandAttack((Player) (Object) this, target);
	}

	@Override
	public void resetOffhandAttackTicks() {
		this.lastAttackedOffhandTicks = 0;
	}

	@Override
	public float getAttackCooldownForOffhand(float baseTime) {
		return Mth.clamp(((float) this.lastAttackedOffhandTicks + baseTime) / this.getAttackCooldownProgressPerTick(), 0.0F, 1.0F);
	}

}
