package io.tofpu.speedbridge2.arena;

import io.tofpu.speedbridge2.util.ClosestNumberFinder;
import io.tofpu.speedbridge2.util.Position;
import java.util.*;
import java.util.function.Supplier;

/**
 * This class is responsible for calculating the position of an arena. It is used by the {@link ArenaManager}.
 */
public class DefaultPositionCalculator<K> implements PositionCalculator<K> {
    /**
     * The default z-axis value. This will remain the same for all position calculations.
     */
    private final int defaultZ;
    /**
     * The default y-axis value. This will remain the same for all position calculations.
     */
    private final int defaultY;
    /**
     * The value that will be added to the x-axis value for each position calculation.
     */
    private final Supplier<Integer> xAxisGapSupplier;
    /**
     * THis is used to store the reserved positions of arenas.
     */
    private final Map<K, Entry<Width, Position>> reservedWidths = new HashMap<>();
    /**
     * Keeps track of the free widths. The goal is to allow the next position calculation to use a previously freed
     * width, if possible.
     * <p></p>
     * This would solve an issue where the world would contain gaps from the previously served positions due to
     * the arena being destroyed and thus freed.
     */
    private final List<Integer> freeWidthList = new ArrayList<>();
    /**
     * Maps a free width to the positions that were freed with that width. That way, we can reuse the position that
     * was freed in the next position calculation for reusability.
     */
    private final Map<Integer, List<Position>> freeWidthToPositionsMap = new HashMap<>();

    /**
     * The current x-axis value. This will be incremented for each position calculation.
     */
    private int currentX;

    public DefaultPositionCalculator(int currentX, int defaultZ, int defaultY, Supplier<Integer> xAxisGapSupplier) {
        this.currentX = currentX;
        this.defaultZ = defaultZ;
        this.defaultY = defaultY;
        this.xAxisGapSupplier = xAxisGapSupplier;
    }

    @Override
    public Position reserve(K key, int width) {
        return tryToUsePreviouslyReservedPosition(key, width).orElseGet(() -> createNewReservedPosition(key, width));
    }

    private Position createNewReservedPosition(K key, int width) {
        Position position = new Position(currentX, defaultY, defaultZ);
        reservedWidths.put(key, entry(width, position));

        // update the current x position to the next position
        // for the next reserved position call
        currentX += width + xAxisGapSupplier.get();
        return position;
    }

    private Entry<Width, Position> entry(int width, Position position) {
        return new Entry<>(Width.of(width), position);
    }

    private Optional<Position> tryToUsePreviouslyReservedPosition(K key, int width) {
        if (freeWidthList.isEmpty()) {
            return Optional.empty();
        }

        // find the closest width in the list of free widths
        Integer closestWidth = ClosestNumberFinder.findClosest(width, freeWidthList);
        if (closestWidth == null) {
            return Optional.empty();
        }

        // get the positions that are free for the closest width
        List<Position> freedPositions = freeWidthToPositionsMap.get(closestWidth);
        if (freedPositions == null || freedPositions.isEmpty()) {
            return Optional.empty();
        }

        // remove the first position from the list of freed positions
        Position freedPosition = freedPositions.remove(0);
        // frees some memory in-case if there are no more positions left
        if (freedPositions.isEmpty()) {
            freeWidthToPositionsMap.remove(closestWidth);
            freeWidthList.remove(closestWidth);
        }
        reservedWidths.put(key, entry(closestWidth, freedPosition));
        return Optional.of(freedPosition);
    }

    @Override
    public void release(K key) {
        Entry<Width, Position> entry = reservedWidths.remove(key);
        if (entry == null) {
            return;
        }
        freeWidthToPositionsMap
                .computeIfAbsent(entry.key.value, k -> new ArrayList<>())
                .add(entry.value);
        freeWidthList.add(entry.key.value);
    }

    static class Width {
        private final int value;

        Width(int value) {
            this.value = value;
        }

        static Width of(int value) {
            return new Width(value);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            Width width1 = (Width) object;
            return value == width1.value;
        }

        @Override
        public int hashCode() {
            return value;
        }
    }

    static class Entry<K, V> {
        final K key;
        final V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            Entry<?, ?> entry = (Entry<?, ?>) object;
            return Objects.equals(key, entry.key) && Objects.equals(value, entry.value);
        }

        @Override
        public int hashCode() {
            int result = Objects.hashCode(key);
            result = 31 * result + Objects.hashCode(value);
            return result;
        }
    }
}
