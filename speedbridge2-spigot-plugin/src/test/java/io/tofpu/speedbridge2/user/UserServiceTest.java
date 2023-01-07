package io.tofpu.speedbridge2.user;

import io.tofpu.speedbridge2.database.repository.factory.RepositoryFactoryMaker;
import io.tofpu.speedbridge2.database.repository.factory.exception.InvalidRepositoryException;
import io.tofpu.speedbridge2.database.storage.SQLiteStorage;
import io.tofpu.speedbridge2.service.user.UserService;
import io.tofpu.speedbridge2.database.repository.user.block.UserBlockChoiceService;
import io.tofpu.speedbridge2.database.repository.user.name.UserNameService;
import io.tofpu.speedbridge2.database.repository.user.score.UserScoreService;
import io.tofpu.speedbridge2.service.factory.ServiceFactoryMaker;
import io.tofpu.speedbridge2.service.factory.exception.InvalidServiceException;
import io.tofpu.speedbridge2.model.player.object.score.Score;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {
    @Test
    public void test_insert_is_present_fetch_delete_user_name()
            throws ExecutionException, InterruptedException, InvalidServiceException, InvalidRepositoryException {
        final BaseStorage storage = new SQLiteStorage();

        final UserService userService = ServiceFactoryMaker.makeFactory(
                ServiceFactoryMaker.ServiceType.DEFAULT,
                RepositoryFactoryMaker.RepositoryType.SQ_LITE,
                storage).createUserService();

        // initializes the process
        storage.init()
                .get();
        userService.init()
                .get();

        // casts over to UserNameService to gain access of its behaviors
        final UserNameService service = (UserNameService) userService;

        // our data
        final UUID playerUid = UUID.randomUUID();
        final String playerName = "player1";

        // inserts the player to the database
        service.insertName(playerUid, playerName)
                .get();

        // fetches the player from the database
        final String expected = service.fetchName(playerUid)
                .get();

        // checks whether the player is present in the database
        assertTrue(service.isNamePresent(playerUid)
                           .get());

        // checks whether the data matches with what we have given
        assertEquals(expected, playerName);

        // deletes the player from the database
        service.deleteName(playerUid)
                .get();

        // check whether the data is still present in the database
        assertFalse(service.isNamePresent(playerUid)
                            .get());
    }

    @Test
    public void test_insert_is_present_fetch_delete_user_score()
            throws ExecutionException, InterruptedException, InvalidServiceException, InvalidRepositoryException {
        final BaseStorage storage = new SQLiteStorage();
        final UserService mainService = ServiceFactoryMaker.makeFactory(
                ServiceFactoryMaker.ServiceType.DEFAULT,
                RepositoryFactoryMaker.RepositoryType.SQ_LITE,
                storage).createUserService();

        // initializes the process
        storage.init()
                .get();
        mainService.init()
                .get();

        final UserScoreService service = (UserScoreService) mainService;

        // our data
        final UUID playerUid = UUID.randomUUID();
        // creates a new instance of Score
        final Score newScore = Score.of(1, 10);

        // inserts the player to the database
        service.insertScore(playerUid, newScore)
                .get();

        // fetches the player from the database
        final Score expected = service.fetchScore(playerUid, 1)
                .get();

        // checks whether the player is present in the database
        assertTrue(service.isScorePresent(playerUid, 1)
                           .get());

        // checks whether the data matches with what we have given
        assertEquals(expected.getScoredOn(), 1);
        assertEquals(expected.getScore(), 10);

        // deletes the player from the database
        service.deleteScore(playerUid, 1)
                .get();

        // check whether the data is still present in the database
        assertFalse(service.isScorePresent(playerUid, 1)
                            .get());
    }

    @Test
    public void test_insert_is_present_fetch_delete_user_block_choice()
            throws ExecutionException, InterruptedException, InvalidServiceException, InvalidRepositoryException {
        final BaseStorage storage = new SQLiteStorage();
        final UserService mainService = ServiceFactoryMaker.makeFactory(
                ServiceFactoryMaker.ServiceType.DEFAULT,
                RepositoryFactoryMaker.RepositoryType.SQ_LITE,
                storage).createUserService();

        // initializes the process
        storage.init()
                .get();
        mainService.init()
                .get();

        final UserBlockChoiceService service = (UserBlockChoiceService) mainService;

        // our data
        final UUID playerUid = UUID.randomUUID();
        // creates a new instance of Score
        final Material blockChoice = Material.WOOL;

        // inserts the player to the database
        service.insertBlockChoice(playerUid, blockChoice)
                .get();

        // fetches the player from the database
        final Material expected = service.fetchBlockChoice(playerUid)
                .get();

        // checks whether the player is present in the database
        assertTrue(service.isBlockChoicePresent(playerUid)
                           .get());

        // checks whether the data matches with what we have given
        assertEquals(expected, Material.WOOL);

        // deletes the player from the database
        service.deleteBlockChoice(playerUid)
                .get();

        // check whether the data is still present in the database
        assertFalse(service.isBlockChoicePresent(playerUid)
                            .get());
    }
}