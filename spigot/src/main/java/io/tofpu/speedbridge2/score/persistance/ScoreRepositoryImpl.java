package io.tofpu.speedbridge2.score.persistance;

import io.tofpu.speedbridge2.score.domain.Score;
import io.tofpu.speedbridge2.score.domain.ScoreRepository;

import java.util.Collection;

public class ScoreRepositoryImpl implements ScoreRepository {
    private final ScoreDao dao;
    private final ScoreMapper mapper;

    public ScoreRepositoryImpl(ScoreDao dao, ScoreMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public void save(Score score) {
        ScoreEntity entity = mapper.toEntity(score);
        dao.save(entity);
    }

    @Override
    public boolean delete(Score score) {
        ScoreEntity entity = mapper.toEntity(score);
        return dao.delete(entity);
    }

    @Override
    public Collection<Score> findAll() {
        return dao.findAll().stream()
                .map(mapper::toScore)
                .toList();
    }
}
