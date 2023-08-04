package net.lunarluned.spookiar.registry.entities.living_entities.ghost.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.lunarluned.spookiar.SpookiarTags;
import net.lunarluned.spookiar.registry.entities.living_entities.ghost.Ghost;
import net.lunarluned.spookiar.registry.entities.living_entities.ghost.GhostBrain;
import net.lunarluned.spookiar.registry.entities.registry.ModMemoryModules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.player.Player;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GhostSpecificSensor extends Sensor<LivingEntity> {

    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, ModMemoryModules.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_CLOAK, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
    }

    @SuppressWarnings("all")
    protected void doTick(ServerLevel serverLevel, LivingEntity ghost) {
        Brain<?> brain = ghost.getBrain();
        Optional<Mob> optional = Optional.empty();
        Optional<Player> attackablePlayer = Optional.empty();
        List<Ghost> list2 = Lists.newArrayList();

        NearestVisibleLivingEntities nearestVisibleLivingEntities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
        Iterator<LivingEntity> var15 = nearestVisibleLivingEntities.findAll((livingEntityx) -> true).iterator();

        while (true) {
            while (true) {
                while (var15.hasNext()) {
                    LivingEntity livingEntity2 = var15.next();

                    if (livingEntity2 instanceof Player player && attackablePlayer.isEmpty() && !GhostBrain.isWearingCloak(player)) {
                        if (attackablePlayer.isEmpty() && ghost.canAttack(livingEntity2)) {
							attackablePlayer = Optional.of(player);
                        }
                    } else if (optional.isEmpty() && (livingEntity2.getType().is(SpookiarTags.GHOST_HOSTILE_TO))) {
                        optional = Optional.of((Mob)livingEntity2);
                    }
                }

                List<LivingEntity> list3 = brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of());

                for (LivingEntity livingEntity3 : list3) {
                    if (livingEntity3 instanceof Ghost ghostEntity) {
                        list2.add(ghostEntity);
                    }
                }

                brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional);
				brain.setMemory(ModMemoryModules.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_CLOAK, attackablePlayer);
				return;
            }
        }
    }
}
