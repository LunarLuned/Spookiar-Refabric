package net.lunarluned.spookiar.registry.paintings;

import net.lunarluned.spookiar.Spookiar;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;

@SuppressWarnings("ALL")
public class ModPaintings {

    private static final PaintingVariant KING_APOLLUS = registerPainting(new PaintingVariant(64, 64));

    private static PaintingVariant registerPainting(PaintingVariant paintingVariant) {
        return Registry.register(BuiltInRegistries.PAINTING_VARIANT, new ResourceLocation(Spookiar.MOD_ID, "king_apollus"), paintingVariant);
    }

    public static void registerPaintings() {
        Spookiar.LOGGER.debug("Registering Paintings for" + Spookiar.MOD_ID);
    }
}
