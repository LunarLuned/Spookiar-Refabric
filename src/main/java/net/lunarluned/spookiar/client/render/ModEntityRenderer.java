package net.lunarluned.spookiar.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.client.entity.model.GhostModel;
import net.lunarluned.spookiar.client.entity.model.PrimeSpiritModel;
import net.lunarluned.spookiar.client.entity.model.WispModel;
import net.lunarluned.spookiar.client.render.entities.ghost.GhostRenderer;
import net.lunarluned.spookiar.client.render.entities.prime_ghost.PrimeSpiritRenderer;
import net.lunarluned.spookiar.client.render.entities.wisp.WispRenderer;
import net.lunarluned.spookiar.registry.entities.registry.ModEntities;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ModEntityRenderer {
	public static final ModelLayerLocation PRIME_SPIRIT = new ModelLayerLocation(new ResourceLocation(Spookiar.MOD_ID, "prime_spirit"), "main");
    public static final ModelLayerLocation GHOST = new ModelLayerLocation(new ResourceLocation(Spookiar.MOD_ID, "ghost"), "main");
	public static final ModelLayerLocation WISP = new ModelLayerLocation(new ResourceLocation(Spookiar.MOD_ID, "wisp"), "main");


    public static void registerRenderers() {
        EntityRendererRegistry.register(ModEntities.GHOST, GhostRenderer::new);
		EntityRendererRegistry.register(ModEntities.PRIME_SPIRIT, PrimeSpiritRenderer::new);
		EntityRendererRegistry.register(ModEntities.WISP, WispRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(WISP, WispModel::getLayerDefinition);
		EntityModelLayerRegistry.registerModelLayer(PRIME_SPIRIT, PrimeSpiritModel::getLayerDefinition);
        EntityModelLayerRegistry.registerModelLayer(GHOST, GhostModel::getLayerDefinition);
    }
}

