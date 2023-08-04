package net.lunarluned.spookiar.mixin.items.sickle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunarluned.spookiar.util.PlayerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
	@Shadow
	private float offHandHeight;
	@Shadow
	private float mainHandHeight;
	@Shadow
	private ItemStack mainHandItem;
	@Shadow
	private float oMainHandHeight;
	@Shadow
	private float oOffHandHeight;

	@Shadow
	@Final
	@Mutable
	private final Minecraft minecraft;

	@Shadow
	private ItemStack offHandItem;

	private float equipOffhand;
	private boolean isOffhandAttack;

	public ItemInHandRendererMixin(Minecraft client) {
		this.minecraft = client;
	}

	@Inject(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 3, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void spookiar_offHandTick(CallbackInfo ci, LocalPlayer player, ItemStack itemStack, ItemStack itemStack2) {
		float o = ((PlayerAccessor) player).getAttackCooldownForOffhand(1.0F);
		if (o < 0.1F)
			this.isOffhandAttack = true;
		if (this.isOffhandAttack) {
			if (this.mainHandHeight >= 1.0F) {
				this.isOffhandAttack = false;
			}
			this.equipOffhand += Mth.clamp((this.offHandItem == itemStack2 ? o * o * o : 0.0F) - this.equipOffhand, -0.4F, 0.4F);
			this.offHandHeight = this.equipOffhand;
		}
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F"))
	public void spookiar_mainHandTick(CallbackInfo info) {
		float o = ((PlayerAccessor) minecraft.player).getAttackCooldownForOffhand(1.0F);
		if (o < 0.9F && o > 0.15F) {
			this.offHandItem = new ItemStack(Items.AIR);
		}
	}
}
