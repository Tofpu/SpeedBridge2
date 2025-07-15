package io.tofpu.speedbridge2.reload.domain;

import java.util.Objects;

public interface Reloadable {
    void onReload();
    Key key();

    interface Key {
        Key ALL = of("all");

        static Key of(String id) {
            return new Impl(id);
        }

        String name();

        class Impl implements Key {
            private final String id;

            public Impl(String id) {
                this.id = id;
            }

            @Override
            public String name() {
                return id;
            }

            @Override
            public String toString() {
                return id;
            }

            @Override
            public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) return false;
                Impl impl = (Impl) o;
                return Objects.equals(id, impl.id);
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(id);
            }
        }
    }
}
