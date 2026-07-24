package me.ayydxn.teleidoscope;

import dev.architectury.platform.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class TeleidoscopeMod
{
    public static final Logger LOGGER = (Logger) LogManager.getLogger("Teleidoscope");
    public static final String MOD_ID = "teleidoscope";

    public static void initialize()
    {
        LOGGER.info("Initializing Teleidoscope... (Version: {})", Platform.getMod(MOD_ID).getVersion());
    }
}
