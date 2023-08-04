package net.lunarluned.spookiar.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunarluned.spookiar.client.render.ModEntityRenderer;

@SuppressWarnings("ALL")
@Environment(EnvType.CLIENT)
public class SpookiarClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
		ModEntityRenderer.registerRenderers();
    }
}
