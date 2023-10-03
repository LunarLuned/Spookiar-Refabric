package net.lunarluned.spookiar.registry.items;


import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.registry.blocks.ModBlocks;
import net.lunarluned.spookiar.registry.entities.registry.ModEntities;
import net.lunarluned.spookiar.registry.items.custom.*;
import net.lunarluned.spookiar.registry.items.materials.ModTiers;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;

@SuppressWarnings("ALL")
public class ModItems {

    // List of items :3

    public static final Item GRAVESTONE_OF_STASHING = registerItem("gravestone_of_stashing",
    new Item(new FabricItemSettings().stacksTo(8).rarity(Rarity.UNCOMMON)));

    public static final Item GRIMSTEEL_INGOT = registerItem("grimsteel_ingot",
            new Item(new FabricItemSettings()));

    public static final Item GRIMSTEEL_SCRAPS = registerItem("grimsteel_scraps",
            new Item(new FabricItemSettings()));

    public static final Item BOTTLED_WISP = registerItem("bottled_wisp",
            RegistryHelpers.registerMobInContainer(ModEntities.WISP, SoundEvents.BOTTLE_EMPTY, Items.GLASS_BOTTLE));

    public static final Item GRIMSTEEL_HEART = registerItem("grimsteel_heart",
            new GrimsteelHeartItem(new FabricItemSettings().durability(10)));

    public static final Item SCULK_SAC = registerItem("sculk_sac",
            new SculkSacItem(new FabricItemSettings()));

    public static final Item FULL_SCULK_SAC = registerItem("full_sculk_sac",
            new SculkSacItem(new FabricItemSettings()));

    public static final Item GRIMSTEEL_DAGGER = registerItem("grimsteel_dagger",
            new GrimsteelDaggerItem(new FabricItemSettings().stacksTo(64)));

    public static final Item GRIMSTEEL_SICKLE = registerItem("grimsteel_sickle",
            new GrimSteelSickleItem(ModTiers.GRIMSTEEL,1, -2.2f, new FabricItemSettings()));

    public static final Item GHOST_SPAWN_EGG = registerItem("ghost_spawn_egg",
            new SpawnEggItem(ModEntities.GHOST, 8122868, 3483938, new FabricItemSettings()));

    public static final Item PRIME_SPIRIT_SPAWN_EGG = registerItem("prime_spirit_spawn_egg",
            new SpawnEggItem(ModEntities.PRIME_SPIRIT, 8122868, 8122864, new FabricItemSettings()));

	public static final Item WISP_SPAWN_EGG = registerItem("wisp_spawn_egg",
			new SpawnEggItem(ModEntities.WISP, 3483938, 8122868, new FabricItemSettings()));

	public static final Item BUCKET_OF_ECTOPLASM = registerItem("bucket_of_ectoplasm",
			new BucketOfEctoplasmItem(ModBlocks.ECTOPLASM_BLOCK, new FabricItemSettings().stacksTo(1)));


	// Registry for Items:

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Spookiar.MOD_ID, name), item);
    }

    public static void registerModItems() {
        System.out.println("Registering Items for " + Spookiar.MOD_ID);
    }
}
