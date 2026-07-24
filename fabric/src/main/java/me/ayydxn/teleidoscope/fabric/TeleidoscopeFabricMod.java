package me.ayydxn.teleidoscope.fabric;

import me.ayydxn.teleidoscope.TeleidoscopeMod;
import net.fabricmc.api.ModInitializer;

public class TeleidoscopeFabricMod implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        TeleidoscopeMod.initialize();
    }
}
