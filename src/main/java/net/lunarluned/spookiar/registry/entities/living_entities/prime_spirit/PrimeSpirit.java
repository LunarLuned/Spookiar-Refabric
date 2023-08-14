package net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.lunarluned.spookiar.registry.entities.living_entities.ghost.Ghost;
import net.lunarluned.spookiar.registry.entities.living_entities.wisp.Wisp;
import net.lunarluned.spookiar.registry.entities.registry.ModMemoryModules;
import net.lunarluned.spookiar.registry.entities.registry.ModSensorTypes;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.lunarluned.spookiar.sounds.boss_music.PrimeSpiritMusicEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
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
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class PrimeSpirit extends Monster {

	private static final int INVULNERABLE_TICKS = 220;
	private static final EntityDataAccessor<Integer> DATA_ID_INV;

	private final ServerBossEvent bossEvent;

	private static final EntityDataAccessor<Boolean> ANGERED = SynchedEntityData.defineId(PrimeSpirit.class, EntityDataSerializers.BOOLEAN);

	private static final ImmutableList<SensorType<? extends Sensor<? super PrimeSpirit>>> SENSOR_TYPES =
			ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, ModSensorTypes.GHOST_SPECIFIC_SENSOR);

	protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of
			(MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, ModMemoryModules.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_CLOAK, MemoryModuleType.NEAREST_LIVING_ENTITIES,
					MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
					MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
					MemoryModuleType.NEAREST_REPELLENT, ModMemoryModules.GHOST_SPAWNING_WISP_COOLDOWN, ModMemoryModules.PRIME_SPIRIT_STOMPING_COOLDOWN, ModMemoryModules.WEAKENED_NEARBY_ENEMIES_COOLDOWN, ModMemoryModules.GHOST_SPAWNING_GHOST_COOLDOWN, ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_COOLDOWN, ModMemoryModules.PRIME_SPIRIT_ABSORB_COOLDOWN, ModMemoryModules.PRIME_SPIRIT_JUDGEMENT_DELAY, ModMemoryModules.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_CLOAK, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.PACIFIED, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER);


	public PrimeSpirit(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
		this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_6).setPlayBossMusic(true)).setDarkenScreen(true);
		this.xpReward = 50;
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

	public final AnimationState bigPunchAnimationState = new AnimationState();
	public final AnimationState normal_attackAnimationState = new AnimationState();
	public final AnimationState walkAnimationState = new AnimationState();
	public final AnimationState runAnimationState = new AnimationState();
	public final AnimationState weakenAnimationState = new AnimationState();
	public final AnimationState summonAnimationState = new AnimationState();
	public final AnimationState deathAnimationState = new AnimationState();
	public final AnimationState deathPoseAnimationState = new AnimationState();

	@Override
	protected Brain.Provider<PrimeSpirit> brainProvider() {
		return Brain.provider(MEMORY_MODULES, SENSOR_TYPES);
	}

	@Override
	protected Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
		return PrimeSpiritBrain.makeBrain(this, this.brainProvider().makeBrain(dynamic));
	}


	@Override
	@SuppressWarnings("all")
	public Brain<PrimeSpirit> getBrain() {
		return (Brain<PrimeSpirit>) super.getBrain();
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
		RandomSource random = serverLevelAccessor.getRandom();

		Difficulty difficulty = this.level().getDifficulty();
		float maxHealth;
		float damageAmt;
		switch (difficulty) {
			case NORMAL -> maxHealth = 275;
			case HARD -> maxHealth = 400;
			default -> maxHealth = 150;
		}
		switch (difficulty) {
			case NORMAL -> damageAmt = 10;
			case HARD -> damageAmt = 12;
			default -> damageAmt = 8;
		}
		Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(maxHealth);
		Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(damageAmt);
		this.setHealth(this.getMaxHealth());
		this.setPersistenceRequired();
		PrimeSpiritBrain.addMemories(this, random);
		return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
	}


	// Attributes

	public static AttributeSupplier.Builder createPrimeGhostAttributes() {
		return createMobAttributes().add(Attributes.MAX_HEALTH, 150.0).add(Attributes.ARMOR, 10).add(Attributes.MOVEMENT_SPEED, 0.30f).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.FOLLOW_RANGE, 30);
	}

	public int getInvulnerableTicks() {
		return (Integer)this.entityData.get(DATA_ID_INV);
	}

	public void setInvulnerableTicks(int ticks) {
		this.entityData.set(DATA_ID_INV, ticks);
	}

	@Override
	protected void customServerAiStep() {
		this.level().getProfiler().push("primeGhostBrain");
		this.getBrain().tick((ServerLevel) this.level(), this);
		this.level().getProfiler().pop();
		this.level().getProfiler().push("primeGhostActivityUpdate");
		PrimeSpiritBrain.updateActivity(this);
		this.level().getProfiler().pop();
/*
		int i;
		if (this.getInvulnerableTicks() > 0) {
			i = this.getInvulnerableTicks() - 1;
			this.bossEvent.setProgress(1.0F - (float)i / 220.0F);
			if (i <= 0) {
				this.level().explode(this, this.getX(), this.getEyeY(), this.getZ(), 7.0F, false, Level.ExplosionInteraction.MOB);
				if (!this.isSilent()) {
					this.level().globalLevelEvent(1023, this.blockPosition(), 0);
				}
			}

			this.setInvulnerableTicks(i);
			if (this.tickCount % 10 == 0) {
				this.heal(10.0F);
			}
		}
*/
		this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());


		super.customServerAiStep();
	}

	public void makeInvulnerable() {
		this.setInvulnerableTicks(220);
		this.bossEvent.setProgress(0.0F);
		this.setHealth(this.getMaxHealth() / 3.0F);
	}


	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		if (this.invulnerableTime < 1) {
			PrimeSpiritMusicEvent.playBossMusic();
			this.bossEvent.addPlayer(player);
		}
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		// reactivate it when the time comes
		PrimeSpiritMusicEvent.stopBossMusic();
		this.bossEvent.removePlayer(player);
	}

	public boolean doHurtTarget(Entity entity) {

		level().broadcastEntityEvent(this, (byte)62); // Attack Animation State Entity Event

		// If the Ghost is angered, it will heal itself upon attacking

		if ((this.getHealth() < this.getMaxHealth() / 2) || this.isAngered()) {
			if (this.getRandom().nextInt(10) <= 10) {
				this.heal(2);
			}
		}

		this.playSound(ModSoundEvents.ENTITY_GHOST_JUMPSCARE, 1.0F, 1.0F);

		return super.doHurtTarget(entity);
	}


	private boolean isMoving() {
		return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
	}

	@Override
	public void handleEntityEvent(byte b) {

		// Attack Animation State Entity Event

		if (b == 62) {
			this.normal_attackAnimationState.start(this.tickCount);
		}
		if (b == 64) {
			this.bigPunchAnimationState.start(this.tickCount);
		}
		if (b == 68) {
			this.summonAnimationState.start(this.tickCount);
		}
		if (b == 70) {
			this.weakenAnimationState.start(this.tickCount);
		}
		if (b == 72) {
			this.weakenAnimationState.start(this.tickCount);
		}
		else {
			super.handleEntityEvent(b);
		}
	}

	@Override
	public boolean killedEntity(ServerLevel world, LivingEntity entity) {
		this.level().playSound(null, this.getOnPos(), ModSoundEvents.ENTITY_PRIME_SPIRIT_PATHETIC, SoundSource.HOSTILE, 5, 1);
		return true;
	}

	@Override
	public void tick() {

		if (this.isInWall()) {
				if (!level().isClientSide) {

					for(int i = 0; i < 16; ++i) {

						double g = this.getX() + (this.getRandom().nextDouble() - 0.5) * 16.0;
						double h = Mth.clamp(this.getY() + (double)(this.getRandom().nextInt(16) - 8), level().getMinBuildHeight(), level().getMinBuildHeight() + ((ServerLevel)level()).getLogicalHeight() - 1);
						double j = this.getZ() + (this.getRandom().nextDouble() - 0.5) * 16.0;

						Vec3 vec3 = this.position();

						if (this.randomTeleport(g, h, j, true)) {

							level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
							break;
						}
					}
				}
			}

		// Adds a movement increase , damage increase and spawns Soul particles when the Prime Spirit is angered

		if (this.isAngered()) {
			this.spawnSoulSpeedParticle();
			this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 30, 0, false, false, false));
			this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30, 1, false, false, false));
		}

		// If the Ghost is at half health, it will become empowered and recieve the Determined effect

		if (this.getHealth() < this.getMaxHealth() / 2) {
			if (this.getRandom().nextInt(1000) >= 1000) {
				this.level().playSound(null, this.getOnPos(), ModSoundEvents.ENTITY_PRIME_SPIRIT_WEAK, SoundSource.HOSTILE, 5, 1);
			}
			// Replace this with the new effect
			//this.addEffect(new MobEffectInstance(ModEffects.DETERMINED, 30, 3, false, false, false));
		}

		// Plays the animations upon ticking

		if (this.isMoving() && !this.isAngered()) {
			this.walkAnimationState.startIfStopped(this.tickCount);
		} else if (this.isMoving() && this.isAngered()) {
			this.runAnimationState.startIfStopped(this.tickCount);
		} else {
			this.runAnimationState.stop();
			this.walkAnimationState.stop();
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

	public boolean ignoreExplosion() {
		return true;
	}


	@Override
	public boolean hurt(@NotNull DamageSource damageSource, float f) {
		Entity entity;
			entity = damageSource.getDirectEntity();
			if ((entity instanceof SpectralArrow) && entity.closerThan(this, 8)) {
				return super.hurt(damageSource, f / 1.35f);
			}
			if ((entity instanceof SpectralArrow) && !entity.closerThan(this, 8)) {
			return false;
		}
			else if (entity instanceof AbstractArrow) {
				return false;
			}
			if (entity instanceof Player) {
				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, (LivingEntity) entity) > 0) {
					// Subtract additional Sharpness damage
					return super.hurt(damageSource, f -= EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, (LivingEntity) entity) * 1.25F);
				}
				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, (LivingEntity) entity) > 0) {
					return false;
				}
		}
		if (this.getHealth() < 5) {
			return super.hurt(damageSource, 1);
		}
		if (this.getHealth() < this.getMaxHealth() / 2) {
			return super.hurt(damageSource, f / 2);
		}
		/*if (this.getHealth() < 2) {
			this.makeInvulnerable();
			this.setHealth(1.0F);
			tickDeath();
		}
		 */
		return super.hurt(damageSource, f);
	}

	@Override
	protected void tickDeath() {



		++this.deathTime;
		if (this.deathTime >= 500 && this.deathTime <= 600) {
			float f = (this.random.nextFloat() - 0.5F) * 8.0F;
			float g = (this.random.nextFloat() - 0.5F) * 4.0F;
			float h = (this.random.nextFloat() - 0.5F) * 8.0F;
			this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + (double)f, this.getY() + 2.0 + (double)g, this.getZ() + (double)h, 0.0, 0.0, 0.0);
			this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX() + (double)f, this.getY() + 2.0 + (double)g, this.getZ() + (double)h, 0.0, 0.0, 0.0);
		}

		boolean bl = this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);

		if (this.level() instanceof ServerLevel) {
			if (this.deathTime > 450 && this.deathTime % 5 == 0 && bl) {
				ExperienceOrb.award((ServerLevel)this.level(), this.position(), Mth.floor( 0.08F));
			}
		}
		if (this.deathTime == 1) {
			BlockPos blockPos = this.getOnPos();
			int m;
			int lo;
			int k = blockPos.getX();
			int j = 64;

			// Scans the area for nearby players

			AABB aABB = new AABB(k, lo = blockPos.getY(), m = blockPos.getZ(), k + 1, lo + 1, m + 1).inflate(j).expandTowards(0.0, this.level().getHeight(), 0.0);
			List<Ghost> nearbyEntities = this.level().getEntitiesOfClass(Ghost.class, aABB);
			List<Wisp> nearbyEntities2 = this.level().getEntitiesOfClass(Wisp.class, aABB);


			for (LivingEntity livingEntity2 : nearbyEntities)  {
				livingEntity2.hurt(damageSources().starve(), 1000);
			}
			for (LivingEntity livingEntity3 : nearbyEntities2)  {
				livingEntity3.hurt(damageSources().starve(), 1000);
			}
			this.deathPoseAnimationState.start(this.tickCount);
			PrimeSpiritMusicEvent.stopBossMusic();
			this.level().playSound(null, this.getOnPos(), ModSoundEvents.ENTITY_PRIME_SPIRIT_MUSIC_END, SoundSource.HOSTILE, 5, 1);
			this.level().playSound(null, this.getOnPos(), ModSoundEvents.ENTITY_PRIME_SPIRIT_DEATH_SPEECH, SoundSource.HOSTILE, 5, 1);
		}
		if (this.deathTime == 430) {
			this.deathPoseAnimationState.stop();
			this.deathAnimationState.start(this.tickCount);
			this.level().playSound(null, this.getOnPos(), ModSoundEvents.ENTITY_PRIME_SPIRIT_DEATH, SoundSource.HOSTILE, 5, 1);
		}
		if (this.deathTime >= 400) {
			if (this.getRandom().nextInt(100) >= 100) {
				this.playSound(ModSoundEvents.BLOCK_GRIMSTEEL_BREAK);
			}
			this.setNoGravity(true);
			this.isNoGravity();
			this.move(MoverType.SELF, new Vec3(0.0, 0.10000000149011612, 0.0));
		}
		if (this.deathTime == 600 && this.level() instanceof ServerLevel) {

			if (bl) {
				ExperienceOrb.award((ServerLevel)this.level(), this.position(), this.level().getDifficulty().getId() * 5000);
			}

			this.remove(RemovalReason.KILLED);
			this.gameEvent(GameEvent.ENTITY_DIE);
		}

	}

	public void aiStep() {

		// If the Prime Spirit is under half health, it becomes enraged--phase 2.

		if (this.isAlive()) {
			if (!this.isAngered() && this.getHealth() <= this.getMaxHealth() / 2) {
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
		this.entityData.define(DATA_ID_INV, 0);
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
		if (this.hasCustomName()) {
			this.bossEvent.setName(this.getDisplayName());
		}
		this.setAngered(nbt.getBoolean("IsAngered"));
	}

	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossEvent.setName(this.getDisplayName());
	}

	public boolean isAngered() {
		return this.entityData.get(ANGERED);
	}
	public void setAngered(boolean isAngered) {
		this.entityData.set(ANGERED, isAngered);
		this.playSound(ModSoundEvents.ENTITY_GHOST_EMPOWERED, 0.75F, 1.0F);
		this.playSound(ModSoundEvents.ENTITY_PRIME_SPIRIT_WEAK, 5.0F, 1.0F);
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


	static {
		DATA_ID_INV = SynchedEntityData.defineId(PrimeSpirit.class, EntityDataSerializers.INT);
	}

}
