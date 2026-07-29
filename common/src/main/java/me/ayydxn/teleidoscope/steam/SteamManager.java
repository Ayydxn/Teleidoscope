package me.ayydxn.teleidoscope.steam;

import dev.architectury.platform.Platform;
import me.ayydxn.teleidoscope.TeleidoscopeMod;
import me.ayydxn.teleidoscope.steam.util.SteamUtils;
import me.ayydxn.teleidoscope.steamworks.SteamAPI;
import me.ayydxn.teleidoscope.steamworks.SteamAPIInitResponse;
import me.ayydxn.teleidoscope.util.natives.NativeLibraryLoader;

import java.util.Optional;

public class SteamManager
{
    private static SteamManager INSTANCE;

    // TODO: (Ayydxn) Somehow get and pay Valve $100 for a custom app ID so we don't have to deal with the overlay reporting us as playing Spacewar.
    private static final int STEAM_APP_ID = 480;

    private boolean isInitialized = false;

    private SteamManager()
    {
    }

    public static synchronized void initialize()
    {
        if (INSTANCE != null)
            return;

        INSTANCE = new SteamManager();
        INSTANCE.trySteamInitialization();
    }

    public void tick()
    {
        if (!this.isInitialized)
            return;

        SteamAPI.runCallbacks();
    }

    public void shutdown()
    {
        if (!this.isInitialized)
            return;

        TeleidoscopeMod.LOGGER.info("Shutting down Steamworks...");

        SteamAPI.shutdown();

        this.isInitialized = false;
    }

    private void trySteamInitialization()
    {
        NativeLibraryLoader.load();

        SteamUtils.writeSteamAppIdFile(Platform.getGameFolder(), STEAM_APP_ID);

        SteamAPIInitResponse steamInitializationResponse = SteamAPI.initEx();
        if (!steamInitializationResponse.isSuccess())
        {
            TeleidoscopeMod.LOGGER.error("Failed to initialize Steamworks! (Result: {}, Reason: {})", steamInitializationResponse.result(),
                    steamInitializationResponse.errorMessage());

            return;
        }

        TeleidoscopeMod.LOGGER.info("Successfully initialized Steamworks!");

        this.isInitialized = true;
    }

    public static Optional<SteamManager> getInstance()
    {
        return Optional.ofNullable(INSTANCE);
    }
}
