package io.tofpu.speedbridge2.game.generic;

import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public class Position implements Comparable<Position> {
    private final int x, y, z;

    private Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean isGreater(Position to, boolean equal) {
        if (equal) {
            return getX() >= to.getX() &&
                   getY() >= to.getY() &&
                   getZ() >= to.getZ();
        }
        return getX() > to.getX() &&
               getY() > to.getY() &&
               getZ() > to.getZ();
    }

    public boolean isLesser(Position to, boolean equal) {
        if (equal) {
            return getX() <= to.getX() &&
                   getY() <= to.getY() &&
                   getZ() <= to.getZ();
        }
        return getX() < to.getX() &&
               getY() < to.getY() &&
               getZ() < to.getZ();
    }

    public boolean isEqualTo(Position to) {
        return getX() == to.getX() &&
               getY() == to.getY() &&
               getZ() == to.getZ();
    }

    public static Position zero() {
        return new Position(0, 0, 0);
    }

    public static Position of(int x, int y, int z) {
        return new Position(x, y, z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Position add(int x, int y, int z) {
        return new Position(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public int compareTo(@NotNull Position o) {
        int points = Integer.compare(this.x, o.x);
        points += Integer.compare(this.y, o.y);
        points += Integer.compare(this.z, o.z);
        return points;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Position.class.getSimpleName() + "[", "]")
                .add("x=" + x)
                .add("y=" + y)
                .add("z=" + z)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        if (y != position.y) return false;
        return z == position.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}
