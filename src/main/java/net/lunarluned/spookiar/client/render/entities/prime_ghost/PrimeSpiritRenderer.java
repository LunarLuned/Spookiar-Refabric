package net.lunarluned.spookiar.client.render.entities.prime_ghost;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.client.entity.model.PrimeSpiritModel;
import net.lunarluned.spookiar.client.render.ModEntityRenderer;
import net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit.PrimeSpirit;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import org.jetbrains.annotations.NotNull;

@Environment(value= EnvType.CLIENT)
public class PrimeSpiritRenderer extends MobRenderer<PrimeSpirit, PrimeSpiritModel<PrimeSpirit>> {

    private static final ResourceLocation NORMAL_TEXTURE = new ResourceLocation(Spookiar.MOD_ID, "textures/entity/prime_spirit/prime_spirit.png");
    private static final ResourceLocation ANGERED_TEXTURE = new ResourceLocation(Spookiar.MOD_ID, "textures/entity/prime_spirit/angered_prime_spirit.png");

    public PrimeSpiritRenderer(EntityRendererProvider.Context context) {
        super(context, new PrimeSpiritModel<>(context.bakeLayer(ModEntityRenderer.PRIME_SPIRIT)), 0.6F);
        this.addLayer(new PrimeSpiritOuterLayerRenderer<>(this));
		this.addLayer(new PrimeSpiritCloakLayerRenderer<>(this));
    }

	@Override
	protected void setupRotations(PrimeSpirit primeSpirit, PoseStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
		if (this.isShaking(primeSpirit)) {
			bodyYaw += (float)(Math.cos((double)primeSpirit.tickCount * 3.25) * Math.PI * 0.4000000059604645);
		}

		if (!primeSpirit.hasPose(Pose.SLEEPING)) {
			matrices.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));
		}

		if (primeSpirit.deathTime > 0) {

		} else if (primeSpirit.isAutoSpinAttack()) {
			matrices.mulPose(Axis.XP.rotationDegrees(-90.0F - primeSpirit.getXRot()));
			matrices.mulPose(Axis.YP.rotationDegrees(((float)primeSpirit.tickCount + tickDelta) * -75.0F));
		}  else if (isEntityUpsideDown(primeSpirit)) {
			matrices.translate(0.0F, primeSpirit.getBbHeight() + 0.1F, 0.0F);
			matrices.mulPose(Axis.ZP.rotationDegrees(180.0F));
		}

	}

    @Override
    public ResourceLocation getTextureLocation(@NotNull PrimeSpirit entity) {
        if (entity.isAngered()) {
            return ANGERED_TEXTURE;
        } else {
            return NORMAL_TEXTURE;
        }
    }

}
