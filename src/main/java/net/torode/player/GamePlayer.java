package net.torode.player;

import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.torode.player.meta.GameMeta;
import net.torode.player.meta.RedLightGreenLightMeta;
import org.jetbrains.annotations.NotNull;

public class GamePlayer extends Player
{
    private boolean spectator;
    private GameMeta meta;

    public GamePlayer(@NotNull PlayerConnection conn, @NotNull GameProfile profile)
    {
        super(conn, profile);

        meta = new RedLightGreenLightMeta();
    }

    public boolean isSpectator()
    {
        return spectator;
    }

    public void setSpectatorTrue()
    {
        this.spectator = true;

        setGameMode(GameMode.ADVENTURE);
        setAllowFlying(true);
    }

    public void setSpectatorFalse()
    {
        this.spectator = false;

        setGameMode(GameMode.SURVIVAL);
        setAllowFlying(false);
        setFlying(false);
    }

    public void toggleSpectator()
    {
        if (spectator)
        {
            setSpectatorFalse();
        } else
        {
            setSpectatorTrue();
        }
    }

    public GameMeta getMeta()
    {
        return meta;
    }
}
