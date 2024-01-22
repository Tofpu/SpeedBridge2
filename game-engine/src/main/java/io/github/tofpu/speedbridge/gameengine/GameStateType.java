package io.github.tofpu.speedbridge.gameengine;

@FunctionalInterface
public interface GameStateType<D extends GameData> {
    boolean test(final Game<D> game);
//    default String id() {
//        return this.getClass().getName();
//    }

    class InitiateGameStateType<D extends GameData> implements GameStateType<D> {
        @Override
        public boolean test(Game<D> game) {
            return true;
        }
    }
}
