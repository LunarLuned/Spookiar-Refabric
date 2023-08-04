package net.lunarluned.spookiar.registry.items.custom;

import net.lunarluned.spookiar.registry.items.ModItems;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SculkSacItem extends Item {

    public SculkSacItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player user, @NotNull InteractionHand hand) {

        ItemStack itemStack = user.getItemInHand(hand);
        if (!level.isClientSide) {
            if (itemStack.is(ModItems.SCULK_SAC)) {


                if (user.experienceLevel > 0) {
                    user.giveExperiencePoints(-7);
                    user.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                    itemStack.shrink(1);
                    user.addItem(new ItemStack(ModItems.FULL_SCULK_SAC, 1));
                    level.playSound(null, user.getOnPos(), ModSoundEvents.SCULK_SAC_FILL, SoundSource.NEUTRAL, 1.0f, 0.8f + level.random.nextFloat() * 0.4F);
                }
            }
            if (itemStack.is(ModItems.FULL_SCULK_SAC)) {
                int i = 3 + level.random.nextInt(5) + level.random.nextInt(5);
                ExperienceOrb.award((ServerLevel) level, Vec3.atCenterOf(user.getOnPos()), i);
                itemStack.shrink(1);
                user.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                level.playSound(null, user.getOnPos(), ModSoundEvents.SCULK_SAC_EMPTY, SoundSource.NEUTRAL, 1.0f, 0.8f + level.random.nextFloat() * 0.4F);
            }
        }
        return InteractionResultHolder.sidedSuccess(itemStack, !level.isClientSide());
    }
}

