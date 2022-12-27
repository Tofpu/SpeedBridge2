package io.tofpu.speedbridge2;

import io.tofpu.speedbridge2.database.storage.SQLiteStorage;
import io.tofpu.speedbridge2.database.user.DefaultUserService;
import io.tofpu.speedbridge2.database.user.UserService;
import io.tofpu.speedbridge2.database.user.repository.name.DefaultUserNameRepository;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Test
    public void test_insert_is_present_fetch_delete_user() throws ExecutionException, InterruptedException {
        final BaseStorage storage = new SQLiteStorage();
        final UserService service = new DefaultUserService(new DefaultUserNameRepository(storage));

        // initializes the process
        storage.init()
                .get();
        service.init()
                .get();

        // our data
        final UUID playerUid = UUID.randomUUID();
        final String playerName = "player1";

        // creates a new instance of BridgePlayer
        BridgePlayer player = generateBridgePlayer(playerUid, playerName);

        // inserts the player to the database
        service.insert(player.getPlayerUid(), player)
                .get();

        // fetches the player from the database
        final BridgePlayer expected = service.fetch(player.getPlayerUid())
                .get();

        // checks whether the player is present in the database
        assertTrue(service.isPresent(playerUid)
                           .get());

        // checks whether the data matches with what we have given
        assertEquals(expected.getPlayerUid(), playerUid);
        assertEquals(expected.getName(), playerName);

        // deletes the player from the database
        service.delete(playerUid)
                .get();

        // check whether the data is still present in the database
        assertFalse(service.isPresent(playerUid)
                            .get());
    }

    @NotNull
    private BridgePlayer generateBridgePlayer(final UUID uuid, final String name) {
        BridgePlayer player = mock(BridgePlayer.class);
        when(player.getPlayerUid()).thenReturn(uuid);
        when(player.getName()).thenReturn(name);

        return player;
    }
}