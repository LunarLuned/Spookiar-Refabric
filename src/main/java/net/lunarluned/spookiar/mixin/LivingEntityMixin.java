package net.lunarluned.spookiar.mixin;

import net.lunarluned.spookiar.registry.effects.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	@Shadow
	public abstract boolean hasEffect(MobEffect mobEffect);

	@Shadow @Nullable
	public abstract MobEffectInstance getEffect(MobEffect mobEffect);

	@Shadow public abstract float getHealth();

	@Shadow public abstract boolean removeEffect(MobEffect mobEffect);

	@Shadow public abstract void heal(float f);

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}


	@ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true)
	private float spookiar_inverseDamageforPersistence(float amount) {
		if (this.hasEffect(ModEffects.PERSISTENCE) && amount > 1 && this.getHealth() <= 4) {
			this.removeEffect(ModEffects.PERSISTENCE);
			this.heal(4);
			return 0.0f;
		} else if (this.hasEffect(ModEffects.PERSISTENCE)) {
			return amount / (Objects.requireNonNull(this.getEffect(ModEffects.PERSISTENCE)).getAmplifier() + 1);
		}
		return amount;
	}

	@Inject(at = @At("HEAD"), method = "heal", cancellable = true)
	private void spookiar_heal(CallbackInfo ci) {
		if(this.getEffect(ModEffects.STAGNATED) != null) {
			ci.cancel();
		}
	}
}
