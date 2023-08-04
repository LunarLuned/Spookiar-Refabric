package net.lunarluned.spookiar.util;

import net.minecraft.world.entity.Entity;

public interface PlayerAccessor {

	void resetOffhandAttackTicks();

	float getAttackCooldownForOffhand(float baseTime);

	void offHandAttack(Entity target);

}
