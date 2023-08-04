package net.lunarluned.spookiar.client.render.entities.ghost;

// The Ghost's glow feature renderer

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.client.entity.model.GhostModel;
import net.lunarluned.spookiar.registry.entities.living_entities.ghost.Ghost;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class GhostGlowFeatureRenderer<T extends Ghost, M extends GhostModel<T>> extends RenderLayer<T, M> {

    private static final RenderType GHOST_GLOW = RenderType.eyes(new ResourceLocation(Spookiar.MOD_ID, "textures/entity/ghost/ghosttrans_e.png"));
    private static final RenderType ANGERED_GHOST_GLOW = RenderType.eyes(new ResourceLocation(Spookiar.MOD_ID, "textures/entity/ghost/ghosttrans_angered_e.png"));

    public GhostGlowFeatureRenderer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }


    public RenderType renderType(@NotNull T entity) {
        if (entity.isAngered()) {
            return ANGERED_GHOST_GLOW;
        }
        return GHOST_GLOW;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, @NotNull T entity, float f, float g, float h, float j, float k, float l) {
        RenderType renderType = this.renderType(entity);
        if (renderType != null) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
            this.getParentModel().renderToBuffer(poseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
