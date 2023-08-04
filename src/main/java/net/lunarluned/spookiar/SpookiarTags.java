package net.lunarluned.spookiar;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("ALL")
public class SpookiarTags {

    // Block Tags

    public static final TagKey<Block> GHOST_SPAWNABLE_ON = TagKey.create(Registries.BLOCK, new ResourceLocation("spookiar", "ghost_spawnable_on"));

	public static final TagKey<Block> GHOST_REPELLENTS = TagKey.create(Registries.BLOCK, new ResourceLocation("spookiar", "ghost_repellents"));

	// Entity Tags
    public static final TagKey<EntityType<?>> GHOST_HOSTILE_TO = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("spookiar", "ghost_hostile_to"));

	public static final TagKey<EntityType<?>> GHOST_AVOIDS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("spookiar", "ghost_avoids"));

	// Item Tags

    public static final TagKey<Item> CLOAKS = TagKey.create(Registries.ITEM, new ResourceLocation("spookiar", "cloaks"));

	public static final TagKey<Item> DUAL_WIELDING_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation("spookiar", "dual_wielding_items"));


}
