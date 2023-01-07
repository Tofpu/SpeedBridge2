package io.tofpu.speedbridge2.service.user;

import io.tofpu.speedbridge2.database.repository.user.factory.UserRepositoryFactory;
import io.tofpu.speedbridge2.database.repository.user.block.AbstractUserBlockChoiceRepository;
import io.tofpu.speedbridge2.database.repository.user.block.UserBlockChoiceService;
import io.tofpu.speedbridge2.database.repository.user.name.AbstractUserNameRepository;
import io.tofpu.speedbridge2.database.repository.user.name.UserNameService;
import io.tofpu.speedbridge2.database.repository.user.score.AbstractUserScoreRepository;
import io.tofpu.speedbridge2.database.repository.user.score.UserScoreService;
import io.tofpu.speedbridge2.database.repository.user.score.key.ScoreUUID;
import io.tofpu.speedbridge2.model.player.object.score.Score;
import io.tofpu.speedbridge2.repository.TableBaseRepository;
import io.tofpu.speedbridge2.sql.table.SQLTableUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DefaultUserService implements UserService, UserNameService, UserScoreService, UserBlockChoiceService {
    private final AbstractUserNameRepository userNameRepository;
    private final AbstractUserScoreRepository userScoreRepository;
    private final AbstractUserBlockChoiceRepository userBlockChoiceRepository;

    public DefaultUserService(final UserRepositoryFactory userRepositoryFactory) {
        this.userNameRepository = userRepositoryFactory.nameRepository();
        this.userScoreRepository = userRepositoryFactory.scoreRepository();
        this.userBlockChoiceRepository = userRepositoryFactory.blockChoiceRepository();
    }

    @Override
    public CompletableFuture<Void> init() {
        boolean haveInitializedAtLeastOnce = false;

        final List<TableBaseRepository<?, ?>> repositories =
                Arrays.asList(userNameRepository, userScoreRepository, userBlockChoiceRepository);

        final List<CompletableFuture<Void>> listOfRepositoryFutures = new ArrayList<>(repositories.size());
        for (final TableBaseRepository<?, ?> repository : repositories) {
            if (repository == null) {
                continue;
            }
            haveInitializedAtLeastOnce = true;
            listOfRepositoryFutures.add(
                    repository.getStorage().executeAsync(SQLTableUtil.tableAsSQLFormat(repository.getTable())));
        }

        if (!haveInitializedAtLeastOnce) {
            throw new IllegalStateException("No repositories have been initialized");
        }

        return CompletableFuture.allOf(listOfRepositoryFutures.toArray(new CompletableFuture[0]));
    }

    @Override
    public CompletableFuture<String> fetchName(final UUID key) {
        return userNameRepository.fetch(key);
    }

    @Override
    public CompletableFuture<Boolean> isNamePresent(final UUID key) {
        return userNameRepository.isPresent(key);
    }

    @Override
    public CompletableFuture<Void> insertName(final UUID key, final String obj) {
        return userNameRepository.insert(key, obj);
    }

    @Override
    public CompletableFuture<Void> deleteName(final UUID key) {
        return userNameRepository.delete(key);
    }

    @Override
    public CompletableFuture<Score> fetchScore(final UUID key, final int islandSlot) {
        return userScoreRepository.fetch(ScoreUUID.of(key, islandSlot));
    }

    @Override
    public CompletableFuture<Boolean> isScorePresent(final UUID key, final int islandSlot) {
        return userScoreRepository.isPresent(ScoreUUID.of(key, islandSlot));
    }

    @Override
    public CompletableFuture<Void> insertScore(final UUID key, final Score score) {
        return userScoreRepository.insert(ScoreUUID.of(key, -1), score);
    }

    @Override
    public CompletableFuture<Void> deleteScore(final UUID key, final int islandSlot) {
        return userScoreRepository.delete(ScoreUUID.of(key, islandSlot));
    }

    @Override
    public CompletableFuture<Void> insertBlockChoice(final UUID key, final Material value) {
        return userBlockChoiceRepository.insert(key, value.name());
    }

    @Override
    public CompletableFuture<Material> fetchBlockChoice(final UUID key) {
        return userBlockChoiceRepository.fetch(key).thenApply(s -> {
            if (s == null) {
                return null;
            }
            return Material.matchMaterial(s);
        });
    }

    @Override
    public CompletableFuture<Boolean> isBlockChoicePresent(final UUID key) {
        return userBlockChoiceRepository.isPresent(key);
    }

    @Override
    public CompletableFuture<Void> deleteBlockChoice(final UUID key) {
        return userBlockChoiceRepository.delete(key);
    }
}
