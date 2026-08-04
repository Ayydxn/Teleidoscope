package me.ayydxn.teleidoscope.steam.util;

import me.ayydxn.teleidoscope.TeleidoscopeMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SteamUtils
{
    public static void showSuccessfulConnectionToast(Minecraft client)
    {
        Component toastTitle = Component.literal("Teleidoscope - Steamworks");
        Component toastMessage = Component.literal("Successfully connected to Steam!");

        client.getToasts().addToast(SystemToast.multiline(client, SystemToast.SystemToastIds.UNSECURE_SERVER_WARNING, toastTitle, toastMessage));
    }

    public static void showFailedConnectionToast(Minecraft client)
    {
        Component toastTitle = Component.literal("Teleidoscope - Steamworks");
        Component toastMessage = Component.literal("Failed to connect to Steam! Ensure Steam is running and try connecting again via Teleidoscope's options menu.");

        client.getToasts().addToast(SystemToast.multiline(client, SystemToast.SystemToastIds.UNSECURE_SERVER_WARNING, toastTitle, toastMessage));
    }

    public static void writeSteamAppIdFile(Path folder, int steamAppID)
    {
        String fileContents = Integer.toString(steamAppID);
        String steamAppIDFilename = "steam_appid.txt";

        Path processWorkingDirectory = Path.of(System.getProperty("user.dir"));
        writeSteamAppIdFileTo(processWorkingDirectory.resolve(steamAppIDFilename), fileContents);

        if (!folder.toAbsolutePath().normalize().equals(processWorkingDirectory.toAbsolutePath().normalize()))
            writeSteamAppIdFileTo(folder.resolve(steamAppIDFilename), fileContents);
    }

    private static void writeSteamAppIdFileTo(Path destination, String fileContents)
    {
        try
        {
            Files.writeString(destination, fileContents, StandardCharsets.UTF_8);
        }
        catch (IOException exception)
        {
            TeleidoscopeMod.LOGGER.warn("Failed to write steam_appid.txt to '{}' - Steamworks initialization may fail unless the game was launched through Steam.", destination, exception);
        }
    }
}
