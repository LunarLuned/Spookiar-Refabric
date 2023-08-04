package net.lunarluned.spookiar.registry.entities.living_entities.ghost;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.lunarluned.spookiar.SpookiarTags;
import net.lunarluned.spookiar.registry.effects.ModEffects;
import net.lunarluned.spookiar.registry.entities.registry.ModMemoryModules;
import net.lunarluned.spookiar.registry.entities.registry.ModSensorTypes;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Ghost extends Monster {

	private static final EntityDataAccessor<Boolean> ANGERED = SynchedEntityData.defineId(Ghost.class, EntityDataSerializers.BOOLEAN);

	private static final ImmutableList<SensorType<? extends Sensor<? super Ghost>>> SENSOR_TYPES =
			ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, ModSensorTypes.GHOST_SPECIFIC_SENSOR);

	protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of
			(MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, ModMemoryModules.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_CLOAK, MemoryModuleType.NEAREST_LIVING_ENTITIES,
					MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
					MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
					MemoryModuleType.NEAREST_REPELLENT, ModMemoryModules.GHOST_SPAWNING_WISP_COOLDOWN, ModMemoryModules.GHOST_SPAWNING_GHOST_COOLDOWN, ModMemoryModules.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_CLOAK, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.PACIFIED, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.IS_PREGNANT);


	public Ghost(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);

		this.xpReward = 10;
		this.setMaxUpStep(1);
		this.getNavigation().setCanFloat(true);

		this.setPathfindingMalus(BlockPathTypes.WATER, -1.0f);
		this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0f);
		this.setPathfindingMalus(BlockPathTypes.LAVA, -1.0f);
		this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0f);
		this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0f);
		this.setPathfindingMalus(BlockPathTypes.TRAPDOOR, -1.0f);
		this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, -1.0f);
		this.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, -1.0f);
		this.setPathfindingMalus(BlockPathTypes.DANGER_OTHER, -1.0f);
		this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, -1.0f);
	}

	// Animation States

	public final AnimationState idleAnimationState = new AnimationState();
	public final AnimationState attackAnimationState = new AnimationState();
	public final AnimationState walkAnimationState = new AnimationState();
	public final AnimationState fallAnimationState = new AnimationState();
	public final AnimationState hurtAnimationState = new AnimationState();

	@Override
	protected Brain.Provider<Ghost> brainProvider() {
		return Brain.provider(MEMORY_MODULES, SENSOR_TYPES);
	}

	@Override
	protected Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
		return GhostBrain.makeBrain(this, this.brainProvider().makeBrain(dynamic));
	}

	@Override
	@SuppressWarnings("all")
	public Brain<Ghost> getBrain() {
		return (Brain<Ghost>) super.getBrain();
	}


	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
		RandomSource random = serverLevelAccessor.getRandom();
		GhostBrain.addMemories(this, random);
		return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
	}

	// Attributes

	public static AttributeSupplier.Builder createGhostAttributes() {
		return createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.ARMOR, 5).add(Attributes.MOVEMENT_SPEED, 0.25f).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.FOLLOW_RANGE, 3.0);
	}

	@Override
	protected void customServerAiStep() {
		this.level().getProfiler().push("ghostBrain");
		this.getBrain().tick((ServerLevel) this.level(), this);
		this.level().getProfiler().pop();
		this.level().getProfiler().push("ghostActivityUpdate");
		GhostBrain.updateActivity(this);
		this.level().getProfiler().pop();

		super.customServerAiStep();
	}

	public boolean doHurtTarget(Entity entity) {

		level().broadcastEntityEvent(this, (byte)62); // Attack Animation State Entity Event

		// If the Ghost is angered, it will heal itself upon attacking

		if ((this.getHealth() < this.getMaxHealth() / 2) || this.isAngered()) {
			if (this.getRandom().nextInt(10) <= 10) {
				this.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 0, false, false, false));
			}
		}

		// Ghosts add the Cursed Effect to their target upon attacking

		assert entity instanceof LivingEntity;
		// Replace with new negative effect
		//((LivingEntity) entity).addEffect(new MobEffectInstance(ModEffects.CURSED, 80, 0, false, true, true));

		this.playSound(ModSoundEvents.ENTITY_GHOST_JUMPSCARE, 1.0F, 1.0F);

		return super.doHurtTarget(entity);
	}

	public static boolean checkGhostSpawnRules(ServerLevelAccessor serverLevelAccessor, BlockPos blockPos) {
		return serverLevelAccessor.getBlockState(blockPos.below()).is(SpookiarTags.GHOST_SPAWNABLE_ON);
	}

	private boolean isMoving() {
		return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
	}

	@Override
	public void handleEntityEvent(byte b) {

		// Attack Animation State Entity Event

		if (b == 62) {
			this.attackAnimationState.start(this.tickCount);
		}
		else {
			super.handleEntityEvent(b);
		}
	}

	@Override
	public void tick() {

		// Plays the falling animation and gives the Ghost Slow Falling if falling while not angered

		if (!this.isAngered()) {
			if (!this.onGround()) {
				this.attackAnimationState.stop();
				this.walkAnimationState.stop();
				this.idleAnimationState.stop();
				this.fallAnimationState.startIfStopped(this.tickCount);
				this.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 30, 1, false, false, false));
			} else {
				this.fallAnimationState.stop();
			}
		}

		// Adds a movement and damage increase, fire res and spawns Soul particles when the Ghost is angered

		if (this.isAngered()) {
			this.spawnSoulSpeedParticle();
			this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 30, 0, false, false, false));
			this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30, 1, false, false, false));
			this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 30, 1, false, false, false));
		}

		// If the Ghost is at half health, it will become empowered and recieve the Determined effect

		if (this.getHealth() < this.getMaxHealth() / 2) {
			if (this.getRandom().nextInt(1000) >= 1000) {
				this.playSound(ModSoundEvents.ENTITY_GHOST_EMPOWERED, 1.0F, 1.0F);
			}
			this.addEffect(new MobEffectInstance(ModEffects.STAGNATED, 30, 0, false, false, false));
		}

		// Plays the animations upon ticking

		if (this.isMoving()) {
			this.idleAnimationState.stop();
			this.walkAnimationState.startIfStopped(this.tickCount);
		} else {
			this.walkAnimationState.stop();
			this.idleAnimationState.startIfStopped(this.tickCount);
		}

		super.tick();
	}

	// Ghost Sounds

	@Override
	@Nullable
	protected SoundEvent getAmbientSound() {
		return ModSoundEvents.ENTITY_GHOST_MOAN;
	}

	@Override
	@Nullable
	protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
		return ModSoundEvents.ENTITY_GHOST_HURT;
	}

	@Override
	@Nullable
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.ENTITY_GHOST_DEATH;
	}

	public boolean hurt(@NotNull DamageSource damageSource, float f) {
		Entity entity;
		if (!this.isAngered()) {
			entity = damageSource.getDirectEntity();
			if (entity instanceof SpectralArrow) {
				return super.hurt(damageSource, f * 1.35f);
			} else if (entity instanceof AbstractArrow) {
				return false;
			}
			if (entity instanceof Player) {
				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, (LivingEntity) entity) > 0) {
					// Subtract additional Sharpness damage
					return super.hurt(damageSource, f -= EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, (LivingEntity) entity) * 1.25F);
				}
			}
		}
		return super.hurt(damageSource, f);
	}

	protected boolean isSunSensitive() {
		return true;
	}

	public void aiStep() {

		// If the Ghost is exposed to sunlight, it will become angered

		if (this.isAlive()) {
			boolean bl = this.isSunSensitive() && this.isSunBurnTick() && !this.isAngered();
			if (bl) {
				this.setAngered(true);
			}
			if (this.isOnFire() && !this.isAngered()) {
				this.setAngered(true);
			}
			updateAngered(level());
		}

		// The Ghost emits ambient Soul Fire Flame particles

		if (this.level().isClientSide) {
			for(int i = 0; i < 2; ++i) {
				if (this.tickCount % 5 == 0) {
					Vec3 vec3 = this.getDeltaMovement();
					this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX() + (this.random.nextDouble() - 0.5) * (double) this.getBbWidth(), this.getY() + 0.4, this.getZ() + (this.random.nextDouble() - 0.5) * (double) this.getBbWidth(), vec3.x * -0.2, 0.1, vec3.z * -0.2);
				}
			}
		}

		super.aiStep();
	}



	@Override
	public boolean canBeLeashed(@NotNull Player player) {
		return false;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(ANGERED, false);
		super.defineSynchedData();
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putBoolean("IsAngered", this.isAngered());
	}

	private void updateAngered(Level level) {
		if (!level.isClientSide()) {
			this.getTarget();
		}
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		this.setAngered(nbt.getBoolean("IsAngered"));
	}


	public boolean isAngered() {
		return this.entityData.get(ANGERED);
	}
	public void setAngered(boolean isAngered) {
		this.entityData.set(ANGERED, isAngered);
		this.playSound(ModSoundEvents.ENTITY_GHOST_EMPOWERED, 1.0F, 1.0F);
	}


	@Override
	public int getHeadRotSpeed() {
		return 10;
	}

	@Override
	public int getMaxHeadYRot() {
		return 20;
	}

	public boolean isPreventingPlayerRest(@NotNull Player player) {
		return true;
	}

	// Ghosts despawn when in Peaceful difficulty

	protected boolean shouldDespawnInPeaceful() {
		return true;
	}

	public boolean removeWhenFarAway(double distanceSquared) {
		return !this.isPersistenceRequired();
	}

}
