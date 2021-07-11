package com.socgam.server.repository;

import com.socgam.server.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TeamRepository extends MongoRepository<Team, String> {
    public Optional<Team> findById(String id);
}
