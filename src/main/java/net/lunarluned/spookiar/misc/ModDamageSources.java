package net.lunarluned.spookiar.misc;

import net.lunarluned.spookiar.Spookiar;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageSources {

    public static final ResourceKey<DamageType> GRIM = register("grim");

    private static ResourceKey<DamageType> register(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Spookiar.id(path));
    }

    @SuppressWarnings("ALL")
    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(GRIM, new DamageType("grim", 0.1F));
    }

        public static void registerDamageSources () {
        }
    }

