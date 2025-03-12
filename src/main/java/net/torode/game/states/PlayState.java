package net.torode.game.states;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.torode.GamePlayer;
import net.torode.ai.Player;
import net.torode.game.AbstractGameState;

public class PlayState implements AbstractGameState
{
    public void eliminatePlayer()
    {

    }

    @Override
    public void spawnEvent(PlayerSpawnEvent event)
    {
    }

    @Override
    public void disconnectEvent(PlayerDisconnectEvent event)
    {
        //new Player((GamePlayer) event.getPlayer());
    }
}
