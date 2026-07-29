package me.ayydxn.teleidoscope.fabric;

import me.ayydxn.teleidoscope.TeleidoscopeMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TeleidoscopeFabricClientMod implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        TeleidoscopeMod.initialize();
    }
}
