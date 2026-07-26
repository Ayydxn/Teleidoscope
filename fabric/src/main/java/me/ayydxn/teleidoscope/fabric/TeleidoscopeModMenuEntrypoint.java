package me.ayydxn.teleidoscope.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.ayydxn.teleidoscope.client.gui.screens.TeleidoscopeOptionsScreen;

public class TeleidoscopeModMenuEntrypoint implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return parent -> new TeleidoscopeOptionsScreen(parent).getHandle();
    }
}
