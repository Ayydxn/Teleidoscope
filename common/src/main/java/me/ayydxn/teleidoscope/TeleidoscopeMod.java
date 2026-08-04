package me.ayydxn.teleidoscope;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import me.ayydxn.teleidoscope.client.gui.screens.TeleidoscopeOptionsScreen;
import me.ayydxn.teleidoscope.options.TeleidoscopeGameOptions;
import me.ayydxn.teleidoscope.steam.SteamManager;
import me.ayydxn.teleidoscope.steam.util.SteamUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

@Environment(EnvType.CLIENT)
public class TeleidoscopeMod
{
    private static volatile TeleidoscopeMod INSTANCE;

    public static final Logger LOGGER = (Logger) LogManager.getLogger("Teleidoscope");
    public static final String MOD_ID = "teleidoscope";

    private final TeleidoscopeGameOptions gameOptions;

    private TeleidoscopeMod()
    {
        this.gameOptions = TeleidoscopeGameOptions.load();
    }

    public static synchronized void initialize()
    {
        if (INSTANCE != null)
            return;

        LOGGER.info("Initializing Teleidoscope... (Version: {})", Platform.getMod(MOD_ID).getVersion());

        Platform.getMod(TeleidoscopeMod.MOD_ID).registerConfigurationScreen(parent -> new TeleidoscopeOptionsScreen(parent).getHandle());

        ClientLifecycleEvent.CLIENT_STARTED.register(client -> SteamManager.getInstance().initialize());
        ClientTickEvent.CLIENT_POST.register(client -> SteamManager.getInstance().tick());
        ClientLifecycleEvent.CLIENT_STOPPING.register(client -> SteamManager.getInstance().shutdown());

        // (Ayydxn) Would prefer to do this during client startup, but it displays tofu text for a few seconds on Fabric.
        LifecycleEvent.SERVER_STARTED.register(server ->
        {
            if (SteamManager.getInstance().getStatus() != SteamManager.ConnectionStatus.CONNECTED)
                SteamUtils.showFailedConnectionToast(Minecraft.getInstance());
        });

        INSTANCE = new TeleidoscopeMod();
    }

    public static TeleidoscopeMod getInstance()
    {
        if (INSTANCE == null)
            throw new IllegalStateException("Tried to access an instance of Teleidoscope before one was available!");

        return INSTANCE;
    }

    public TeleidoscopeGameOptions getGameOptions()
    {
        return this.gameOptions;
    }
}
