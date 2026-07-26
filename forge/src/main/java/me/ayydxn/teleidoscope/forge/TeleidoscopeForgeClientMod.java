package me.ayydxn.teleidoscope.forge;

import me.ayydxn.teleidoscope.TeleidoscopeClientMod;
import me.ayydxn.teleidoscope.TeleidoscopeMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TeleidoscopeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TeleidoscopeForgeClientMod
{
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent clientSetupEvent)
    {
        TeleidoscopeClientMod.initialize();
    }
}
