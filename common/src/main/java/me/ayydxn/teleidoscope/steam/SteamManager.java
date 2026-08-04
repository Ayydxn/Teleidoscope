package me.ayydxn.teleidoscope.steam;

import dev.architectury.platform.Platform;
import me.ayydxn.teleidoscope.TeleidoscopeMod;
import me.ayydxn.teleidoscope.steam.util.SteamUtils;
import me.ayydxn.teleidoscope.steamworks.SteamAPI;
import me.ayydxn.teleidoscope.steamworks.SteamAPIInitResponse;
import me.ayydxn.teleidoscope.util.natives.NativeLibraryLoader;

public class SteamManager
{
    private static SteamManager INSTANCE;

    // TODO: (Ayydxn) Somehow get and pay Valve $100 for a custom app ID so we don't have to deal with the overlay reporting us as playing Spacewar.
    private static final int STEAM_APP_ID = 480;

    private ConnectionStatus connectionStatus;
    private int ticksSinceLastCallback = 0;

    private SteamManager()
    {
    }

    public boolean initialize()
    {
        if (this.connectionStatus == ConnectionStatus.INITIALIZING)
            return true;

        NativeLibraryLoader.load();

        SteamUtils.writeSteamAppIdFile(Platform.getGameFolder(), STEAM_APP_ID);

        this.connectionStatus = ConnectionStatus.INITIALIZING;

        TeleidoscopeMod.LOGGER.info("Attempting to connect to Steam...");

        SteamAPIInitResponse steamInitializationResponse = SteamAPI.initEx();
        if (!steamInitializationResponse.isSuccess())
        {
            TeleidoscopeMod.LOGGER.error("Failed to connect to Steam! (Result: {}, Reason: {})", steamInitializationResponse.result(),
                    steamInitializationResponse.errorMessage());

            this.connectionStatus = ConnectionStatus.FAILED;

            return false;
        }

        TeleidoscopeMod.LOGGER.info("Successfully connected to Steam!");

        this.connectionStatus = ConnectionStatus.CONNECTED;

        return true;
    }

    public void tick()
    {
        if (this.connectionStatus != ConnectionStatus.CONNECTED)
            return;

        int steamCallbackIntervalTicks = TeleidoscopeMod.getInstance().getGameOptions().advancedSettings.steamCallbackIntervalTicks;

        this.ticksSinceLastCallback++;

        if (this.ticksSinceLastCallback < steamCallbackIntervalTicks)
            return;

        SteamAPI.runCallbacks();

        this.ticksSinceLastCallback = 0;
    }

    public void shutdown()
    {
        if (this.connectionStatus != ConnectionStatus.CONNECTED)
            return;

        TeleidoscopeMod.LOGGER.info("Shutting down Steamworks...");

        SteamAPI.shutdown();

        this.connectionStatus = ConnectionStatus.OFFLINE;
    }

    public synchronized static SteamManager getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new SteamManager();

        return INSTANCE;
    }

    public ConnectionStatus getStatus()
    {
        return this.connectionStatus;
    }

    public enum ConnectionStatus
    {
        OFFLINE,
        INITIALIZING,
        CONNECTED,
        FAILED
    }
}
