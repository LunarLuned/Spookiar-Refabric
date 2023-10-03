package net.lunarluned.spookiar.registry.items.custom;

import net.lunarluned.spookiar.misc.ModDamageSources;
import net.lunarluned.spookiar.registry.effects.ModEffects;
import net.lunarluned.spookiar.registry.entities.projectiles.GrimsteelDaggerProjectile;
import net.lunarluned.spookiar.registry.entities.registry.ModEntities;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class GrimsteelDaggerItem extends Item {
    public GrimsteelDaggerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player user, @NotNull InteractionHand hand) {

        ItemStack itemStack = user.getItemInHand(hand);
        if (!level.isClientSide) {

            user.gameEvent(GameEvent.PROJECTILE_SHOOT, user);
            GrimsteelDaggerProjectile grimsteelDaggerProjectile;

            grimsteelDaggerProjectile = ModEntities.GRIMSTEEL_DAGGER.create(level);


            grimsteelDaggerProjectile.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.0F, 1.0F);
            grimsteelDaggerProjectile.setPos(user.getX(), user.getEyeY() - 0.10000000149011612D, user.getZ());
            grimsteelDaggerProjectile.setOwner(user);

            level.addFreshEntity(grimsteelDaggerProjectile);
            if (!user.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        return InteractionResultHolder.sidedSuccess(itemStack, !level.isClientSide());
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity livingEntity, LivingEntity livingEntity2) {
        livingEntity.hurt(livingEntity.level().damageSources().source(ModDamageSources.GRIM, livingEntity, livingEntity), 4.0F);
        return true;
    }
}
