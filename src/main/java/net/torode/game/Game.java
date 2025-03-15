package net.torode.game;

import net.torode.player.GamePlayer;

public abstract class Game
{
    public abstract void enable();

    public abstract void start();

    public abstract void disable();

    public abstract void eliminatePlayer(GamePlayer player);
}
