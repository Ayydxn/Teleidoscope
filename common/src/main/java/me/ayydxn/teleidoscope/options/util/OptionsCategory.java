package me.ayydxn.teleidoscope.options.util;

import dev.isxander.yacl3.api.ConfigCategory;
import me.ayydxn.teleidoscope.options.TeleidoscopeGameOptions;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface OptionsCategory
{
    ConfigCategory create(@NotNull TeleidoscopeGameOptions gameOptions);
}
