package net.torode.util;

public final class MathUtil
{
    private MathUtil()
    {}

    public static long randomLong(long min, long max)
    {
        return min + (long) (Math.random() * (max - min + 1));
    }
}
