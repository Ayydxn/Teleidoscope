package me.ayydxn.teleidoscope;

import dev.architectury.platform.Platform;
import me.ayydxn.teleidoscope.options.TeleidoscopeGameOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

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
