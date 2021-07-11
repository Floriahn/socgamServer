package com.socgam.server;

import com.socgam.server.model.Enemy;
import com.socgam.server.repository.EnemyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnemyController {

    @Autowired
    EnemyRepository enemyRepository;

    @GetMapping("enemy/getByName/{name}")
    public Enemy getEnemyByName(@PathVariable String name){
        return enemyRepository.findByName(name).get();
    }

}
