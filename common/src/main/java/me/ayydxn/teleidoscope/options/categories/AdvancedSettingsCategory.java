package me.ayydxn.teleidoscope.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import me.ayydxn.teleidoscope.options.TeleidoscopeGameOptions;
import me.ayydxn.teleidoscope.options.util.OptionsCategory;
import me.ayydxn.teleidoscope.options.util.OptionsFactory;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class AdvancedSettingsCategory implements OptionsCategory
{
    @Override
    public ConfigCategory create(@NotNull TeleidoscopeGameOptions gameOptions)
    {
        TeleidoscopeGameOptions.AdvancedSettings advancedSettings = gameOptions.advancedSettings;
        TeleidoscopeGameOptions.AdvancedSettings defaultAdvancedSettings = TeleidoscopeGameOptions.defaults().advancedSettings;

        return ConfigCategory.createBuilder()
                .name(Component.translatable("teleidoscope.options.category.advanced"))
                .option(OptionsFactory.enumCycle("teleidoscope.options.advanced.steamNetworkingLogLevel",
                        TeleidoscopeGameOptions.SteamNetworkingLogLevel.class,
                        defaultAdvancedSettings.steamNetworkingLogLevel,
                        () -> advancedSettings.steamNetworkingLogLevel,
                        newValue -> advancedSettings.steamNetworkingLogLevel = newValue))
                .option(OptionsFactory.intSlider("teleidoscope.options.advanced.steamCallbackIntervalTicks",
                        defaultAdvancedSettings.steamCallbackIntervalTicks,
                        () -> advancedSettings.steamCallbackIntervalTicks,
                        newValue -> advancedSettings.steamCallbackIntervalTicks = newValue, 1, 20, 1, value ->
                        {
                            if (value == 1)
                                return Component.translatable("teleidoscope.options.unit.tick", value);

                            return Component.translatable("teleidoscope.options.unit.ticks", value);
                        }))
                .build();
    }
}
