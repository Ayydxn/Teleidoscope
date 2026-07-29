package me.ayydxn.teleidoscope.util.natives;

import me.ayydxn.teleidoscope.TeleidoscopeMod;
import me.ayydxn.teleidoscope.util.TeleidoscopePaths;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class NativeLibraryLoader
{
    private static volatile boolean isLoaded = false;

    private NativeLibraryLoader()
    {
    }

    public static synchronized void load()
    {
        if (isLoaded)
            return;

        String platform = resolvePlatform();
        String libraryFileName = resolveLibraryFileName(platform);
        String redistributableFileName = resolveSteamworksRedistributableFileName(platform);

        Path extractedSteamLibrary = extractLibrary("natives/" + platform + "/" + redistributableFileName, redistributableFileName);
        Path extractedTeleidoscopeLibrary = extractLibrary("natives/" + platform + "/" + libraryFileName, libraryFileName);

        try
        {
            System.load(extractedSteamLibrary.toAbsolutePath().toString());
            System.load(extractedTeleidoscopeLibrary.toAbsolutePath().toString());
        }
        catch (UnsatisfiedLinkError error)
        {
            throw new IllegalStateException(String.format("Failed to load Steamworks native library from '%s'!", extractedTeleidoscopeLibrary.toAbsolutePath()), error);
        }

        TeleidoscopeMod.LOGGER.info("Loaded Steamworks native library ({}) for platform '{}'", libraryFileName, platform);

        isLoaded = true;
    }

    private static String resolvePlatform()
    {
        String osName = System.getProperty("os.name", "").toLowerCase();
        String osArch = System.getProperty("os.arch", "").toLowerCase();

        if (osName.contains("win"))
            return "windows-x64";

        if (osName.contains("mac") || osName.contains("darwin"))
            return (osArch.contains("aarch64") || osArch.contains("arm")) ? "macos-arm64" : "macos-x64";

        if (osName.contains("nux"))
            return "linux-x64";

        throw new IllegalStateException(String.format("Unsupported platform for Steamworks natives %s %s", osName, osArch));
    }

    private static String resolveSteamworksRedistributableFileName(String platform)
    {
        return switch (platform)
        {
            case "windows-x64" -> "steam_api64.dll";
            case "macos-x64", "macos-arm64" -> "libsteam_api.dylib";
            case "linux-x64" -> "libsteam_api.so";
            default -> throw new IllegalStateException(String.format("No Steamworks redistributable filename for the platform '%s'!", platform));
        };
    }

    private static String resolveLibraryFileName(String platform)
    {
        return switch (platform)
        {
            case "windows-x64" -> "TeleidoscopeSteamworks-Win64.dll";
            case "macos-x64", "macos-arm64" -> "libTeleidoscopeSteamworks-Mac.dylib";
            case "linux-x64" -> "libTeleidoscopeSteamworks-Linux.so";
            default -> throw new IllegalStateException(String.format("No native library filename for the platform '%s'!", platform));
        };
    }

    private static Path extractLibrary(String resourcePath, String libraryFileName)
    {
        try (InputStream nativeLibraryStream = NativeLibraryLoader.class.getClassLoader().getResourceAsStream(resourcePath))
        {
            if (nativeLibraryStream == null)
                throw new IllegalStateException(String.format("Failed to find bundled Steamworks native on the classpath at '%s'", resourcePath));

            Files.createDirectories(TeleidoscopePaths.TELEIDOSCOPE_DIRECTORY);

            Path destination = TeleidoscopePaths.TELEIDOSCOPE_DIRECTORY.resolve(libraryFileName);
            Files.copy(nativeLibraryStream, destination, StandardCopyOption.REPLACE_EXISTING);

            return destination;
        }
        catch (IOException exception)
        {
            throw new IllegalStateException(String.format("Failed to extract Steamworks native library from '%s'", resourcePath), exception);
        }
    }
}
