package net.lunarluned.spookiar.registry.items.custom;

import net.lunarluned.spookiar.registry.effects.ModEffects;
import net.lunarluned.spookiar.registry.items.ModItems;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class GrimsteelHeartItem extends Item {

    private static final String TAG_OPENED = "Opened";

    public GrimsteelHeartItem(Properties properties) {
        super(properties);
    }

    public static boolean isOpened(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        return compoundTag != null && compoundTag.getBoolean(TAG_OPENED);
    }


    public static void setOpened(ItemStack itemStack, boolean bl) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putBoolean(TAG_OPENED, bl);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player user, @NotNull InteractionHand hand) {

        ItemStack itemStack = user.getItemInHand(hand);
        if (!level.isClientSide) {

        if (!isOpened(itemStack)) {

            level.playSound(null, user.getX(), user.getY(), user.getZ(), ModSoundEvents.BLOCK_GRIMSTEEL_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);
            setOpened(itemStack, true);
        }

            if (isOpened(itemStack)) {

                level.playSound(null, user.getX(), user.getY(), user.getZ(), ModSoundEvents.BLOCK_GRIMSTEEL_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);
                user.addEffect(new MobEffectInstance(ModEffects.PERSISTENCE, 200, 0));
                user.getItemInHand(hand).hurtAndBreak(1, user, player -> player.broadcastBreakEvent(hand));
                setOpened(itemStack, false);
                return InteractionResultHolder.consume(itemStack);

            }

        }
        return InteractionResultHolder.sidedSuccess(itemStack, !level.isClientSide());
    }
}
