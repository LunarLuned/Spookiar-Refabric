package net.lunarluned.spookiar.registry.enchantments;

import net.lunarluned.spookiar.Spookiar;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ModEnchantments {
	public static Enchantment TIPPING = register("tipping",
			new TippingEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON));


	private static Enchantment register(String name, Enchantment enchantment) {
		return Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Spookiar.MOD_ID, name), enchantment);
	}

	public static void registerModEnchantments() {
		System.out.println("Registering enchantments for " + Spookiar.MOD_ID);
	}
}
