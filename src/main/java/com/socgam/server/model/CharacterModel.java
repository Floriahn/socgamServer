package com.socgam.server.model;

import java.util.LinkedList;
import java.util.List;

import com.socgam.server.model.CombatSystem.CONSTANTS;
import com.socgam.server.model.CombatSystem.CharacterClasses;

public class CharacterModel {
    private String name;
    private CharacterClasses charClass;
    private int level;
    private int vitality;
    private List<String> cardDeck;
    private int charIndex;

    public CharacterModel(String name, CharacterClasses charClass, int vitality, int charIndex) {
        this.name = name;
        this.charClass = charClass;
        this.level = 1;
        this.vitality = vitality;
        cardDeck = new LinkedList<>();
        this.charClass = charClass;
        this.charIndex = charIndex;
        
        String[] deckCards;
        switch (charClass){
            case MONK:
                deckCards = CONSTANTS.MONKDECK.split(",");
                break;
            case DRUID:
                deckCards = CONSTANTS.DRUIDDECK.split(",");
                break;
            case ARCANIST:
                deckCards = CONSTANTS.ARCANISTDECK.split(",");
                break;
            case MARTYR:
                deckCards = CONSTANTS.MARTYRDECK.split(",");
                break;
            case BERSERK:
                deckCards = CONSTANTS.BERSERKDECK.split(",");
                break;
            case CLERIC:
                deckCards = CONSTANTS.CLERICDECK.split(",");
                break;
            default:
                deckCards = new String[]{};
                break;
        }
        for(String s : deckCards) {
            cardDeck.add(s);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CharacterClasses getCharClass() {
        return charClass;
    }

    public void setCharClass(CharacterClasses charClass) {
        this.charClass = charClass;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVitality() {
        return vitality;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public List<String> getCardDeck() {
        return cardDeck;
    }

    public void setCharIndex(int charIndex) {
        this.charIndex = charIndex;
    }

    public void setCardDeck(List<String> cardDeck) {
        this.cardDeck = cardDeck;
    }

    public int getCharIndex() {
        return charIndex;
    }
}
