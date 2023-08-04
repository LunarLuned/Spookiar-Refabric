package net.lunarluned.spookiar.util;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class GrimsteelDualWieldingPacket {
	public static final ResourceLocation ATTACK_PACKET = new ResourceLocation("spookiar", "attack_entity");

	public static Packet<?> attackPacket(Entity entity) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(entity.getId());
		return ClientPlayNetworking.createC2SPacket(ATTACK_PACKET, buf);
	}

	public static void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(ATTACK_PACKET, (server, player, handler, buffer, sender) -> {
			int entityId = buffer.readInt();
			server.execute(() -> {
				player.resetLastActionTime();
				if (player.level().getEntity(entityId) != null)
					((PlayerAccessor) player).offHandAttack(player.level().getEntity(entityId));

			});

		});

	}
}
