package net.lunarluned.spookiar.registry.entities;

import com.mojang.authlib.GameProfile;
import net.lunarluned.spookiar.Spookiar;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GraveBlockEntity extends BlockEntity {
    private NonNullList<ItemStack> items;
    private GameProfile graveOwner;
    private String customName;

    public GraveBlockEntity(BlockPos pos, BlockState state) {
        super(Spookiar.GRAVE_BLOCK_ENTITY, pos, state);

        this.customName = "";
        this.graveOwner = null;
        this.items = NonNullList.withSize(41, ItemStack.EMPTY);
    }

    public void setItems(NonNullList<ItemStack> items) {
        this.items = items;
        this.setChanged();
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public void setGraveOwner(GameProfile gameProfile) {
        this.graveOwner = gameProfile;
        this.setChanged();
    }

    public GameProfile getGraveOwner() {
        return graveOwner;
    }

    public void setCustomName(String text) {
        this.customName = text;
        this.setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.items = NonNullList.withSize(tag.getInt("ItemCount"), ItemStack.EMPTY);

        ContainerHelper.loadAllItems(tag.getCompound("Items"), this.items);


        if(tag.contains("GraveOwner"))
            this.graveOwner = NbtUtils.readGameProfile(tag.getCompound("GraveOwner"));

        if(tag.contains("CustomName"))
            this.customName = tag.getString("CustomName");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putInt("ItemCount", this.items.size());

        tag.put("Items", ContainerHelper.saveAllItems(new CompoundTag(), this.items, true));

        if(graveOwner != null)
            tag.put("GraveOwner", NbtUtils.writeGameProfile(new CompoundTag(), graveOwner));
        if(customName != null && !customName.isEmpty())
            tag.putString("CustomName", customName);
    }
}