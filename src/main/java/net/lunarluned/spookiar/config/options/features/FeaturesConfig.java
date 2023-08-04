package net.lunarluned.spookiar.config.options.features;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class FeaturesConfig {
    @ConfigEntry.Gui.CollapsibleObject()
    @ConfigEntry.Gui.Tooltip
    public FeaturesChanges featuresChanges = new FeaturesChanges();

}
