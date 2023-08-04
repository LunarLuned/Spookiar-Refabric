package net.lunarluned.spookiar.registry.entities.registry;

import net.lunarluned.spookiar.Spookiar;
import net.lunarluned.spookiar.registry.entities.living_entities.ghost.Ghost;
import net.lunarluned.spookiar.registry.entities.living_entities.ghost.ai.GhostSpecificSensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.sydokiddo.chrysalis.mixin.util.SensorTypeAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ModSensorTypes {

	// List of Sensor Types:


	public static final SensorType<GhostSpecificSensor> GHOST_SPECIFIC_SENSOR = registerSensorType("ghost_specific_sensor", GhostSpecificSensor::new);

	// Registry for Sensor Types:

	@NotNull
	private static <U extends Sensor<?>> SensorType<U> registerSensorType(String string, Supplier<U> supplier) {
		return SensorTypeAccessor.callRegister(Spookiar.MOD_ID + ":" + string, supplier);
	}
}
