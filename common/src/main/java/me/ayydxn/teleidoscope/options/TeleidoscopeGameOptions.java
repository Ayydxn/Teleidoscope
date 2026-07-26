package me.ayydxn.teleidoscope.options;

import com.google.gson.FieldNamingPolicy;
import dev.architectury.platform.Platform;
import dev.isxander.yacl3.api.NameableEnum;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import me.ayydxn.teleidoscope.TeleidoscopeMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TeleidoscopeGameOptions
{
    private static final ConfigClassHandler<TeleidoscopeGameOptions> CONFIG_CLASS_HANDLER = ConfigClassHandler.createBuilder(TeleidoscopeGameOptions.class)
            .id(new ResourceLocation(TeleidoscopeMod.MOD_ID, "teleidoscope-game-options"))
            .serializer(configClassHandler -> GsonConfigSerializerBuilder.create(configClassHandler)
                    .setPath(Platform.getConfigFolder().resolve("teleidoscope-options.json5"))
                    .appendGsonBuilder(gsonBuilder -> gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY))
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public GlobalSettings globalSettings = new GlobalSettings();

    @SerialEntry
    public HostSessionSettings hostSessionSettings = new HostSessionSettings();

    @SerialEntry
    public AdvancedSettings advancedSettings = new AdvancedSettings();

    public static TeleidoscopeGameOptions defaults()
    {
        return CONFIG_CLASS_HANDLER.defaults();
    }

    public static TeleidoscopeGameOptions load()
    {
        CONFIG_CLASS_HANDLER.load();

        return CONFIG_CLASS_HANDLER.instance();
    }

    public void save()
    {
        CONFIG_CLASS_HANDLER.save();
    }

    public static final class GlobalSettings
    {
        public boolean openWorldOnLaunch = false;
        public boolean isRichPresenceEnabled = true;
        public boolean showModMismatchDiffOnBlock = true;
        public HandshakeEnforcement handshakeEnforcement = HandshakeEnforcement.STRICT;
        public int connectionTimeoutSeconds = 15;
        public boolean isUnreliableLaneEnabled = true;
    }

    public static final class HostSessionSettings
    {
        public LobbyVisibility lobbyVisibility = LobbyVisibility.FRIENDS_ONLY;
        public int maxPlayers = 8;
        public String worldNameOverride = null;
    }

    public static final class AdvancedSettings
    {
        public SteamNetworkingLogLevel steamNetworkingLogLevel = SteamNetworkingLogLevel.WARNING;
        public int steamCallbackIntervalTicks = 1;
    }

    public enum HandshakeEnforcement implements NameableEnum
    {
        STRICT,
        LENIENT;

        @Override
        public Component getDisplayName()
        {
            return Component.literal(this.name().charAt(0) + this.name().substring(1).toLowerCase());
        }
    }

    public enum SteamNetworkingLogLevel implements NameableEnum
    {
        OFF,
        DEBUG,
        WARNING,
        ERROR;

        @Override
        public Component getDisplayName()
        {
            return Component.literal(this.name().charAt(0) + this.name().substring(1).toLowerCase());
        }
    }

    public enum LobbyVisibility implements NameableEnum
    {
        FRIENDS_ONLY("Friends Only"),
        INVITE_ONLY("Invite Only"),
        PRIVATE("Private");

        private final String displayName;

        LobbyVisibility(String displayName)
        {
            this.displayName = displayName;
        }

        @Override
        public Component getDisplayName()
        {
            return Component.literal(this.displayName);
        }
    }
}
