package com.socgam.server;

import com.socgam.server.ControllerArrays.CardDatabaseList;
import com.socgam.server.model.CardSystem.Card;
import com.socgam.server.model.CardSystem.CardDatabase;
import com.socgam.server.model.CardSystem.Effects.*;
import com.socgam.server.model.TradeOffer;
import com.socgam.server.model.User;
import com.socgam.server.repository.CardDatabaseRepository;
import com.socgam.server.repository.EffectDatabaseRepository;
import com.socgam.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CardController {

    @Autowired
    CardDatabaseRepository cardDatabaseRepository;

    @Autowired
    EffectDatabaseRepository effectDatabaseRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("card/getCard/{id}")
    public CardDatabase getCard(@PathVariable String id){
        Optional<CardDatabase> optional = cardDatabaseRepository.findById(id);
        if(optional.isEmpty()){
            System.out.println(id + " aaa");
            return null;
        }
        CardDatabase cd = optional.get();
        List<EffectDatabase> effects = (List<EffectDatabase>) Arrays.stream(cd.getEffects().split(",")).map(effectString -> getEffectbyId(effectString)).collect(Collectors.toList());
        cd.setEffectsList(effects);

        return cd;
    }

    @GetMapping("card/getCardsList/{idsString}")
    public CardDatabaseList getCardsList(@PathVariable String idsString){
        String[] ids = idsString.split(",");
        List<CardDatabase> list = new LinkedList<>();
        for (String id : ids){
            CardDatabase cd = cardDatabaseRepository.findById(id).get();
            List<EffectDatabase> effects = (List<EffectDatabase>) Arrays.stream(cd.getEffects().split(",")).map(effectString -> getEffectbyId(effectString)).collect(Collectors.toList());
            cd.setEffectsList(effects);
            list.add(cd);
        }
        return new CardDatabaseList(list);
    }

    @GetMapping("card/getDeck/{id}")
    public String getDeck(@PathVariable String id){
        User u = userRepository.findById(id).get();
        return String.join(",", u.getActiveCharacter().getCardDeck());
    }

    @PostMapping("card/setDeck/{id}/{deck}")
    public void cardSetDeck(@PathVariable String id, @PathVariable String deck){
        User u = userRepository.findById(id).get();
        u.getActiveCharacter().setCardDeck(Arrays.stream(deck.split(",")).collect(Collectors.toList()));
        userRepository.save(u);
    }

    @GetMapping("card/getCollectionWithoutActiveDeck/{id}")
    public String getCollectionWithoutActiveDeck(@PathVariable String id){
        User u = userRepository.findById(id).get();
        List<String> collection = u.getCardCollection();
        for(String cid : u.getActiveCharacter().getCardDeck()){
            collection.remove(cid);
        }
        return String.join(",", collection);
    }

    @GetMapping("card/getDeckAsCardDatabases/{id}")
    public CardDatabaseList getDeckAsCardDatabases(@PathVariable String id){
        return getCardsList(getDeck(id));
    }

    @GetMapping("card/getCollectionWithoutActiveDeckAsCardDatabases/{id}")
    public CardDatabaseList getCollectionWithoutActiveDeckAsCardDatabases(@PathVariable String id){
        return getCardsList(getCollectionWithoutActiveDeck(id));
    }

    @GetMapping("effect/getAllEffects")
    public List<EffectDatabase> getAllEffects(){
        return effectDatabaseRepository.findAll();
    }

    private EffectDatabase getEffectbyId(String id){
        EffectDatabase ed = effectDatabaseRepository.findById(id).get();
        /*switch (ed.getSubtype()){
            case changeAttribute:
                return new ChangeAttributeEffect(ed.getEffectId(),ed.getTargetClass(),ed.getBaseValue(),ed.getAttribute());
            case modifyAttack:
                return new ModifyAttackEffect(ed.getEffectId(), ed.getBaseValue(),ed.getAttackModifier());
            case status:
                return new CreateStatusEffect(ed.getEffectId(), ed.getTargetClass(), ed.getAttribute(), ed.getBaseValue(), ed.getDuration());
            case attack:
                return new AttackActionEffect(ed.getEffectId(), ed.getTargetClass(), ed.isPhysical(), ed.getBaseValue());
        }*/
        return ed;
    }

    @GetMapping("effect/getAllCards")
    public CardDatabaseList getAllCards(){
        return new CardDatabaseList(cardDatabaseRepository.findAll());
    }


    @GetMapping("card/getRandomCard/{id}")
    public CardDatabase getRandomCard(@PathVariable String id){
        List<CardDatabase> cards = cardDatabaseRepository.findAll();
        Random rand = new Random();
        while (true){
            CardDatabase cd = cards.get(rand.nextInt(cards.size()));
            if(cd.getSubtype() != null && cd.getSubtype().equals("player")){
                User u = userRepository.findById(id).get();
                u.getCardCollection().add(cd.getCardID());
                userRepository.save(u);
                return cd;
            }
        }
    }

    @PostMapping("trade/makeOffer/{fromId}/{toId}/{fromCardId}/{toCardId}")
    public void makeOffer(@PathVariable String fromId, @PathVariable String toId, @PathVariable String fromCardId, @PathVariable String toCardId){
        User u = userRepository.findById(fromId).get();
        User to = userRepository.findById(toId).get();

        TradeOffer offer = new TradeOffer(fromId, toId, fromCardId, toCardId);

        u.setTradeOffers(offer);
        to.setTradeOffers(offer);

        userRepository.save(to);
        userRepository.save(u);
    }

    @PostMapping("trade/answerOffer/{userId}/{cardId}")
    public void answerOffer(@PathVariable String cardId, @PathVariable String userId){
        User u = userRepository.findById(userId).get();
        TradeOffer offer = u.getTradeOffers();
        User offerer = userRepository.findById(offer.getCardFromId()).get();
        offer.setCardToId(cardId);
        u.setTradeOffers(offer);
        offerer.setTradeOffers(offer);
        userRepository.save(u);
        userRepository.save(offerer);
    }

    @PostMapping("trade/confirmOffer/{userId}")
    public void confirmOffer(@PathVariable String userId){
        User u = userRepository.findById(userId).get();
        TradeOffer offer = u.getTradeOffers();
        User offerer = userRepository.findById(offer.getFromId()).get();

        u.getCardCollection().remove(offer.getCardFromId());
        u.getCardCollection().add(offer.getCardToId());
        offerer.getCardCollection().remove(offer.getCardToId());
        offerer.getCardCollection().add(offer.getCardFromId());

        u.setTradeState(2);
        offerer.setTradeState(2);

        userRepository.save(u);
        userRepository.save(offerer);
    }

    @PostMapping("trade/cancelOffer/{userId}")
    public void cancelOffer(@PathVariable String userId){
        User u = userRepository.findById(userId).get();
        TradeOffer offer = u.getTradeOffers();
        Optional<User> optional = userRepository.findById(offer.getFromId());
        if(optional.isEmpty()){
            System.out.println(userId);
            System.out.println(offer.getFromId());
            return;
        }
        User offerer = optional.get();
        u.setTradeState(1);
        offerer.setTradeState(1);

        userRepository.save(u);
        userRepository.save(offerer);
    }

    @PostMapping("trade/resetState/{userId}")
    public void resetState(@PathVariable String userId){
        User u = userRepository.findById(userId).get();
        u.setTradeState(0);
        u.setTradeOffers(null);
        userRepository.save(u);
    }

}