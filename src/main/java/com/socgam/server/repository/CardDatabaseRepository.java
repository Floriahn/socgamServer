package com.socgam.server.repository;

import com.socgam.server.model.CardSystem.CardDatabase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CardDatabaseRepository extends MongoRepository<CardDatabase, String> {
    public Optional<CardDatabase> findById(String id);

}
