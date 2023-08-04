package net.lunarluned.spookiar.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.lunarluned.spookiar.config.options.features.FeaturesConfig;

@Config(name = "spookiar")
@Config.Gui.Background("minecraft:textures/block/polished_deepslate.png")
public class ModConfig implements ConfigData {

        // Features

        @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)

        public Features features = new Features();

        public static class Features {

            @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
            @ConfigEntry.Gui.Tooltip
            public FeaturesConfig featuresConfig = new FeaturesConfig();

        }

    }

