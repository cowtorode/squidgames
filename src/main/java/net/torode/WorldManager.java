package net.torode;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;

public final class WorldManager
{
    private WorldManager()
    {
    }

    private static final InstanceContainer WORLD;
    private static final Pos SPAWN;

    static
    {
        WORLD = MinecraftServer.getInstanceManager().createInstanceContainer();

        WORLD.setTime(0);
        WORLD.setTimeRate(0);
        WORLD.setGenerator(unit ->
        {
            unit.modifier().fillHeight(-64, -63, Block.BEDROCK);
            unit.modifier().fillHeight(-63, -62, Block.GRASS_BLOCK);
        });
        WORLD.setChunkSupplier(LightingChunk::new);

        SPAWN = new Pos(0, -62, 0);
    }

    public static InstanceContainer getWorld()
    {
        return WORLD;
    }

    public static Pos getSpawn()
    {
        return SPAWN;
    }
}
