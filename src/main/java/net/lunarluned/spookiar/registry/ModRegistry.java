package net.lunarluned.spookiar.registry;

import net.lunarluned.spookiar.client.render.ModEntityRenderer;
import net.lunarluned.spookiar.registry.blocks.custom.ModBlockEntities;
import net.lunarluned.spookiar.registry.blocks.ModBlocks;
import net.lunarluned.spookiar.registry.effects.ModEffects;
import net.lunarluned.spookiar.registry.enchantments.ModEnchantments;
import net.lunarluned.spookiar.registry.entities.registry.ModEntities;
import net.lunarluned.spookiar.registry.entities.registry.ModMemoryModules;
import net.lunarluned.spookiar.registry.items.ModItems;
import net.lunarluned.spookiar.registry.paintings.ModPaintings;
import net.lunarluned.spookiar.sounds.boss_music.CustomBossMusic;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.lunarluned.spookiar.util.GrimsteelDualWieldingPacket;
import net.lunarluned.spookiar.util.ModLootTableModifiers;

import static net.lunarluned.spookiar.misc.ModDamageSources.registerDamageSources;

public class ModRegistry {

    public static void registerAll() {

        // - Blocks, Items, Etc.
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModSoundEvents.registerSounds();
        SpookiarCreativeModeTab.registerCreativeTabs();
		ModEnchantments.registerModEnchantments();
		ModEffects.registerEffects();
		ModPaintings.registerPaintings();
		CustomBossMusic.createBossMusicInstance();
        registerDamageSources();
        // - Entities
        ModBlockEntities.registerBlockEntities();
		ModMemoryModules.MEMORY_MODULES.register();
        ModEntities.registerModEntities();
        // - Technical
        ModLootTableModifiers.modifyLootTables();
		GrimsteelDualWieldingPacket.onInitialize();
        // - Composting

        // - Flammable Blocks

        // - Furnace Fuel

        System.out.println("Registering Content for Spookiar");
    }
}
