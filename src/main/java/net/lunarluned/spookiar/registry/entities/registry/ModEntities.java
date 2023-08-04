package net.lunarluned.spookiar.registry.entities.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.registry.entities.living_entities.ghost.Ghost;
import net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit.PrimeSpirit;
import net.lunarluned.spookiar.registry.entities.living_entities.wisp.Wisp;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    // Living Entities

    public static final EntityType<Ghost> GHOST = FabricEntityTypeBuilder.create(MobCategory.MONSTER, Ghost::new).dimensions(EntityDimensions.fixed(0.9f, 2f)).fireImmune().trackRangeChunks(10).build();

	public static final EntityType<PrimeSpirit> PRIME_SPIRIT = FabricEntityTypeBuilder.create(MobCategory.MONSTER, PrimeSpirit::new).dimensions(EntityDimensions.fixed(1.4f, 2.8f)).fireImmune().trackRangeChunks(10).build();

	public static final EntityType<Wisp> WISP = FabricEntityTypeBuilder.create(MobCategory.MONSTER, Wisp::new).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).fireImmune().trackRangeChunks(10).build();


	public static void registerModEntities() {

        Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Spookiar.MOD_ID, "ghost"), GHOST);

		Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Spookiar.MOD_ID, "prime_spirit"), PRIME_SPIRIT);

		Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Spookiar.MOD_ID, "wisp"), WISP);

        FabricDefaultAttributeRegistry.register(GHOST, Ghost.createGhostAttributes());

		FabricDefaultAttributeRegistry.register(PRIME_SPIRIT, PrimeSpirit.createPrimeGhostAttributes());

		FabricDefaultAttributeRegistry.register(WISP, Wisp.createWispAttributes());

        System.out.println("Registering Entities for " + Spookiar.MOD_ID);
    }
}
