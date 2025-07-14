package io.tofpu.speedbridge2.placeholder.domain;

public interface InputStream {
    InputStream EMPTY = from(new String[0]);

    String readString();
    int readInt();

    static InputStream from(String[] args) {
        return new Impl(args);
    }

    class Impl implements InputStream {
        private final String[] args;
        private int currentIndex = 0;

        public Impl(String[] args) {
            this.args = args;
        }

        @Override
        public String readString() {
            ensureNextInputIsAvailable();
            return args[currentIndex++];
        }

        private void ensureNextInputIsAvailable() {
            if (currentIndex >= args.length) {
                throw new IllegalStateException("No more arguments to read");
            }
        }

        @Override
        public int readInt() {
            ensureNextInputIsAvailable();
            String input = args[currentIndex++];
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Expected an integer but found: " + input, e);
            }
        }
    }
}
