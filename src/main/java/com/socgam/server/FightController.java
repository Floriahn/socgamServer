package com.socgam.server;

import com.socgam.server.model.CardSystem.Card;
import com.socgam.server.model.CardSystem.CardDatabase;
import com.socgam.server.model.CardSystem.Effects.EffectDatabase;
import com.socgam.server.model.CharacterModel;
import com.socgam.server.model.CombatSystem.CombatInstance;
import com.socgam.server.model.CombatSystem.Combatant;
import com.socgam.server.model.CombatSystem.Faction;
import com.socgam.server.model.CombatSystem.SnapShotSystem.CombatSnapshot;
import com.socgam.server.model.Enemy;

import com.socgam.server.model.Team;
import com.socgam.server.model.User;
import com.socgam.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class FightController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CardDatabaseRepository cardDatabaseRepository;

    @Autowired
    EffectDatabaseRepository effectDatabaseRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    EnemyRepository enemyRepository;

    public final Map<String, CombatInstance> combatInstances = new HashMap<>();

    @PostMapping("fight/initiate/{id}/{type}")
    public void initiateFight(@PathVariable String id, @PathVariable String type){
        System.out.println("Initiate Fight");
        List<User> users = new LinkedList<>();
        // create combat instance object
        // create combatant objects
        // load decks
        // add combatants

        User u = userRepository.findById(id).get();


        users.add(u);

        if(u.getTeamId() != null){
            //team fight
            Team t = teamRepository.findById(u.getTeamId()).get();
            for(String p_id : t.getPlayerIds()){
                if(!p_id.equals(u.getId())){
                    users.add(userRepository.findById(p_id).get());
                }
            }
        }

        String uuid = "";
        do{
            uuid = UUID.randomUUID().toString();
        }while (combatInstances.containsKey(uuid));

        CombatInstance fight = new CombatInstance(uuid);

        //List<Combatant> players = users.stream().map(user -> new Combatant(Faction.Players, user.getActiveCharacter().getCharClass(), user.getActiveCharacter().getVitality())).collect(Collectors.toList());
        List<Combatant> combatants = new LinkedList<>();
        for(User user : users){
            user.setActiveFightId(uuid);
            userRepository.save(user);
            CharacterModel characterModel = user.getActiveCharacter();

            Combatant c = new Combatant(Faction.Players, characterModel.getCharClass(), characterModel.getVitality(), fight, user.getId(), user.getUsername());

            c.loadDeck(getDeck(characterModel.getCardDeck()));
            fight.addCombatant(c);
        }



        String[] enemies = type.split(",");
        //Gegner Combatant erstellen
        //Gegner Deck laden
        //Combatant zum fight hinzufügen
        int index = 0;
        for(String s : enemies){
            Enemy e = enemyRepository.findByName(s).get();
            Combatant enemyC = new Combatant(Faction.Enemies, e, fight, "e"+index, e.getName());
            index++;
            enemyC.loadDeck(getDeck(Arrays.asList(e.getDeck().split(","))));
            fight.addCombatant(enemyC);
        }

        combatInstances.put(uuid, fight);
        fight.startCombatLoop();
    }

    @PostMapping("fight/playCard/{userId}/{cardId}/{targetId}")
    public void playCard(@PathVariable String cardId, @PathVariable String targetId, @PathVariable String userId){
        CombatInstance fight = combatInstances.get(userRepository.findById(userId).get().getActiveFightId());
        fight.addPlayedCard(cardId, userId, targetId);
    }

    @PostMapping("fight/pass/{userId}")
    public void passTurn(@PathVariable String userId){
        CombatInstance fight = combatInstances.get(userRepository.findById(userId).get().getActiveFightId());
        fight.combatantHasPassed(userId);
    }

    private LinkedList<Card> getDeck(List<String> cardIDs){
        LinkedList<Card> deck = new LinkedList<>();
        for(String s : cardIDs){
            CardDatabase cd = cardDatabaseRepository.findById(s).get();
            List<EffectDatabase> effects = (List<EffectDatabase>) Arrays.stream(cd.getEffects().split(",")).map(effectString -> getEffectbyId(effectString)).collect(Collectors.toList());
            cd.setEffectsList(effects);
            Card c = new Card(cd);
            deck.add(c);
        }
        return deck;
    }

    private EffectDatabase getEffectbyId(String id){
        EffectDatabase ed = effectDatabaseRepository.findById(id).get();
        return ed;
    }

    /*@GetMapping("fight/getCombatSnapshot/{userId}/{LastSeqNumber}")
    public Optional<CombatSnapshot> combatSnapshot(@PathVariable String userId, @PathVariable String LastSeqNumber){
        CombatInstance fight = combatInstances.get(userRepository.findById(userId).get().getActiveFightId());
        Optional<CombatSnapshot> cs = fight.retrieveSnapShot(Integer.parseInt(LastSeqNumber));
        return cs;
    }*/

    @GetMapping("fight/getCombatSnapshot/{combatId}/{LastSeqNumber}")
    public CombatSnapshot getCombatSnapshot(@PathVariable String combatId, @PathVariable String LastSeqNumber){
        CombatInstance fight = combatInstances.get(combatId);
        Optional<CombatSnapshot> cs = fight.retrieveSnapShot(Integer.parseInt(LastSeqNumber));
        return cs.orElse(null);
    }
}