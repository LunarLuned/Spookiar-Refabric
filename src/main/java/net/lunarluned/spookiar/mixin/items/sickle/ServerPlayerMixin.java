package net.lunarluned.spookiar.mixin.items.sickle;

import net.lunarluned.spookiar.registry.items.custom.GrimSteelSickleItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
	@Inject(method = "swing", at = @At("HEAD"), cancellable = true)
	public void spookiar_swing(InteractionHand hand, CallbackInfo info) {
		Item item = ((Player) (Object) this).getOffhandItem().getItem();
		if (hand == InteractionHand.OFF_HAND && (item instanceof GrimSteelSickleItem)) {
			info.cancel();
		}
	}
}
