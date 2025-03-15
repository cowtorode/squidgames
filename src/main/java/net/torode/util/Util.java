package net.torode.util;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.sound.SoundEvent;

public final class Util
{
    private static final Particle TRAIL = Particle.SMOKE;

    public static void fireGunshot(Instance instance, Point from, Point to)
    {
        instance.playSound(Sound.sound(SoundEvent.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, Sound.Source.HOSTILE, 1.0f, 0.5f));

        // number of particles need to display between two points
        int dist = (int) from.distance(to);
        double distInv = 1.0d / dist;
        Point p;

        instance.sendGroupedPacket(new ParticlePacket(Particle.EXPLOSION, from, Pos.ZERO, 0, 1));

        for (int i = 0; i < dist; ++i)
        {
            // the ith particle out of dist particles between from and to is p.
            p = from.add(to.sub(from).mul(i * distInv)); // from + (to - from) * i / dist

            instance.sendGroupedPacket(new ParticlePacket(TRAIL, p, Pos.ZERO, 0, 1));
        }
    }

    private Util() {}
}
