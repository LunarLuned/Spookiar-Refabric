package net.lunarluned.spookiar.registry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.lunarluned.spookiar.registry.blocks.ModBlocks;
import net.lunarluned.spookiar.registry.items.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class SpookiarCreativeModeTab {
    @SuppressWarnings("all")
    public static void registerCreativeTabs() {

        // Ingredients Tab
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.IRON_INGOT, ModItems.GRIMSTEEL_INGOT);
            entries.addAfter(Items.IRON_NUGGET, ModItems.GRIMSTEEL_SCRAPS);
            entries.addAfter(Items.DISC_FRAGMENT_5, ModItems.SCULK_SAC, ModItems.FULL_SCULK_SAC);
        });

        // Natural Blocks Tab
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.addAfter(Blocks.SCULK_SENSOR, ModBlocks.SCULK_SPINE, ModBlocks.SCULK_SAC_BLOCK);
        });

        // Building Blocks Tab
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.addAfter(Blocks.REINFORCED_DEEPSLATE, ModBlocks.SCULK_SPINE, ModBlocks.SPINAL_BRICKS, ModBlocks.SPINAL_BRICK_STAIRS, ModBlocks.SPINAL_BRICK_SLAB, ModBlocks.SPINAL_BRICK_WALL, ModBlocks.SPINAL_PILLAR, ModBlocks.SPINAL_TILES, ModBlocks.SPINAL_TILE_SLAB, ModBlocks.SPINAL_TILE_STAIRS, ModBlocks.SPINAL_TILE_WALL, ModBlocks.CHISELED_SPINAL_BRICKS);
            entries.addAfter(Blocks.CHAIN, ModBlocks.GRIMSTEEL_BLOCK, ModBlocks.GRIMSTEEL_PLATING, ModBlocks.GRIMSTEEL_PLATING_STAIRS, ModBlocks.GRIMSTEEL_PLATING_SLAB, ModBlocks.GRIMSTEEL_PLATING_WALL, ModBlocks.NECRITE, ModBlocks.NECRITE_STAIRS, ModBlocks.NECRITE_SLAB, ModBlocks.NECRITE_WALL, ModBlocks.POLISHED_NECRITE, ModBlocks.POLISHED_NECRITE_STAIRS, ModBlocks.POLISHED_NECRITE_SLAB, ModBlocks.POLISHED_NECRITE_WALL, ModBlocks.NECRITE_BRICKS, ModBlocks.NECRITE_BRICK_STAIRS, ModBlocks.NECRITE_BRICK_SLAB, ModBlocks.NECRITE_BRICK_WALL);
        });

        // Combat Tab
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.addAfter(Items.TOTEM_OF_UNDYING, ModItems.GRAVESTONE_OF_STASHING);
            entries.addBefore(Items.TRIDENT, ModItems.GRIMSTEEL_SICKLE);
        });

        // Spawn Eggs Tab
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
            entries.addAfter(Items.GHAST_SPAWN_EGG, ModItems.GHOST_SPAWN_EGG);
            entries.addAfter(Items.WITCH_SPAWN_EGG, ModItems.WISP_SPAWN_EGG);
        });

    }

}
