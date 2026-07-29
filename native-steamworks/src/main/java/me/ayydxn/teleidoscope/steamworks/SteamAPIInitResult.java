package me.ayydxn.teleidoscope.steamworks;

public enum SteamAPIInitResult
{
    OK(0),

    /** Some other failure */
    FAILED_GENERIC(1),

    /** We cannot connect to Steam, steam probably isn't running */
    NO_STEAM_CLIENT(2),

    /** // Steam client appears to be out of date */
    VERSION_MISMATCH(3);

    private final int nativeValue;

    SteamAPIInitResult(int nativeValue)
    {
        this.nativeValue = nativeValue;
    }

    public static SteamAPIInitResult fromNativeValue(int nativeValue)
    {
        return switch (nativeValue)
        {
            case 0 -> OK;
            case 1 -> FAILED_GENERIC;
            case 2 -> NO_STEAM_CLIENT;
            case 3 -> VERSION_MISMATCH;
            default -> throw new IllegalArgumentException("Invalid native value for SteamAPIInitResult: " + nativeValue);
        };
    }

    public int getNativeValue()
    {
        return this.nativeValue;
    }
}
