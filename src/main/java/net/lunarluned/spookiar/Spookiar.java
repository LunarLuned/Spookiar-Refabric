package net.lunarluned.spookiar;


import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.lunarluned.spookiar.config.ModConfig;
import net.lunarluned.spookiar.registry.ModRegistry;
import net.lunarluned.spookiar.registry.blocks.ModBlocks;
import net.lunarluned.spookiar.registry.blocks.custom.ModBlockEntities;
import net.lunarluned.spookiar.registry.entities.GraveBlockEntity;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Spookiar implements ModInitializer {
	private static final ModConfig CONFIG = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new).getConfig();
	public static BlockEntityType<GraveBlockEntity> GRAVE_BLOCK_ENTITY;

	public static final String MOD_ID = "spookiar";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModRegistry.registerAll();
		//ServerTickEvents.START_SERVER_TICK.register(this::serverTickEvent);
		GRAVE_BLOCK_ENTITY = ModBlockEntities.GRAVESTONE;
		LOGGER.info("Congratulations on being SPOOKED!!! grah ha ha!! >:3");
	}

	//private void serverTickEvent(MinecraftServer server) {

	//}

	// Mod Config

	public static ModConfig getConfig () {
		return CONFIG;
	}













	public static void placeGrave(ServerLevel level, Vec3 pos, Player player) {
		if (level.isClientSide) return;

		BlockPos blockPos = new BlockPos((int) pos.x, (int) (pos.y - 1), (int) pos.z);
		BlockPos blockPos2 = new BlockPos((int) pos.x, (int) (pos.y - 1), (int) pos.z);

		if(blockPos.getY() <= level.dimensionType().minY()) {
			blockPos = new BlockPos(blockPos.getX(), level.dimensionType().minY() + 2, blockPos.getZ());
		}

		if(blockPos2.getY() <= level.dimensionType().minY()) {
			blockPos2 = new BlockPos(blockPos2.getX(), level.dimensionType().minY() + 2, blockPos2.getZ());
		}

		BlockState blockState = level.getBlockState(blockPos);
		Block block = blockState.getBlock();


		NonNullList<ItemStack> combinedInventory = NonNullList.create();

		level.playSound(player, blockPos, ModSoundEvents.BLOCK_GRAVESTONE_LAY, SoundSource.BLOCKS);
		combinedInventory.addAll(player.getInventory().items);
		combinedInventory.addAll(player.getInventory().armor);
		combinedInventory.addAll(player.getInventory().offhand);


		boolean placed = false;

		for (BlockPos gravePos : BlockPos.withinManhattan(blockPos.offset(new Vec3i(0, 1, 0)), 5, 5, 5)) {
			BlockState state = level.getBlockState(blockPos2.below());
			if(canPlaceGravestone(level, block, gravePos)) {
				if (state.is(Blocks.AIR)) {
					level.setBlockAndUpdate(blockPos2, Blocks.COARSE_DIRT.defaultBlockState());
				}
				BlockState graveState = ModBlocks.GRAVESTONE.defaultBlockState();

				placed = level.setBlockAndUpdate(gravePos, graveState);
				GraveBlockEntity gravestoneBlockEntity = new GraveBlockEntity(gravePos, graveState);
				gravestoneBlockEntity.setItems(combinedInventory);
				gravestoneBlockEntity.setGraveOwner(player.getGameProfile());
				level.setBlockEntity(gravestoneBlockEntity);


				gravestoneBlockEntity.setChanged();
				block.playerWillDestroy(level, blockPos, blockState, player);
				System.out.println("Spookiar Gravestone spawned at: " + gravePos.getX() + ", " + gravePos.getY() + ", " + gravePos.getZ());

				break;
			}
		}

		if (!placed) {
			player.getInventory().dropAll();
		}
	}

	private static boolean canPlaceGravestone(ServerLevel level, Block block, BlockPos blockPos) {
		BlockEntity blockEntity = level.getBlockEntity(blockPos);



		if(blockEntity != null) return false;

		return !(blockPos.getY() < level.dimensionType().minY() || blockPos.getY() > level.dimensionType().height() - level.dimensionType().minY());
	}


}
