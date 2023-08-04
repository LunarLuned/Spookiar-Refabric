package net.lunarluned.spookiar.client.render.entities.ghost;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.client.entity.model.GhostModel;
import net.lunarluned.spookiar.client.render.ModEntityRenderer;
import net.lunarluned.spookiar.registry.entities.living_entities.ghost.Ghost;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(value= EnvType.CLIENT)
public class GhostRenderer extends MobRenderer<Ghost, GhostModel<Ghost>> {

    private static final ResourceLocation NORMAL_TEXTURE = new ResourceLocation(Spookiar.MOD_ID, "textures/entity/ghost/ghosttrans.png");
    private static final ResourceLocation ANGERED_TEXTURE = new ResourceLocation(Spookiar.MOD_ID, "textures/entity/ghost/ghosttrans_angered.png");

    public GhostRenderer(EntityRendererProvider.Context context) {
        super(context, new GhostModel<>(context.bakeLayer(ModEntityRenderer.GHOST)), 0.6F);
        this.addLayer(new GhostGlowFeatureRenderer<>(this));
    }

    @Override
    protected boolean isShaking(Ghost entity) {
        return entity.isAngered();
    }

    @Override
    public ResourceLocation getTextureLocation(@NotNull Ghost entity) {
        if (entity.isAngered()) {
            return ANGERED_TEXTURE;
        } else {
            return NORMAL_TEXTURE;
        }
    }

}
