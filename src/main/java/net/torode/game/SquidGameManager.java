package net.torode.game;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.torode.GamePlayer;

import java.util.ArrayList;
import java.util.List;

import static net.torode.game.GameState.*;

public final class SquidGameManager
{
    public static final int AUTO_START_THRESHOLD = 1;
    public static final int MAX_PLAYERS = 456;

    private static final List<GamePlayer> players;
    private static final List<GamePlayer> spectators;

    private static GameState state = WAIT;

    static
    {
        players = new ArrayList<>();
        spectators = new ArrayList<>();
    }

    public static void start()
    {
        if (state != WAIT)
        {
            throw new IllegalStateException("Games are already started.");
        }
    }

    public static void spawnWhileWaiting(PlayerSpawnEvent event)
    {
        GamePlayer player = (GamePlayer) event.getPlayer();

        // number of players currently waiting
        int count = players.size();

        if (count == MAX_PLAYERS)
        {
            // add the player as a spectator
            spectators.add(player);
        }
    }

    public static void onSpawn(PlayerSpawnEvent event)
    {
        // Player joins the server
        switch (state)
        {
            case WAIT:
                spawnWhileWaiting(event);
                break;
            case PLAY:
                // set spectator
                break;
        }
    }

    public static void onDisconnect(PlayerDisconnectEvent event)
    {

    }
}
