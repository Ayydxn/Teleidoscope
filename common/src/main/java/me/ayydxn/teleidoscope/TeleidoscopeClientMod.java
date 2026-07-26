package me.ayydxn.teleidoscope;

import dev.architectury.platform.Platform;
import me.ayydxn.teleidoscope.client.gui.screens.TeleidoscopeOptionsScreen;

public class TeleidoscopeClientMod
{
    public static void initialize()
    {
        Platform.getMod(TeleidoscopeMod.MOD_ID).registerConfigurationScreen(parent -> new TeleidoscopeOptionsScreen(parent).getHandle());
    }
}
