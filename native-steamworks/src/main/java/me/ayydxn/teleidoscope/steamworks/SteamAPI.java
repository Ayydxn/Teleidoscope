package me.ayydxn.teleidoscope.steamworks;

public class SteamAPI
{
    public static SteamAPIInitResponse initEx()
    {
        final StringBuilder errorMessage = new StringBuilder();
        final SteamAPIInitResult result = SteamAPIInitResult.fromNativeValue(NativeMethods.nSteamAPI_InitEx(errorMessage));

        return new SteamAPIInitResponse(result, errorMessage.toString());
    }

    public static void runCallbacks()
    {
        NativeMethods.nSteamAPI_RunCallbacks();
    }

    public static void shutdown()
    {
        NativeMethods.nSteamAPI_Shutdown();
    }

    private static final class NativeMethods
    {
        private static native int nSteamAPI_InitEx(StringBuilder outErrorMessage);

        private static native void nSteamAPI_RunCallbacks();

        private static native void nSteamAPI_Shutdown();
    }
}