package me.ayydxn.teleidoscope.client.gui.screens;

import com.google.common.collect.Lists;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import me.ayydxn.teleidoscope.TeleidoscopeMod;
import me.ayydxn.teleidoscope.options.TeleidoscopeGameOptions;
import me.ayydxn.teleidoscope.options.categories.AdvancedSettingsCategory;
import me.ayydxn.teleidoscope.options.categories.GlobalSettingsCategory;
import me.ayydxn.teleidoscope.options.util.OptionsCategory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeleidoscopeOptionsScreen
{
    private final List<OptionsCategory> optionsCategories = Lists.newArrayList(
            new GlobalSettingsCategory(),
            new AdvancedSettingsCategory()
    );
    private final TeleidoscopeGameOptions gameOptions;
    private final Screen previousScreen;

    public TeleidoscopeOptionsScreen(@Nullable Screen previousScreen)
    {
        this.gameOptions = TeleidoscopeMod.getInstance().getGameOptions();
        this.previousScreen = previousScreen;
    }

    public Screen getHandle()
    {
        List<ConfigCategory> builtConfigCategories = this.optionsCategories.stream()
                .map(category -> category.create(this.gameOptions))
                .toList();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Teleidoscope Options"))
                .save(this.gameOptions::save)
                .categories(builtConfigCategories)
                .build()
                .generateScreen(this.previousScreen);
    }
}
