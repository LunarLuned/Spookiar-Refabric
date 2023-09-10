package net.lunarluned.spookiar.registry.entities.living_entities.wisp;

import net.lunarluned.spookiar.registry.effects.ModEffects;
import net.lunarluned.spookiar.registry.items.ModItems;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.misc.util.mobs.ContainerMob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class Wisp extends Monster implements ContainerMob {


	private boolean hasLimitedLife;
	private int limitedLifeTicks;
	private BlockPos boundOrigin;

	public Wisp(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
		this.moveControl = new WispMoveControl(this);
		this.xpReward = 3;
	}

	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("BoundX")) {
			this.boundOrigin = new BlockPos(nbt.getInt("BoundX"), nbt.getInt("BoundY"), nbt.getInt("BoundZ"));
		}

		if (nbt.contains("LifeTicks")) {
			this.setLimitedLife(nbt.getInt("LifeTicks"));
		}

	}

	@Override
	protected void tickDeath() {

		++this.deathTime;
		if (deathTime >= 1) {
			spawnSoulSpeedParticle();
			this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY(), this.getZ(), 0.0, 0.1, 0.0);
		}
		if (deathTime >= 20) {
			this.remove(RemovalReason.KILLED);
			this.gameEvent(GameEvent.ENTITY_DIE);
		}
	}

	public void setLimitedLife(int lifeTicks) {
		this.hasLimitedLife = true;
		this.limitedLifeTicks = lifeTicks;
	}

	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		if (this.boundOrigin != null) {
			nbt.putInt("BoundX", this.boundOrigin.getX());
			nbt.putInt("BoundY", this.boundOrigin.getY());
			nbt.putInt("BoundZ", this.boundOrigin.getZ());
		}

		if (this.hasLimitedLife) {
			nbt.putInt("LifeTicks", this.limitedLifeTicks);
		}

	}


	public void setBoundOrigin(@Nullable BlockPos pos) {
		this.boundOrigin = pos;
	}

	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		return dimensions.height - 0.28125F;
	}

	public void move(MoverType movementType, Vec3 movement) {
		super.move(movementType, movement);
		this.checkInsideBlocks();
	}

	public void tick() {
		this.noPhysics = true;
		super.tick();
		this.noPhysics = false;
		this.setNoGravity(true);
		if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
			this.limitedLifeTicks = 20;
			this.hurt(this.damageSources().starve(), 1.0F);
		}

	}

	@Override
	public boolean fromItem() {
		return true;
	}

	@Override
	public void setFromItem(boolean b) {

	}

	@Override
	public void saveToItemTag(ItemStack itemStack) {
		ContainerMob.saveDefaultDataToItemTag(this, itemStack);
		CompoundTag compoundTag = itemStack.getOrCreateTag();
	}

	@Override
	public void loadFromItemTag(CompoundTag compoundTag) {
		ContainerMob.loadDefaultDataFromItemTag(this, compoundTag);
	}

	@Override
	public ItemStack getResultItemStack() {
		return new ItemStack(ModItems.BOTTLED_WISP);
	}

	@Override
	public SoundEvent getPickupSound() {
		return SoundEvents.BOTTLE_FILL;
	}

	private class WispMoveControl extends MoveControl {
		public WispMoveControl(Wisp owner) {
			super(owner);
		}

		public void tick() {
			if (this.operation == Operation.MOVE_TO) {
				Vec3 vec3 = new Vec3(this.wantedX - Wisp.this.getX(), this.wantedY - Wisp.this.getY(), this.wantedZ - Wisp.this.getZ());
				double d = vec3.length();
				if (d < Wisp.this.getBoundingBox().getSize()) {
					this.operation = Operation.WAIT;
					Wisp.this.setDeltaMovement(Wisp.this.getDeltaMovement().scale(0.5));
				} else {
					Wisp.this.setDeltaMovement(Wisp.this.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.05 / d)));
					if (Wisp.this.getTarget() == null) {
						Vec3 vec32 = Wisp.this.getDeltaMovement();
						Wisp.this.setYRot(-((float) Mth.atan2(vec32.x, vec32.z)) * 57.295776F);
						Wisp.this.yBodyRot = Wisp.this.getYRot();
					} else {
						double e = Wisp.this.getTarget().getX() - Wisp.this.getX();
						double f = Wisp.this.getTarget().getZ() - Wisp.this.getZ();
						Wisp.this.setYRot(-((float)Mth.atan2(e, f)) * 57.295776F);
						Wisp.this.yBodyRot = Wisp.this.getYRot();
					}
				}

			}
		}
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(4, new WispChargeAttackGoal());
		this.goalSelector.addGoal(8, new WispRandomMoveGoal());
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[]{Raider.class})).setAlertOthers(new Class[0]));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, true));
	}

	private class WispChargeAttackGoal extends Goal {
		public WispChargeAttackGoal() {
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		public boolean canUse() {
			LivingEntity livingEntity = Wisp.this.getTarget();
			if (livingEntity != null && livingEntity.isAlive() && !Wisp.this.getMoveControl().hasWanted() && Wisp.this.random.nextInt(reducedTickDelay(7)) == 0) {
				return Wisp.this.distanceToSqr(livingEntity) > 4.0;
			} else {
				return false;
			}
		}

		public boolean canContinueToUse() {
			return Wisp.this.getMoveControl().hasWanted() && Wisp.this.getTarget() != null && Wisp.this.getTarget().isAlive();
		}

		public void start() {
			LivingEntity livingEntity = Wisp.this.getTarget();
			if (livingEntity != null) {
				Vec3 vec3 = livingEntity.getEyePosition();
				Wisp.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0);
			}

			Wisp.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		public void tick() {
			LivingEntity livingEntity = Wisp.this.getTarget();
			if (livingEntity != null) {
				if (Wisp.this.getBoundingBox().intersects(livingEntity.getBoundingBox())) {
					Wisp.this.doHurtTarget(livingEntity);
				} else {
					double d = Wisp.this.distanceToSqr(livingEntity);
					if (d < 9.0) {
						Vec3 vec3 = livingEntity.getEyePosition();
						Wisp.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0);
					}
				}

			}
		}
	}

	// Attributes

	public static AttributeSupplier.Builder createWispAttributes() {
		return createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.ARMOR, 1).add(Attributes.MOVEMENT_SPEED, 0.35f).add(Attributes.ATTACK_DAMAGE, 4.0).add(Attributes.FOLLOW_RANGE, 50.0);
	}

	@Override
	public boolean doHurtTarget(Entity entity) {

		if (!this.isSilent()) {
			this.playSound(ModSoundEvents.ENTITY_GHOST_JUMPSCARE, 1.0F, 2.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
		}
		assert entity instanceof LivingEntity;
		((LivingEntity) entity).addEffect(new MobEffectInstance(ModEffects.STAGNATED, 200, 0, false, true, true));
		this.hurt(damageSources().starve(), 10);

		return super.doHurtTarget(entity);
	}

	@Nullable
	public BlockPos getBoundOrigin() {
		return this.boundOrigin;
	}

	private class WispRandomMoveGoal extends Goal {
		public WispRandomMoveGoal() {
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		public boolean canUse() {
			return !Wisp.this.getMoveControl().hasWanted() && Wisp.this.random.nextInt(reducedTickDelay(7)) == 0;
		}

		public boolean canContinueToUse() {
			return false;
		}

		public void tick() {
			BlockPos blockPos = Wisp.this.getBoundOrigin();
			if (blockPos == null) {
				blockPos = Wisp.this.blockPosition();
			}

			for(int i = 0; i < 3; ++i) {
				BlockPos blockPos2 = blockPos.offset(Wisp.this.random.nextInt(15) - 7, Wisp.this.random.nextInt(11) - 5, Wisp.this.random.nextInt(15) - 7);
				if (Wisp.this.level().isEmptyBlock(blockPos2)) {
					Wisp.this.moveControl.setWantedPosition((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 0.5, (double)blockPos2.getZ() + 0.5, 0.25);
					if (Wisp.this.getTarget() == null) {
						Wisp.this.getLookControl().setLookAt((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 0.5, (double)blockPos2.getZ() + 0.5, 180.0F, 20.0F);
					}
					break;
				}
			}

		}
	}
	@Override
	public InteractionResult mobInteract(Player player, @NotNull InteractionHand interactionHand) {
		return ContainerMob.containerMobPickup(player, interactionHand, this, Items.GLASS_BOTTLE).orElse(super.mobInteract(player, interactionHand));
	}
}
