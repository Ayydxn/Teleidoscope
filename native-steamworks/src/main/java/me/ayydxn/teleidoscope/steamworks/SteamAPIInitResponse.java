package me.ayydxn.teleidoscope.steamworks;

public record SteamAPIInitResponse(SteamAPIInitResult result, String errorMessage)
{
    public boolean isSuccess()
    {
        return this.result == SteamAPIInitResult.OK;
    }
}