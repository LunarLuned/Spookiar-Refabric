package net.lunarluned.spookiar.client.render.entities.wisp;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.client.entity.model.WispModel;
import net.lunarluned.spookiar.client.render.ModEntityRenderer;
import net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit.PrimeSpirit;
import net.lunarluned.spookiar.registry.entities.living_entities.wisp.Wisp;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import org.jetbrains.annotations.NotNull;

@Environment(value= EnvType.CLIENT)
public class WispRenderer extends MobRenderer<Wisp, WispModel<Wisp>> {

    private static final ResourceLocation NORMAL_TEXTURE = new ResourceLocation(Spookiar.MOD_ID, "textures/entity/wisp/wisp.png");

    public WispRenderer(EntityRendererProvider.Context context) {
        super(context, new WispModel<>(context.bakeLayer(ModEntityRenderer.WISP)), 0.2F);
        this.addLayer(new WispGlowFeatureRenderer<>(this));
    }

	@Override
	protected void setupRotations(Wisp wisp, PoseStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
		if (this.isShaking(wisp)) {
			bodyYaw += (float)(Math.cos((double)wisp.tickCount * 3.25) * Math.PI * 0.4000000059604645);
		}

		if (!wisp.hasPose(Pose.SLEEPING)) {
			matrices.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));
		}

		if (wisp.deathTime > 0) {

		} else if (wisp.isAutoSpinAttack()) {
			matrices.mulPose(Axis.XP.rotationDegrees(-90.0F - wisp.getXRot()));
			matrices.mulPose(Axis.YP.rotationDegrees(((float)wisp.tickCount + tickDelta) * -75.0F));
		}  else if (isEntityUpsideDown(wisp)) {
			matrices.translate(0.0F, wisp.getBbHeight() + 0.1F, 0.0F);
			matrices.mulPose(Axis.ZP.rotationDegrees(180.0F));
		}

	}



    @Override
    public ResourceLocation getTextureLocation(@NotNull Wisp entity) {
        return NORMAL_TEXTURE;
    }
}
