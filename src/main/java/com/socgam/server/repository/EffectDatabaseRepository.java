package com.socgam.server.repository;

import com.socgam.server.model.CardSystem.Effects.EffectDatabase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EffectDatabaseRepository extends MongoRepository<EffectDatabase, String> {
    public Optional<EffectDatabase> findById(String id);

}
