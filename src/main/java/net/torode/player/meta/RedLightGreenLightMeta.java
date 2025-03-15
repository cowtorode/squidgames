package net.torode.player.meta;

import net.minestom.server.coordinate.Pos;

public class RedLightGreenLightMeta extends GameMeta
{
    /**
     * The position that the player was at when red light was called.
     * This is used to determine how far the player has traveled
     */
    private Pos redLightPos;

    public Pos getRedLightPos()
    {
        return redLightPos;
    }

    public void setRedLightPos(Pos pos)
    {
        redLightPos = pos;
    }
}
