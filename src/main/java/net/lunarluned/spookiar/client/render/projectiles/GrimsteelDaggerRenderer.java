package net.lunarluned.spookiar.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.client.entity.model.GrimsteelDaggerModel;
import net.lunarluned.spookiar.client.render.ModEntityRenderer;
import net.lunarluned.spookiar.registry.entities.projectiles.GrimsteelDaggerProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

@Environment(value= EnvType.CLIENT)
public class GrimsteelDaggerRenderer extends EntityRenderer<GrimsteelDaggerProjectile> {

    private static final ResourceLocation GRIMSTEEL_DAGGER_TEXTURE = new ResourceLocation(Spookiar.MOD_ID, "textures/entity/grimsteel_dagger/grimsteel_dagger.png");
    private final GrimsteelDaggerModel<GrimsteelDaggerProjectile> model;

    public GrimsteelDaggerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new GrimsteelDaggerModel<>(context.bakeLayer(ModEntityRenderer.GRIMSTEEL_DAGGER));
    }

    @Override
    public void render(GrimsteelDaggerProjectile grimsteelDaggerProjectile, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        this.model.setupAnim(grimsteelDaggerProjectile, g, 0.0f, -0.1f, 0.0f, 0.0f);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(this.model.renderType(GRIMSTEEL_DAGGER_TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        super.render(grimsteelDaggerProjectile, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(GrimsteelDaggerProjectile entity) {
        return GRIMSTEEL_DAGGER_TEXTURE;
    }
}

