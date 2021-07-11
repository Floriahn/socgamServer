package com.socgam.server.repository;

import com.socgam.server.model.Enemy;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EnemyRepository extends MongoRepository<Enemy, String> {
    public Optional<Enemy> findById(String id);
    public Optional<Enemy> findByName(String name);
}
