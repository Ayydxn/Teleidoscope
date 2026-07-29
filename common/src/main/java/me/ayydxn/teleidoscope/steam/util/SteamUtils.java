package me.ayydxn.teleidoscope.steam.util;

import me.ayydxn.teleidoscope.TeleidoscopeMod;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SteamUtils
{
    public static void writeSteamAppIdFile(Path folder, int steamAppID)
    {
        final String fileContents = Integer.toString(steamAppID);

        final Path processWorkingDirectory = Path.of(System.getProperty("user.dir"));
        writeSteamAppIdFileTo(processWorkingDirectory.resolve("steam_appid.txt"), fileContents);

        if (!folder.toAbsolutePath().normalize().equals(processWorkingDirectory.toAbsolutePath().normalize()))
            writeSteamAppIdFileTo(folder.resolve("steam_appid.txt"), fileContents);
    }

    private static void writeSteamAppIdFileTo(Path destination, String fileContents)
    {
        try
        {
            Files.writeString(destination, fileContents, StandardCharsets.US_ASCII);
        }
        catch (IOException exception)
        {
            TeleidoscopeMod.LOGGER.warn("Failed to write steam_appid.txt to '{}' - Steamworks initialization may fail unless the game was launched through Steam.", destination, exception);
        }
    }
}
