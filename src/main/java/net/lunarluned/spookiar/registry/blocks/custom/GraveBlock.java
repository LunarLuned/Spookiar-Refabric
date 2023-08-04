package net.lunarluned.spookiar.registry.blocks.custom;

import net.lunarluned.spookiar.registry.entities.GraveBlockEntity;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GraveBlock extends HorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
    private static final VoxelShape SHAPE = Shapes.or(Block.box(6.0D, 0.0D, 2.0D, 10.0D, 14.0D, 14.0D));
	private static final DirectionProperty FACING;
	private static final BooleanProperty WATERLOGGED;

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    public GraveBlock(Properties properties) {
        super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
	}

	// Gets the rotation block states for the block

	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getClockWise());
	}

	public BlockState updateShape(BlockState blockState, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
		if (blockState.getValue(WATERLOGGED)) {
			levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
		}
		return super.updateShape(blockState, direction, neighborState, levelAccessor, pos, neighborPos);
	}

	@SuppressWarnings("ALL")
	public FluidState getFluidState(BlockState blockState) {
		return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
	}

	@SuppressWarnings("ALL")
	public BlockState rotate(BlockState blockState, Rotation rotation) {
		return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
	}


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            RetrieveGrave(player, level, pos);
        player.playSound(ModSoundEvents.BLOCK_GRAVESTONE_COLLECT, 1, 0.6F + player.getRandom().nextFloat() * 0.4F);
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
            if(RetrieveGrave(player, level, pos))
        dropAllGrave(level, pos);
        player.playSound(ModSoundEvents.BLOCK_GRAVESTONE_COLLECT, 1, 0.6F + player.getRandom().nextFloat() * 0.4F);
        super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GraveBlockEntity(pos, state);
    }

    public void dropAllGrave(Level level, BlockPos pos) {
        if(level.isClientSide) return;

        BlockEntity bl = level.getBlockEntity(pos);

        if(!(bl instanceof GraveBlockEntity)) return;
        GraveBlockEntity blockEntity = (GraveBlockEntity) bl;

        blockEntity.setChanged();

        if(blockEntity.getItems() == null) return;

        Containers.dropContents(level, pos, blockEntity.getItems());

        blockEntity.setItems(NonNullList.of(ItemStack.EMPTY));
    }

    public boolean RetrieveGrave(Player player, Level level, BlockPos pos) {
        if(level.isClientSide) return false;

        BlockEntity bl = level.getBlockEntity(pos);

        if(!(bl instanceof GraveBlockEntity)) return false;
        GraveBlockEntity blockEntity = (GraveBlockEntity) bl;

        blockEntity.setChanged();

        if(blockEntity.getItems() == null) return false;
        if(blockEntity.getGraveOwner() == null) return false;

        NonNullList<ItemStack> items = blockEntity.getItems();

        NonNullList<ItemStack> retrievalInventory = NonNullList.create();

        retrievalInventory.addAll(player.getInventory().items);
        retrievalInventory.addAll(player.getInventory().armor);
        retrievalInventory.addAll(player.getInventory().offhand);

        player.getInventory().clearContent();
            List<ItemStack> armor = items.subList(36, 40);

            for (ItemStack itemStack : armor) {
                EquipmentSlot equipmentSlot = Mob.getEquipmentSlotForItem(itemStack);

                player.setItemSlot(equipmentSlot, itemStack);
            }

        player.setItemSlot(EquipmentSlot.OFFHAND, items.get(40));

            List<ItemStack> mainInventory = items.subList(0, 36);

            for (int i = 0; i < mainInventory.size(); i++) {
                player.getInventory().add(i, mainInventory.get(i));
            }

            NonNullList<ItemStack> extraItems = NonNullList.create();

            List<Integer> openArmorSlots = getInventoryOpenSlots(player.getInventory().armor);

            for(int i = 0; i < 4; i++) {
                if(openArmorSlots.contains(i)) {
                    player.setItemSlot(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, i), retrievalInventory.subList(36, 40).get(i));
                }
                else
                    extraItems.add(retrievalInventory.subList(36, 40).get(i));
            }

            if(player.getInventory().offhand.get(0) == ItemStack.EMPTY)
                player.setItemSlot(EquipmentSlot.OFFHAND, retrievalInventory.get(40));
            else
                extraItems.add(retrievalInventory.get(40));

            extraItems.addAll(retrievalInventory.subList(0, 36));
            if (retrievalInventory.size() > 41)
                extraItems.addAll(retrievalInventory.subList(41, retrievalInventory.size()));

            List<Integer> openSlots = getInventoryOpenSlots(player.getInventory().items);

            for(int i = 0; i < openSlots.size(); i++) {
                player.getInventory().add(openSlots.get(i), extraItems.get(i));
            }

            NonNullList<ItemStack> dropItems = NonNullList.create();
            dropItems.addAll(extraItems.subList(openSlots.size(), extraItems.size()));

            Containers.dropContents(level, pos, dropItems);


        level.removeBlock(pos, false);
        return true;
    }

    private List<Integer> getInventoryOpenSlots(NonNullList<ItemStack> inventory) {
        List<Integer> openSlots = new ArrayList<>();

        for (int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i) == ItemStack.EMPTY)
                openSlots.add(i);
        }

        return openSlots;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if(!(blockEntity instanceof GraveBlockEntity) || !itemStack.hasCustomHoverName()) {
            super.setPlacedBy(world, pos, state, placer, itemStack);
            return;
        }

        GraveBlockEntity gravestoneBlockEntity = (GraveBlockEntity) blockEntity;

        gravestoneBlockEntity.setCustomName(itemStack.getOrCreateTagElement("display").getString("Name"));
    }

	static {
		FACING = HorizontalDirectionalBlock.FACING;
		WATERLOGGED = BlockStateProperties.WATERLOGGED;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}
}
