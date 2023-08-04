package net.lunarluned.spookiar.registry.enchantments;

import net.lunarluned.spookiar.registry.items.custom.GrimSteelSickleItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TippingEnchantment extends Enchantment {
	protected TippingEnchantment(Rarity weight, EnchantmentCategory type, EquipmentSlot... slotTypes) {
		super(weight, type, slotTypes);
	}

	public static List<MobEffectInstance> potionEffects = new ArrayList<>();

	@Override
	public void doPostAttack(@NotNull LivingEntity user, @NotNull Entity target, int level) {
		if (user instanceof Player) {
			potionEffects.addAll(user.getActiveEffects());
		}
		if (target != null) {
			applyPotionEffects((LivingEntity) target);
			potionEffects.clear();
		}
	}

		public boolean isTreasureOnly() {
		return false;
	}

	public boolean isTradeable() {
		return false;
	}

	public boolean isDiscoverable() {
		return false;
	}

	private void applyPotionEffects(LivingEntity entity) {
		for (MobEffectInstance effect : potionEffects) {
			entity.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier() + 1));

		}
	}

	public boolean canEnchant(ItemStack stack) {
		return stack.getItem() instanceof GrimSteelSickleItem;
	}

	@Override
	public boolean checkCompatibility(Enchantment enchantment) {
		return !(enchantment instanceof DamageEnchantment);
	}
}
