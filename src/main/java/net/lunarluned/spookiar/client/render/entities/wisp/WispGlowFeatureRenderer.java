package net.lunarluned.spookiar.client.render.entities.wisp;

// The Ghost's glow feature renderer

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.client.entity.model.WispModel;
import net.lunarluned.spookiar.registry.entities.living_entities.wisp.Wisp;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class WispGlowFeatureRenderer<T extends Wisp, M extends WispModel<T>> extends EyesLayer<T, M> {

    private static final RenderType WISP_GLOW = RenderType.eyes(new ResourceLocation(Spookiar.MOD_ID, "textures/entity/wisp/wisp_e.png"));

    public WispGlowFeatureRenderer(WispRenderer featureRendererContext) {
        super((RenderLayerParent<T, M>) featureRendererContext);
    }

    public RenderType renderType() {
        return WISP_GLOW;
    }
}
