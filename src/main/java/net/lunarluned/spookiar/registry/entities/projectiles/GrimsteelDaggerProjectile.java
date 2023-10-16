package net.lunarluned.spookiar.registry.entities.projectiles;

import net.lunarluned.spookiar.misc.ModDamageSources;
import net.lunarluned.spookiar.registry.items.ModItems;
import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GrimsteelDaggerProjectile extends AbstractHurtingProjectile {

    private double xPower;
    private double yPower;
    private double zPower;
    public int lifeTime;
    public int soulPower = 4;

    public GrimsteelDaggerProjectile(EntityType<? extends GrimsteelDaggerProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.NEUTRAL;
    }

    @Override
    public void tick() {

        super.tick();
        ++this.lifeTime;


        Vec3 vec3 = this.getDeltaMovement();
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        this.onHit(hitResult);

        double d = this.getX() + vec3.x;
        double e = this.getY() + vec3.y;
        double f = this.getZ() + vec3.z;
        this.updateRotation();
        float h;

        if (this.isInWaterOrBubble()) {
            h = 0.8F;
        } else {
            h = 0.99f;
        }

        this.setDeltaMovement(vec3.scale(h));

        if (!this.isNoGravity()) {
            Vec3 vec32 = this.getDeltaMovement();
            this.setDeltaMovement(vec32.x, vec32.y - (double)this.getGravity(), vec32.z);
        }
        this.setPos(d, e, f);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        DamageSource damageSource;
        Entity owner = this.getOwner();

        if (owner == null) {
            damageSource = this.damageSources().source(ModDamageSources.GRIM, this, this);
        } else {
            damageSource = this.damageSources().source(ModDamageSources.GRIM, this, owner);
            if (owner instanceof LivingEntity) {
                ((LivingEntity) owner).setLastHurtMob(entityHitResult.getEntity());
            }
        }

        if (!(entityHitResult.getEntity() instanceof LivingEntity livingEntity && livingEntity.isFallFlying())) {
            entityHitResult.getEntity().hurt(damageSource, soulPower);

            soulPower = soulPower * 2;

        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && !entity.noPhysics;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else {
            this.markHurt();
            Entity entity = damageSource.getEntity();
            if (entity != null) {
                if (!this.level().isClientSide) {
                    Vec3 vec3 = entity.getLookAngle();
                    this.setDeltaMovement(vec3);
                    this.xPower = vec3.x * 2000;
                    this.yPower = vec3.y * 2000;
                    this.zPower = vec3.z * 2000;
                    this.setOwner(entity);
                    soulPower = soulPower * 2;
                }

                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        int i = entity == null ? 0 : entity.getId();
        return new ClientboundAddEntityPacket(this.getId(), this.getUUID(), this.getX(), this.getY(), this.getZ(), this.getXRot(), this.getYRot(), this.getType(), i, new Vec3(this.xPower, this.yPower, this.zPower), 0.0);
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket) {
        super.recreateFromPacket(clientboundAddEntityPacket);
        double d = clientboundAddEntityPacket.getXa();
        double e = clientboundAddEntityPacket.getYa();
        double f = clientboundAddEntityPacket.getZa();
        double g = Math.sqrt(d * d + e * e + f * f);
        if (g != 0.0) {
            this.xPower = d / g * 20;
            this.yPower = e / g * 20;
            this.zPower = f / g * 20;
        }
    }

    protected float getGravity() {
        return 0.05F;
    }
}