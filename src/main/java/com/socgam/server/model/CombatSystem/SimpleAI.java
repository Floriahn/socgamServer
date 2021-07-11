package com.socgam.server.model.CombatSystem;

import com.socgam.server.model.CardSystem.Card;
import com.socgam.server.model.CardSystem.CardTargetClass;

import java.util.*;

public class SimpleAI {
    public static Optional<Card> cardToPlay(Combatant player) {
        // Very simple method that returns the playable card with the highest priority (=closest to 0 value)
        Optional<Card> c;
        // Check energy
        int energy = player.getAttributeValue(Attributes.ENERGY);
        // Check cards in priority order
        Queue<Card> availableCards = player.getHandCards();
        c = availableCards.stream()
                .filter(ca -> ca.getCost() <= energy)
                .sorted(Comparator.comparingInt(Card::getPriority).reversed()).findFirst();

        return c;
    }

    public static Combatant chooseTarget(Card card, CombatInstance instance, Combatant self) {
        switch(card.getCardTargetClass()){
            case Self:
            case AllCombatants:
            case Ally:
            case AllAllies:
                return self;
            case Enemy:
            case AllEnemies:
                return instance.getCombatantByFaction(Faction.Players).get(0);
            default:
                return self;
        }
    }
}
