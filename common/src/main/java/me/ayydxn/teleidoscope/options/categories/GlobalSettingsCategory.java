package me.ayydxn.teleidoscope.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import me.ayydxn.teleidoscope.options.TeleidoscopeGameOptions;
import me.ayydxn.teleidoscope.options.util.OptionsCategory;
import me.ayydxn.teleidoscope.options.util.OptionsFactory;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class GlobalSettingsCategory implements OptionsCategory
{
    @Override
    public ConfigCategory create(@NotNull TeleidoscopeGameOptions gameOptions)
    {
        TeleidoscopeGameOptions.GlobalSettings globalSettings = gameOptions.globalSettings;
        TeleidoscopeGameOptions.GlobalSettings defaultGlobalSettings = TeleidoscopeGameOptions.defaults().globalSettings;

        return ConfigCategory.createBuilder()
                .name(Component.translatable("teleidoscope.options.category.global"))
                .option(OptionsFactory.toggle("teleidoscope.options.global.openWorldOnLaunch",
                        defaultGlobalSettings.openWorldOnLaunch,
                        () -> globalSettings.openWorldOnLaunch,
                        newValue -> globalSettings.openWorldOnLaunch = newValue))
                .option(OptionsFactory.toggle("teleidoscope.options.global.isRichPresenceEnabled",
                        defaultGlobalSettings.isRichPresenceEnabled,
                        () -> globalSettings.isRichPresenceEnabled,
                        newValue -> globalSettings.isRichPresenceEnabled = newValue))
                .option(OptionsFactory.toggle("teleidoscope.options.global.showModMismatchDiffOnBlock",
                        defaultGlobalSettings.showModMismatchDiffOnBlock,
                        () -> globalSettings.showModMismatchDiffOnBlock,
                        newValue -> globalSettings.showModMismatchDiffOnBlock = newValue))
                .option(OptionsFactory.enumCycle("teleidoscope.options.global.handshakeEnforcement",
                        TeleidoscopeGameOptions.HandshakeEnforcement.class,
                        defaultGlobalSettings.handshakeEnforcement,
                        () -> globalSettings.handshakeEnforcement,
                        newValue -> globalSettings.handshakeEnforcement = newValue))
                .option(OptionsFactory.intSlider("teleidoscope.options.global.connectionTimeoutSeconds",
                        defaultGlobalSettings.connectionTimeoutSeconds,
                        () -> globalSettings.connectionTimeoutSeconds,
                        newValue -> globalSettings.connectionTimeoutSeconds = newValue, 5, 60, 1,
                        value ->
                        {
                            if (value == 60)
                                return Component.translatable("teleidoscope.options.unit.minute", 1);

                            return Component.translatable("teleidoscope.options.unit.seconds", value);
                        }))
                .option(OptionsFactory.toggle("teleidoscope.options.global.isUnreliableLaneEnabled",
                        defaultGlobalSettings.isUnreliableLaneEnabled,
                        () -> globalSettings.isUnreliableLaneEnabled,
                        newValue -> globalSettings.isUnreliableLaneEnabled = newValue))
                .build();
    }
}
