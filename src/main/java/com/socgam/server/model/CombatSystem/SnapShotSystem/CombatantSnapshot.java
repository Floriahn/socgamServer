package com.socgam.server.model.CombatSystem.SnapShotSystem;

import com.socgam.server.model.CardSystem.Card;
import com.socgam.server.model.CombatSystem.Attributes;
import com.socgam.server.model.CombatSystem.CharacterClasses;
import com.socgam.server.model.CombatSystem.Combatant;
import com.socgam.server.model.CombatSystem.Faction;

import java.sql.Timestamp;
import java.util.*;

import static java.lang.System.currentTimeMillis;

public class CombatantSnapshot {
    // Attributes
    private final String userID;
    private final String name;
    private final Faction faction;
    private final Timestamp timestamp;
    //private final CombatSnapshot parent;
    private final Map<Attributes,Integer> attributesMap;
    private final boolean isDead;
    private final List<String> handCards;

    // Constructor
    public CombatantSnapshot(Combatant combatant, CombatSnapshot cs) {
        this.userID = combatant.getUserId();
        this.name = combatant.getName();
        this.faction = combatant.getFaction();
        //this.parent = cs;
        this.timestamp = new Timestamp(currentTimeMillis());
        this.attributesMap = new HashMap<>();
        for(Attributes a : Attributes.values()) {
            this.attributesMap.put(a, combatant.getAttributeValue(a));
        }
        isDead = combatant.isDead();
        handCards = new LinkedList<>();
        for (Card handcard : combatant.getHandCards()) {
            handCards.add(handcard.getID());
        }
    }

    // Getters

    public String getUserID() {
        return userID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    /*public CombatSnapshot getParent() {
        return parent;
    } */

    public Map<Attributes, Integer> getAttributesMap() {
        return attributesMap;
    }

    public boolean isDead() {
        return isDead;
    }

    public List<String> getHandCards() {
        return handCards;
    }

    public Faction getFaction() {
        return faction;
    }

    public String getName() {
        return name;
    }
}
