package io.tofpu.speedbridge2.database.user;

import io.tofpu.speedbridge2.database.user.repository.AbstractUserRepository;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;
import io.tofpu.speedbridge2.sql.table.RepositoryTable;
import io.tofpu.speedbridge2.sql.table.SQLTableUtil;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DefaultUserService implements UserService {
    private final AbstractUserRepository userRepository;

    public DefaultUserService(final AbstractUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CompletableFuture<Void> init() {
        final RepositoryTable table = userRepository.getTable();
        final BaseStorage storage = userRepository.getStorage();
        return storage.asyncThreadExecutor()
                .runAsync(() -> storage.execute(SQLTableUtil.tableAsSQLFormat(table)));
    }

    @Override
    public CompletableFuture<BridgePlayer> fetch(final UUID key) {
        return userRepository.fetch(key);
    }

    @Override
    public CompletableFuture<Boolean> isPresent(final UUID key) {
        return userRepository.isPresent(key);
    }

    @Override
    public CompletableFuture<Void> insert(final UUID key, final BridgePlayer obj) {
        return userRepository.insert(key, obj);
    }

    @Override
    public CompletableFuture<Void> delete(final UUID key) {
        return userRepository.delete(key);
    }
}
