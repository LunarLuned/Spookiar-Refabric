package net.lunarluned.spookiar.mixin.items.sickle;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunarluned.spookiar.registry.items.custom.GrimSteelSickleItem;
import net.lunarluned.spookiar.util.GrimsteelDualWieldingPacket;
import net.lunarluned.spookiar.util.PlayerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow
	@Nullable
	public LocalPlayer player;

	@Shadow
	@Nullable
	public MultiPlayerGameMode gameMode;

	@Shadow
	@Nullable
	public HitResult hitResult;
	@Shadow
	private int rightClickDelay;
	@Unique
	private int secondAttackCooldown;

	@Inject(method = "Lnet/minecraft/client/Minecraft;tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;handleKeybinds()V"))
	public void spookiar_secondAttackTick(CallbackInfo info) {
		if (this.secondAttackCooldown > 0) {
			--this.secondAttackCooldown;
		}
	}

	@Inject(method = "startUseItem", at = @At(value = "HEAD"), cancellable = true)
	private void spookiar_startUseItem(CallbackInfo info) {
		Item offHandItem = player.getOffhandItem().getItem();
		Item mainHandItem = player.getMainHandItem().getItem();

		if (player != null && !player.isSpectator() && (offHandItem instanceof GrimSteelSickleItem)
				&& (mainHandItem instanceof GrimSteelSickleItem)) {
			if (this.secondAttackCooldown <= 0) {
				if (this.hitResult != null && !this.player.isVehicle()) {
					switch (this.hitResult.getType()) {
						case ENTITY:
							// Client
							((PlayerAccessor) player).resetOffhandAttackTicks();
							((PlayerAccessor) this.player).offHandAttack(((EntityHitResult) this.hitResult).getEntity());

							if (this.gameMode.hasMissTime()) {
								this.secondAttackCooldown = 20;
							}
							((PlayerAccessor) player).resetOffhandAttackTicks();

							// Server
							Minecraft.getInstance().getConnection().send(GrimsteelDualWieldingPacket.attackPacket(((EntityHitResult) this.hitResult).getEntity()));
							break;
						case BLOCK:
							BlockHitResult blockHitResult = (BlockHitResult) this.hitResult;
							BlockPos blockPos = blockHitResult.getBlockPos();
							if (!player.level().getBlockState(blockPos).isAir()) {
								this.gameMode.useItemOn(player, InteractionHand.OFF_HAND, blockHitResult);
								break;
							}
						case MISS:
							if (this.gameMode.hasMissTime()) {
								this.secondAttackCooldown = 10;
							}
							((PlayerAccessor) player).resetOffhandAttackTicks();
					}
					this.rightClickDelay = 4;
					this.player.swing(InteractionHand.OFF_HAND);
					info.cancel();
				}
			} else {
				info.cancel();
			}
		}
	}

}
