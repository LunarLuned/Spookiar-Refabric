package net.lunarluned.spookiar.registry.blocks.custom;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.registry.blocks.ModBlocks;
import net.lunarluned.spookiar.registry.entities.GraveBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static BlockEntityType<GraveBlockEntity> GRAVESTONE = FabricBlockEntityTypeBuilder.create(GraveBlockEntity::new, ModBlocks.GRAVESTONE).build(null);

    public static void registerBlockEntities() {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Spookiar.MOD_ID, "gravestone"), GRAVESTONE);
    }
}
