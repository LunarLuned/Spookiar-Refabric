package net.lunarluned.spookiar.client.render.entities.prime_ghost;

// The Prime Spirit's cloak layer renderer

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.client.entity.model.PrimeSpiritModel;
import net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit.PrimeSpirit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class PrimeSpiritCloakLayerRenderer<T extends PrimeSpirit, M extends PrimeSpiritModel<T>> extends RenderLayer<T, M> {

	private static final RenderType PRIME_SPIRIT_CLOAK = RenderType.entityCutout(new ResourceLocation(Spookiar.MOD_ID, "textures/entity/prime_spirit/prime_spirit_cloak.png"));
	private static final RenderType PRIME_SPIRIT_CLOAK_ANGERED = RenderType.entityCutout(new ResourceLocation(Spookiar.MOD_ID, "textures/entity/prime_spirit/prime_spirit_cloak_angry.png"));

	public PrimeSpiritCloakLayerRenderer(RenderLayerParent<T, M> featureRendererContext) {
		super(featureRendererContext);
	}

	@Override
	public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, @NotNull T entity, float f, float g, float h, float j, float k, float l) {
		RenderType renderType = this.renderType(entity);
		if (renderType != null && !entity.isInvisible()) {
			VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
			this.getParentModel().renderToBuffer(poseStack, vertexConsumer, i, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	private RenderType renderType(@NotNull PrimeSpirit primeSpirit) {
		if (primeSpirit.isAngered()) {
			return PRIME_SPIRIT_CLOAK_ANGERED;
		}
		return PRIME_SPIRIT_CLOAK;
	}
}

