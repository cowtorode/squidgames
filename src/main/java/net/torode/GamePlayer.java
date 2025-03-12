package net.torode;

import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

public class GamePlayer extends Player
{
    public GamePlayer(@NotNull PlayerConnection conn, @NotNull GameProfile profile)
    {
        super(conn, profile);
    }
}
