package net.lunarluned.spookiar.registry.entities.registry;

import com.mojang.serialization.Codec;
import net.lunarluned.spookiar.Spookiar;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.sydokiddo.chrysalis.misc.util.CoreRegistry;
import net.sydokiddo.chrysalis.mixin.util.MemoryModuleAccessor;

import java.util.Optional;

public class ModMemoryModules {

	public static final net.sydokiddo.chrysalis.misc.util.CoreRegistry<MemoryModuleType<?>> MEMORY_MODULES = CoreRegistry.create(Registries.MEMORY_MODULE_TYPE, Spookiar.MOD_ID);

	// Ghost Memory Modules

	public static final MemoryModuleType<Player> NEAREST_TARGETABLE_PLAYER_NOT_WEARING_CLOAK = register("nearest_targetable_player_not_wearing_cloak");

	public static final MemoryModuleType<Integer> GHOST_SPAWNING_WISP_COOLDOWN = register("ghost_spawning_wisp_cooldown", Codec.INT);

	public static final MemoryModuleType<Integer> PRIME_SPIRIT_STOMPING_COOLDOWN = register("prime_spirit_stomping_cooldown", Codec.INT);

	public static final MemoryModuleType<Integer> WEAKENED_NEARBY_ENEMIES_COOLDOWN = register("weakened_nearby_enemies_cooldown", Codec.INT);

	public static final MemoryModuleType<Integer> GHOST_SPAWNING_GHOST_COOLDOWN = register("ghost_spawning_ghost_cooldown", Codec.INT);

	public static final MemoryModuleType<Integer> PRIME_SPIRIT_JUDGEMENT_COOLDOWN = register("prime_spirit_judgement_cooldown", Codec.INT);

	public static final MemoryModuleType<Integer> PRIME_SPIRIT_ABSORB_COOLDOWN = register("prime_spirit_absorb_cooldown", Codec.INT);

	public static final MemoryModuleType<Integer> PRIME_SPIRIT_JUDGEMENT_DELAY = register("prime_spirit_judgement_delay", Codec.INT);


	// - Misc Memory Modules



	public static <U> MemoryModuleType<U> register(String id, Codec<U> codec) {
		return MEMORY_MODULES.register(id, net.sydokiddo.chrysalis.mixin.util.MemoryModuleAccessor.createMemoryModuleType(Optional.of(codec)));
	}

	public static <U> MemoryModuleType<U> register(String id) {
		return MEMORY_MODULES.register(id, MemoryModuleAccessor.createMemoryModuleType(Optional.empty()));
	}
}
