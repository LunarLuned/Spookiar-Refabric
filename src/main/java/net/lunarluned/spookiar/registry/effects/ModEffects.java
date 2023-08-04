package net.lunarluned.spookiar.registry.effects;

import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.registry.effects.potion_effects.PersistenceEffect;
import net.lunarluned.spookiar.registry.effects.potion_effects.StagnatedEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModEffects {
	public static MobEffect STAGNATED;
	public static MobEffect PERSISTENCE;

	public static MobEffect registerStagnatedStatusEffect(String name) {
		return Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(Spookiar.MOD_ID, name), new StagnatedEffect(MobEffectCategory.HARMFUL, 	15766579));
	}
	public static MobEffect registerPersistenceStatusEffect(String name) {
		return Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(Spookiar.MOD_ID, name), new PersistenceEffect(MobEffectCategory.HARMFUL, 8058623));
	}

	public static void registerEffects() {
		STAGNATED = registerStagnatedStatusEffect("stagnated");
		PERSISTENCE = registerPersistenceStatusEffect("persistence");
	}

}
