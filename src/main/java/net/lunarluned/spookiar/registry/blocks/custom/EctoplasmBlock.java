package net.lunarluned.spookiar.registry.blocks.custom;

import net.lunarluned.spookiar.registry.items.ModItems;
import net.lunarluned.spookiar.util.ModMobTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class EctoplasmBlock extends SoulSandBlock {
	protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);

	public EctoplasmBlock(Properties properties) {
		super(properties);
	}

	@SuppressWarnings("all")
	@Override
	public InteractionResult use(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {

		ItemStack itemStack = player.getItemInHand(hand);

		if (itemStack.is(Items.BUCKET)) {

			itemStack.shrink(1);
			setToAir(state, world, pos);
			world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);

			if (itemStack.isEmpty()) {

				player.setItemInHand(hand, new ItemStack(ModItems.BUCKET_OF_ECTOPLASM));
			} else if (!player.getInventory().add(new ItemStack(ModItems.BUCKET_OF_ECTOPLASM))) {
				player.drop(new ItemStack(ModItems.BUCKET_OF_ECTOPLASM), false);
			}
		}
		return super.use(state, world, pos, player, hand, hit);
	}
/*
	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return SHAPE;
	}

 */
	@Override
	public void entityInside(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Entity entity) {
		super.entityInside(state, world, pos, entity);
		if (entity instanceof LivingEntity livingEntity && entity.getType() != EntityType.WITHER && ((LivingEntity) entity).getMobType() != ModMobTypes.GHOUL) {
			livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200));
			entity.makeStuckInBlock(state, new Vec3(0.8999999761581421D, 1.5D, 0.8999999761581421D));
		}
	}
	private static void setToAir(BlockState state, Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, pushEntitiesUp(state, Blocks.AIR.defaultBlockState(), world, pos));
	}
}
