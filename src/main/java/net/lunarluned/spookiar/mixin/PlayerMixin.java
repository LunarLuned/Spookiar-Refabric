package net.lunarluned.spookiar.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.registry.items.ModItems;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow
    @Final
    private Inventory inventory;

    @Shadow public abstract Inventory getInventory();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(method = "dropEquipment", at = @At(value = "INVOKE", target = "net.minecraft.world.entity.player.Inventory.dropAll()V"))
    private void spookiar_dropEquipment(Inventory inventory) {
        ItemStack gravestone = new ItemStack(ModItems.GRAVESTONE_OF_STASHING);
        if (Spookiar.getConfig().features.featuresConfig.featuresChanges.gravestone_of_stashing_activated) {
            if (!this.getInventory().contains(gravestone)) {
                this.inventory.dropAll();
                return;
            }
            int slot = this.getInventory().findSlotMatchingItem(new ItemStack(ModItems.GRAVESTONE_OF_STASHING));
            this.getInventory().removeItem(slot, 1);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSoundEvents.BLOCK_GRAVESTONE_LAY, this.getSoundSource(), 1.0F, 1.0F);
            Spookiar.placeGrave((ServerLevel) this.level(), this.position(), this.inventory.player);
        } else {
            this.inventory.dropAll();
        }
    }
}
