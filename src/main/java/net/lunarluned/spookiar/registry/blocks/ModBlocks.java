package net.lunarluned.spookiar.registry.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.registry.blocks.custom.*;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {

	public static final Block ECTOPLASM_BLOCK = registerBlock("ectoplasm_block",
			new EctoplasmBlock(FabricBlockSettings.of().mapColor(MapColor.TERRACOTTA_BLUE).requiresTool().sounds(SoundType.SLIME_BLOCK)));

	public static final Block GRIMSTEEL_BLOCK = registerBlock("grimsteel_block",
			new Block(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_CYAN).requiresTool().sounds(ModSoundEvents.GRIMSTEEL)));

	public static final Block GRIMSTEEL_PLATING = registerBlock("grimsteel_plating",
			new Block(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_CYAN).requiresTool().sounds(ModSoundEvents.GRIMSTEEL)));

	public static final Block GRIMSTEEL_PLATING_STAIRS = registerBlock("grimsteel_plating_stairs",
			new ModStairsBlock(ModBlocks.GRIMSTEEL_PLATING.defaultBlockState(),
					FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_CYAN).resistance(1).strength(2f).requiresTool().sounds(ModSoundEvents.GRIMSTEEL)));

	public static final Block GRIMSTEEL_PLATING_SLAB = registerBlock("grimsteel_plating_slab",
			new SlabBlock(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_CYAN).resistance(1).strength(2f).requiresTool().sounds(ModSoundEvents.GRIMSTEEL)));

	public static final Block GRIMSTEEL_PLATING_WALL = registerBlock("grimsteel_plating_wall",
			new WallBlock(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_CYAN).resistance(1).strength(2f).requiresTool().sounds(ModSoundEvents.GRIMSTEEL)));


	// Sculk-like blocks

    public static final Block SCULK_SPINE = registerBlock("sculk_spine",
            new SculkPillarBlock(FabricBlockSettings.copyOf(Blocks.SCULK).mapColor(MapColor.TERRACOTTA_WHITE).sounds(SoundType.SCULK_CATALYST).requiresTool().resistance(1).strength(2f),
                    UniformInt.of(2, 8)));

    public static final Block SCULK_SAC_BLOCK = registerBlock("sculk_sac_block",
            new SculkSacBlock(FabricBlockSettings.copyOf(Blocks.SCULK).mapColor(MapColor.COLOR_BLACK).sounds(SoundType.SCULK).strength(.1f),
                    UniformInt.of(63, 78)));

    public static final Block SPINAL_PILLAR = registerBlock("spinal_pillar",
            new RotatedPillarBlock(FabricBlockSettings.copyOf(Blocks.SCULK).mapColor(MapColor.TERRACOTTA_WHITE).sounds(SoundType.SCULK_CATALYST).requiresTool().resistance(1).strength(2f)));

    public static final Block CHISELED_SPINAL_BRICKS = registerBlock("chiseled_spinal_bricks",
            new Block(FabricBlockSettings.copyOf(Blocks.SCULK).mapColor(MapColor.TERRACOTTA_WHITE).sounds(SoundType.SCULK_CATALYST).requiresTool().resistance(1).strength(2f)));

    public static final Block SPINAL_BRICKS = registerBlock("spinal_bricks",
            new Block(FabricBlockSettings.copyOf(Blocks.SCULK).mapColor(MapColor.TERRACOTTA_WHITE).sounds(SoundType.SCULK_CATALYST).requiresTool().resistance(1).strength(2f)));

    public static final Block SPINAL_BRICK_STAIRS = registerBlock("spinal_brick_stairs",
            new ModStairsBlock(ModBlocks.SPINAL_BRICKS.defaultBlockState(),
                    FabricBlockSettings.copyOf(Blocks.SCULK).mapColor(MapColor.TERRACOTTA_WHITE).resistance(1).strength(2f).requiresTool().sounds(SoundType.SCULK_CATALYST)));

    public static final Block SPINAL_BRICK_SLAB = registerBlock("spinal_brick_slab",
            new SlabBlock(FabricBlockSettings.copyOf(Blocks.SCULK).mapColor(MapColor.TERRACOTTA_WHITE).resistance(1).strength(2f).requiresTool().sounds(SoundType.SCULK_CATALYST)));

    public static final Block SPINAL_BRICK_WALL = registerBlock("spinal_brick_wall",
            new WallBlock(FabricBlockSettings.copyOf(Blocks.SCULK).mapColor(MapColor.TERRACOTTA_WHITE).resistance(1).strength(2f).requiresTool().sounds(SoundType.SCULK_CATALYST)));

    public static final Block SPINAL_TILES = registerBlock("spinal_tiles",
            new Block(FabricBlockSettings.copyOf(Blocks.SCULK).mapColor(MapColor.TERRACOTTA_WHITE).sounds(SoundType.SCULK_CATALYST).requiresTool().resistance(1).strength(2f)));

    public static final Block SPINAL_TILE_STAIRS = registerBlock("spinal_tile_stairs",
            new ModStairsBlock(ModBlocks.SPINAL_TILES.defaultBlockState(),
                    FabricBlockSettings.copyOf(Blocks.SCULK).mapColor(MapColor.TERRACOTTA_WHITE).resistance(1).strength(2f).requiresTool().sounds(SoundType.SCULK_CATALYST)));

    public static final Block SPINAL_TILE_SLAB = registerBlock("spinal_tile_slab",
            new SlabBlock(FabricBlockSettings.copyOf(Blocks.SCULK).mapColor(MapColor.TERRACOTTA_WHITE).resistance(1).strength(2f).requiresTool().sounds(SoundType.SCULK_CATALYST)));

    public static final Block SPINAL_TILE_WALL = registerBlock("spinal_tile_wall",
            new WallBlock(FabricBlockSettings.copyOf(Blocks.SCULK).mapColor(MapColor.TERRACOTTA_WHITE).resistance(1).strength(2f).requiresTool().sounds(SoundType.SCULK_CATALYST)));

	public static final Block GRAVESTONE = registerBlockwithoutItem("gravestone",
			new GraveBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).strength(4f).resistance(100).luminance(8).requiresTool()));

	// Necrite

	public static final Block NECRITE = registerBlock("necrite",
			new Block(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).resistance(3).strength(4f).requiresTool().sounds(SoundType.DEEPSLATE)));

	public static final Block NECRITE_STAIRS = registerBlock("necrite_stairs",
			new ModStairsBlock(ModBlocks.NECRITE.defaultBlockState(),
					FabricBlockSettings.copyOf(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).resistance(3).strength(4f).requiresTool().sounds(SoundType.DEEPSLATE)));

	public static final Block NECRITE_SLAB = registerBlock("necrite_slab",
			new SlabBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).resistance(3).strength(4f).requiresTool().sounds(SoundType.DEEPSLATE)));

	public static final Block NECRITE_WALL = registerBlock("necrite_wall",
			new WallBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).resistance(3).strength(4f).requiresTool().sounds(SoundType.DEEPSLATE)));

	public static final Block POLISHED_NECRITE = registerBlock("polished_necrite",
			new Block(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).resistance(3).strength(4f).requiresTool().sounds(SoundType.DEEPSLATE)));

	public static final Block POLISHED_NECRITE_STAIRS = registerBlock("polished_necrite_stairs",
			new ModStairsBlock(ModBlocks.NECRITE.defaultBlockState(),
					FabricBlockSettings.copyOf(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).resistance(3).strength(4f).requiresTool().sounds(SoundType.DEEPSLATE)));

	public static final Block POLISHED_NECRITE_SLAB = registerBlock("polished_necrite_slab",
			new SlabBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).resistance(3).strength(4f).requiresTool().sounds(SoundType.DEEPSLATE)));

	public static final Block POLISHED_NECRITE_WALL = registerBlock("polished_necrite_wall",
			new WallBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).resistance(3).strength(4f).requiresTool().sounds(SoundType.DEEPSLATE)));

	public static final Block NECRITE_BRICKS = registerBlock("necrite_bricks",
			new Block(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).resistance(3).strength(4f).requiresTool().sounds(SoundType.DEEPSLATE)));

	public static final Block NECRITE_BRICK_STAIRS = registerBlock("necrite_brick_stairs",
			new ModStairsBlock(ModBlocks.NECRITE.defaultBlockState(),
					FabricBlockSettings.copyOf(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).resistance(3).strength(4f).requiresTool().sounds(SoundType.DEEPSLATE)));

	public static final Block NECRITE_BRICK_SLAB = registerBlock("necrite_brick_slab",
			new SlabBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).resistance(3).strength(4f).requiresTool().sounds(SoundType.DEEPSLATE)));

	public static final Block NECRITE_BRICK_WALL = registerBlock("necrite_brick_wall",
			new WallBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).resistance(3).strength(4f).requiresTool().sounds(SoundType.DEEPSLATE)));


	private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Spookiar.MOD_ID, name), block);
    }

    private static Block registerBlockwithoutItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Spookiar.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Spookiar.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {}
}
