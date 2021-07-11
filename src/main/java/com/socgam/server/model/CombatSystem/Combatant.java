package com.socgam.server.model.CombatSystem;

import com.socgam.server.model.CardSystem.Card;
import com.socgam.server.model.CombatSystem.SnapShotSystem.CombatSnapshot;
import com.socgam.server.model.CombatSystem.SnapShotSystem.CombatantSnapshot;
import com.socgam.server.model.CombatSystem.SnapShotSystem.DrawCardActionSnapshot;
import com.socgam.server.model.CombatSystem.Status.Status;
import com.socgam.server.model.Enemy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.socgam.server.model.CombatSystem.StandardValues.getStandardAttributeValue;

public class Combatant {

    // Attributes
    private Map<Attributes, modifiableAttribute> modifiableAttributesMap;
    private Faction faction;
    private CombatInstance currentCombatInstance;
    private List<Status> statuses;
    private boolean isDead;
    private int MAXHANDSIZE = 8; // UI restriction
    private boolean hasPassed;

    // ClientCommunication
    // ToDO:
    private String userID;
    private String name;
    //private Integer combatantID;
    private Timestamp lastUpdate;
    private final Object lock = new Object();

    // CardDeck (all queues should be implemented as linkedList)
    private List<String> cardDeckAsCardIDs;
    private Queue<Card> drawDeck;
    private Queue<Card> discardPile;
    private Queue<Card> handCards;
    private Queue<Card> playedCards;


    // Constructor
    public Combatant(Faction faction, CharacterClasses charClass, int vitality, CombatInstance combatInstance, String id, String name) {
        // modAttributes
        synchronized(lock) {
            modifiableAttributesMap = new EnumMap<Attributes, modifiableAttribute>(Attributes.class);
            modifiableAttributesMap.put(Attributes.VITALITY, new modifiableAttribute("Vitalität", vitality));
            modifiableAttributesMap.put(Attributes.HEALTH, new modifiableAttribute("Gesundheit", vitality, vitality));
            int physicalDamage = getStandardAttributeValue(charClass, Attributes.PHYSICALDAMAGE);
            modifiableAttributesMap.put(Attributes.PHYSICALDAMAGE, new modifiableAttribute("Waffenschaden", physicalDamage));
            int magicalDamage = getStandardAttributeValue(charClass, Attributes.MAGICALDAMAGE);
            modifiableAttributesMap.put(Attributes.MAGICALDAMAGE, new modifiableAttribute("Zauberschaden", magicalDamage));
            int armor = getStandardAttributeValue(charClass, Attributes.ARMOR);
            modifiableAttributesMap.put(Attributes.ARMOR, new modifiableAttribute("Rüstung", armor));
            int absorption = getStandardAttributeValue(charClass, Attributes.ABSORPTION);
            modifiableAttributesMap.put(Attributes.ABSORPTION, new modifiableAttribute("Absorption", absorption));
            int energy = getStandardAttributeValue(charClass, Attributes.ENERGY);
            modifiableAttributesMap.put(Attributes.ENERGY, new modifiableAttribute("Energie", 0, energy));
            modifiableAttributesMap.put(Attributes.MAXENERGY, new modifiableAttribute("Maximale Energie", energy));
            int energyGain = getStandardAttributeValue(charClass, Attributes.ENERGYGAIN);
            modifiableAttributesMap.put(Attributes.ENERGYGAIN, new modifiableAttribute("Energie pro Runde", energyGain));
            int carddraw = getStandardAttributeValue(charClass, Attributes.CARDDRAW);
            modifiableAttributesMap.put(Attributes.CARDDRAW, new modifiableAttribute("Karten pro Runde", carddraw));
            int regeneration = getStandardAttributeValue(charClass, Attributes.REGENERATION);
            modifiableAttributesMap.put(Attributes.REGENERATION, new modifiableAttribute("Heilung pro Runde", regeneration));
        }


        this.faction = faction;
        this.statuses = new LinkedList<Status>();
        this.currentCombatInstance = combatInstance;
        this.userID = id;
        this.name = name;
        drawDeck = new LinkedList<>();
        discardPile = new LinkedList<>();
        handCards = new LinkedList<>();
        playedCards = new LinkedList<>();
        cardDeckAsCardIDs = new LinkedList();

        // Subscribe Health
        isDead = false;
        PropertyChangeListener healthListener = new healthChangeListener();
        getAttribute(Attributes.HEALTH).addPropertyChangeListener(healthListener);

        // Subscribe Vitality so that max Health is automatically adjusted to Vitality
        PropertyChangeListener vitalityListener = new vitalityChangeListener();
        getAttribute(Attributes.VITALITY).addPropertyChangeListener(vitalityListener);
    }

