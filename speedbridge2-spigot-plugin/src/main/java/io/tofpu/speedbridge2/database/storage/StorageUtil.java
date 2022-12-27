package io.tofpu.speedbridge2.database.storage;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * A storage convenience utility class.
 */
public class StorageUtil {

    /**
     * @param key the {@link UUID} to transform.
     *
     * @return the {@link Byte} array form of {@link UUID}
     */
    public static byte[] uidAsByte(final UUID key) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(key.getMostSignificantBits());
        byteBuffer.putLong(key.getLeastSignificantBits());
        return byteBuffer.array();
    }
}
