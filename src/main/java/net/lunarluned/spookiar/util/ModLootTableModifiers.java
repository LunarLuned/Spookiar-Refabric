package net.lunarluned.spookiar.util;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.lunarluned.spookiar.registry.blocks.ModBlocks;
import net.lunarluned.spookiar.registry.items.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetInstrumentFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;


public class ModLootTableModifiers {
    private static final ResourceLocation END_CITY_STRUCTURE_CHEST_ID
            = new ResourceLocation("minecraft", "chests/end_city_treasure");
    private static final ResourceLocation SPAWN_BONUS_CHEST_CHEST_ID
            = new ResourceLocation("minecraft", "chests/spawn_bonus_chest");
    private static final ResourceLocation ANCIENT_CITY_ICE_BOX_STRUCTURE_CHEST_ID
            = new ResourceLocation("minecraft", "chests/ancient_city_ice_box");
    private static final ResourceLocation ANCIENT_CITY_STRUCTURE_CHEST_ID
            = new ResourceLocation("minecraft", "chests/ancient_city");
    private static final ResourceLocation DESERT_PYRAMID_STRUCTURE_CHEST_ID
            = new ResourceLocation("minecraft", "chests/desert_pyramid");
    private static final ResourceLocation BASTION_OTHER_STRUCTURE_CHEST_ID
            = new ResourceLocation("minecraft", "chests/bastion_other");
    private static final ResourceLocation BASTION_TREASURE_STRUCTURE_CHEST_ID
            = new ResourceLocation("minecraft", "chests/bastion_treasure");
    private static final ResourceLocation BASTION_HOGLIN_STABLE_STRUCTURE_CHEST_ID
            = new ResourceLocation("minecraft", "chests/bastion_hoglin_stable");
    private static final ResourceLocation STRONGHOLD_CORRIDOR_STRUCTURE_CHEST_ID
            = new ResourceLocation("minecraft", "chests/stronghold_corridor");
    private static final ResourceLocation STRONGHOLD_CROSSING_STRUCTURE_CHEST_ID
            = new ResourceLocation("minecraft", "chests/stronghold_crossing");

    //bosses
    private static final ResourceLocation WARDEN_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/warden");
    private static final ResourceLocation WITHER_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/wither");
    private static final ResourceLocation ENDER_DRAGON_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/ender_dragon");

    //entities
    private static final ResourceLocation ZOMBIE_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/zombie");
    private static final ResourceLocation SPIDER_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/spider");
    private static final ResourceLocation CAVE_SPIDER_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/cave_spider");
    private static final ResourceLocation ENDERMAN_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/enderman");
    private static final ResourceLocation ENDERMITE_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/endermite");
    private static final ResourceLocation ZOMBIFIED_PIGLIN_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/zombified_piglin");
    private static final ResourceLocation DROWNED_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/drowned");
    private static final ResourceLocation HUSK_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/husk");
    private static final ResourceLocation WITHER_SKELETON_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/wither_skeleton");
    private static final ResourceLocation SKELETON_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/skeleton");
    private static final ResourceLocation STRAY_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/stray");
    private static final ResourceLocation WITCH_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/witch");
    private static final ResourceLocation CREEPER_ENTITY_ID
            = new ResourceLocation("minecraft", "entities/creeper");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, table, setter) -> {
            //structures
            if(ANCIENT_CITY_ICE_BOX_STRUCTURE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1));
                LootPool.Builder builder = LootPool.lootPool();
                builder.when(LootItemRandomChanceCondition.randomChance(0.45f)); //45% chance
                builder.setRolls(UniformGenerator.between(23, 50)); //  23 to 50
                builder.add(LootItem.lootTableItem(ModItems.SCULK_SAC));
                table.withPool(builder);
            }
            if(ANCIENT_CITY_ICE_BOX_STRUCTURE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1));
                LootPool.Builder builder = LootPool.lootPool();
                builder.when(LootItemRandomChanceCondition.randomChance(0.30f)); //30% chance
                builder.setRolls(UniformGenerator.between(2, 10)); //  2 to 10
                builder.add(LootItem.lootTableItem(ModItems.FULL_SCULK_SAC));
                table.withPool(builder);
            }
            if(ANCIENT_CITY_STRUCTURE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1));
                LootPool.Builder builder = LootPool.lootPool();
                builder.when(LootItemRandomChanceCondition.randomChance(0.1f)); //10% chance
                builder.setRolls(UniformGenerator.between(1, 8)); //  1 to 8
                builder.add(LootItem.lootTableItem(ModItems.SCULK_SAC));
                table.withPool(builder);
            }
            if(ANCIENT_CITY_STRUCTURE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1));
                LootPool.Builder builder = LootPool.lootPool();
                builder.when(LootItemRandomChanceCondition.randomChance(0.10f)); //10% chance
                builder.setRolls(UniformGenerator.between(1, 3)); //  1 to 3
                builder.add(LootItem.lootTableItem(ModItems.GRIMSTEEL_SCRAPS));
                table.withPool(builder);
            }
            if(ANCIENT_CITY_STRUCTURE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1));
                LootPool.Builder builder = LootPool.lootPool();
                builder.when(LootItemRandomChanceCondition.randomChance(0.50f)); //50% chance
                builder.setRolls(UniformGenerator.between(2, 12)); //  1 to 12
                builder.add(LootItem.lootTableItem(ModBlocks.SCULK_SPINE));
                table.withPool(builder);
            }
        });
    }
}