    public Combatant(Faction faction, Enemy e, CombatInstance combatInstance, String id, String name) {
        // modAttributes
        synchronized(lock) {
            modifiableAttributesMap = new EnumMap<Attributes, modifiableAttribute>(Attributes.class);
            modifiableAttributesMap.put(Attributes.VITALITY, new modifiableAttribute("Vitalität", e.getVitality()));
            modifiableAttributesMap.put(Attributes.HEALTH, new modifiableAttribute("Gesundheit", e.getVitality(), e.getVitality()));
            modifiableAttributesMap.put(Attributes.PHYSICALDAMAGE, new modifiableAttribute("Waffenschaden", e.getPhysicalDamage()));
            modifiableAttributesMap.put(Attributes.MAGICALDAMAGE, new modifiableAttribute("Zauberschaden", e.getMagicalDamage()));
            modifiableAttributesMap.put(Attributes.ARMOR, new modifiableAttribute("Rüstung", e.getArmor()));
            modifiableAttributesMap.put(Attributes.ABSORPTION, new modifiableAttribute("Absorption", e.getAbsorption()));
            modifiableAttributesMap.put(Attributes.ENERGY, new modifiableAttribute("Energie", 0, e.getMaxEnergy()));
            modifiableAttributesMap.put(Attributes.MAXENERGY, new modifiableAttribute("Maximale Energie", e.getMaxEnergy()));
            modifiableAttributesMap.put(Attributes.ENERGYGAIN, new modifiableAttribute("Energie pro Runde", e.getEnergyGain()));
            modifiableAttributesMap.put(Attributes.CARDDRAW, new modifiableAttribute("Karten pro Runde", e.getCardDraw()));
            modifiableAttributesMap.put(Attributes.REGENERATION, new modifiableAttribute("Heilung pro Runde", e.getRegeneration()));
        }


        this.faction = faction;
        this.statuses = new LinkedList<Status>();
        this.currentCombatInstance = combatInstance;
        this.userID = id;
        this.name = name;

        drawDeck = new LinkedList<>();
        discardPile = new LinkedList<>();
        handCards = new LinkedList<>();
        playedCards = new LinkedList<>();
        cardDeckAsCardIDs = new LinkedList();

        // Subscribe Health
        isDead = false;
        PropertyChangeListener healthListener = new healthChangeListener();
        getAttribute(Attributes.HEALTH).addPropertyChangeListener(healthListener);

        // Subscribe Vitality so that max Health is automatically adjusted to Vitality
        PropertyChangeListener vitalityListener = new vitalityChangeListener();
        getAttribute(Attributes.VITALITY).addPropertyChangeListener(vitalityListener);
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Queue<Card> getHandCards() {
        synchronized (handCards) {
            return handCards;
        }
    }

    public String getUserId() {
        return userID;
    }

    public class healthChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            checkDeath();
        }
    }

    public class vitalityChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            int newMax = (int) evt.getNewValue();
            synchronized (lock) {
                getAttribute(Attributes.HEALTH).setMaxValue(newMax);
            }
        }
    }


    // Public Methods
    private modifiableAttribute getAttribute(Attributes attribute) {
        return modifiableAttributesMap.get(attribute);
    }

    public void modifyAttribute(Attributes attribute, int modValue) {
        synchronized (lock) {
            getAttribute(attribute).increaseModifier(modValue);
        }
    }

    public void changeAttribute(Attributes attribute, int value) {
        synchronized (lock) {
            getAttribute(attribute).changeAttribute(value);
        }
    }

    public int getAttributeValue(Attributes attribute) {
        synchronized (lock) {
            return getAttribute(attribute).getValue();
        }
    }

    public CombatInstance getCombatContext() {
        return currentCombatInstance;
    }

    public void takeDamage(int damage) {
        synchronized(lock) {
            modifiableAttributesMap.get(Attributes.HEALTH).changeAttribute(-damage);
        }
    }

    public void addStatus(Status status) {
        statuses.add(status);
        if(CONSTANTS.DEBUG) {
            System.out.println("Added status: "+status+" to "+this);
        }
    }

    public void removeStatus(Status status) {
        statuses.remove(status);
        if(CONSTANTS.DEBUG) {
            System.out.println("Removed status: "+status+" to "+this);
        }
    }

    private void checkDeath(){
        int value = modifiableAttributesMap.get(Attributes.HEALTH).getValue();
        if(value <= 0) {
            synchronized (lock){
                isDead = true;
            }
            // ToDo: Make this combatant so that other combatants cannot interact with it once is dead
        }
    }

    private void drawSingleCard() {
        if(CONSTANTS.DEBUG) {
            System.out.println(this+" draws a card");
        }
        synchronized (lock) {
            if(handCards.size() < MAXHANDSIZE) {
                Card card = drawDeck.poll();
                handCards.add(card);
                currentCombatInstance.addActionToQueue(new DrawCardActionSnapshot(this.userID));
                if(drawDeck.isEmpty()) {
                    resetDrawDeck();
                }
            } else {
                // ToDo: maybe give feedback here
            }
        }
    }

    private void resetDrawDeck() {
        synchronized (lock) {
            drawDeck = discardPile;
            discardPile = new LinkedList<>();
            shuffleDrawDeck();
        }
    }

    private void shuffleDrawDeck() {
        Collections.shuffle((List)drawDeck);
    }

    public void playCard(Card card, Combatant target) {
        // ToDo:
    }

    public Faction getFaction() {
        return faction;
    }

    public void drawRoundCards() {
        int cardDraw = getAttribute(Attributes.CARDDRAW).getValue();
        for(int i = 0; i < cardDraw; i++) drawSingleCard();
        return;
    }

    public void gainRoundEnergy() {
        synchronized (lock) {
            int energyGained = getAttribute(Attributes.ENERGYGAIN).getValue();
            getAttribute(Attributes.ENERGY).changeAttribute(energyGained);
            if(CONSTANTS.DEBUG) {
                System.out.println(this+" receives "+energyGained+" energy to a value of "+getAttributeValue(Attributes.ENERGY));
            }
            return;
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public CombatantSnapshot createSnapshot(CombatSnapshot cs) {
        synchronized(lock) {
            return new CombatantSnapshot(this, cs);
        }
    }

    public boolean hasPassed() {
        return hasPassed;
    }

    public void pass(boolean hasPassed) {
        this.hasPassed = hasPassed;
    }

    public Optional<Card> retrievePlayedCard() {
        synchronized (playedCards) {
            Card c = playedCards.poll();
            Optional r;
            if (c == null) {
                r = Optional.empty();
            } else {
                r = Optional.of(c);
            }
            return r;
        }
    }

    public void addPlayedCard(Combatant target, String cardID) {
        // Extract handcard
        Optional<Card> card = handCards.stream().filter(c -> c.getID().equals(cardID)).findFirst();
        if(card.isPresent()) {
            // Add target to handcard
            card.get().addMainTarget(target);
            // Add card to playedQueue
            synchronized (playedCards) {
                playedCards.add(card.get());
            }
            // Remove card from handcards
            synchronized (handCards) {
                handCards.remove(card.get());
            }
        }
    }

    public void loadDeck(Queue<Card> drawDeck) {
        this.drawDeck = drawDeck;
        for(Card c : drawDeck) {
            cardDeckAsCardIDs.add(c.getCardID());
        }
        shuffleDrawDeck();
    }

    public void decreaseStatusesTTL() {
        synchronized (lock) {
            Iterator<Status> i = statuses.iterator();
            while (i.hasNext()) {
               Status s = i.next(); // must be called before you can call i.remove()
                s.decreaseTTL();
                if(s.getTTL()<=0) {
                    s.removeStatus();
                    i.remove();
                }
            }
        }
    }

    public List<String> getDeckAsCardIDs() {
        return cardDeckAsCardIDs;
    }

    public String getDeckAsOneString() {
        return String.join(",", cardDeckAsCardIDs);
    }

    public String getName() {
        return name;
    }

    public void putCardToDiscardPile(Card card){
        synchronized (lock){
            discardPile.add(card);
            handCards.remove(card);
        }
    }

    @Override
    public String toString() {
        return "C:'" + name + "'";
    }

    public void regenerate() {
        synchronized (lock) {
            changeAttribute(Attributes.HEALTH,getAttributeValue(Attributes.REGENERATION));
            if(CONSTANTS.DEBUG) {
                System.out.println(this+" regenerates "+getAttributeValue(Attributes.REGENERATION)+", now at "+getAttributeValue(Attributes.HEALTH)+" health");
            }
        }
    }
}