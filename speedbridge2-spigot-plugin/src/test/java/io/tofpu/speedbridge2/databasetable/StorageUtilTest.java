package io.tofpu.speedbridge2.databasetable;

import io.tofpu.speedbridge2.database.storage.StorageUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageUtilTest {
    @Test
    public void test_uid_as_byte() {
        final UUID actualUid = UUID.randomUUID();
        final byte[] uidAsByte = StorageUtil.uidAsByte(actualUid);
        final UUID fromBytesToUid = UUID.nameUUIDFromBytes(uidAsByte);

        assertEquals(fromBytesToUid, actualUid);
    }
}
