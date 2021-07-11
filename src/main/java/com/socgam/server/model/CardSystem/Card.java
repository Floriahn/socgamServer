package com.socgam.server.model.CardSystem;

import com.socgam.server.model.CardSystem.Effects.Effect;
import com.socgam.server.model.CardSystem.Effects.EffectDatabase;
import com.socgam.server.model.CardSystem.Effects.EffectInterface;
import com.socgam.server.model.CombatSystem.*;
import com.socgam.server.model.CombatSystem.AttackModifier.AttackModifier;
import com.socgam.server.model.CombatSystem.SnapShotSystem.PlayCardActionSnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Card implements CardInterface {
    // Unique card ID

    private String cardID;

    // card attributes - Constructed
    private List<EffectInterface> cardEffectList;
    private CardTargetClass cardTargetClass;
    private int cost;
    private String title;
    private CharacterSpheres cClass;
    private Rarity rarity;
    private int level;
    private String textbox;
    private int artwork;
    // attributes - after play
    private CombatInstance context;
    private Combatant actor;
    private Combatant mainTarget;
    private Queue<Attack> nextAttacks;
    private List<AttackModifier> attackModifiers;
    private int priority;

    // Constructors
    public Card(String ID, String title, CardTargetClass cardTargetClass, int cost, List<EffectInterface> effects, int artwork, Rarity rarity, int level,
                CharacterSpheres cClass, String textbox, int priority) {
        this.cardID = ID;
        this.title = title;
        this.cardTargetClass = cardTargetClass;
        this.cost = cost;
        this.cardEffectList = effects;
        this.artwork = artwork;
        this.rarity = rarity;
        this.level = level;
        this.cClass = cClass;
        this.textbox = textbox;
        this.attackModifiers = new LinkedList<>();
        this.nextAttacks = new LinkedList<>();
        this.priority = priority;
    }

    public Card(CardDatabase cd){
        this.cardID = cd.getCardID();
        this.title = cd.getTitle();
        this.cardTargetClass = cd.getCardTargetClass();
        this.cost = cd.getCost();
        this.cardEffectList = new LinkedList<>();
        // Load all card effects and create effect objects
        for(EffectDatabase ed : cd.getEffectsList()){
            this.cardEffectList.add(Effect.loadEffect(ed));
        }
        this.artwork = cd.getMainImageId();
        this.rarity = cd.getRarity();
        this.level = cd.getLevel();
        this.cClass = cd.getSphere();
        this.textbox = cd.getTextbox();
        this.attackModifiers = new LinkedList<>();
        this.nextAttacks = new LinkedList<>();
        this.priority = cd.getPriority();
    }

    @Override
    public void playCard(CombatInstance context, Combatant actor) {
        this.actor = actor;
        this.context = context;
        //put card to discard pile
        actor.putCardToDiscardPile(this);
        // Pay energy costs
        actor.changeAttribute(Attributes.ENERGY, -cost);

        if(CONSTANTS.DEBUG) {
            System.out.println(actor + " plays Card: "+title+" for "+cost+" Energy");
            System.out.println(actor+" has "+actor.getAttributeValue(Attributes.ENERGY)+ " Energy");
        }
        // Execute all associated card effects
        for (EffectInterface effect : cardEffectList) {
            effect.setContext(context);
            effect.setActor(actor);
            effect.executeEffect(mainTarget, this, actor);
            //System.out.print(effect.toString() + " ");
        }
        // Execute card attacks if there are any
        if(!nextAttacks.isEmpty()) {
            for(Attack attack : nextAttacks) {
                // First add all modifiers to that attack
                for(AttackModifier attackModifier: attackModifiers) {
                    attackModifier.applyModifier(attack);
                }
                // Now execute the attack
                attack.executeAttack();
            }
        }
        context.addActionToQueue(new PlayCardActionSnapshot(context.getCombatantID(actor), context.getCombatantID(mainTarget), cardID));
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardID='" + cardID + '\'' +
                ", cardEffectList=" + cardEffectList +
                ", cost=" + cost +
                ", title='" + title + '\'' +
                ", cClass=" + cClass +
                ", rarity=" + rarity +
                ", level=" + level +
                ", textbox='" + textbox + '\'' +
                ", artwork=" + artwork +
                '}';
    }

    // generic methods
    public void addAttackModifier(AttackModifier attackModifier) {
        attackModifiers.add(attackModifier);
    }

    public void addAttack(Attack attack) {
        nextAttacks.add(attack);
    }

    public String getID() { return cardID;}

    public void addMainTarget(Combatant target) {
        mainTarget = target;
    }

    public int getPriority() { return priority;}

    public int getCost() { return cost;}

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public List<EffectInterface> getCardEffectList() {
        return cardEffectList;
    }

    public void setCardEffectList(List<EffectInterface> cardEffectList) {
        this.cardEffectList = cardEffectList;
    }

    public CardTargetClass getCardTargetClass() {
        return cardTargetClass;
    }

    public void setCardTargetClass(CardTargetClass cardTargetClass) {
        this.cardTargetClass = cardTargetClass;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CharacterSpheres getcClass() {
        return cClass;
    }

    public void setcClass(CharacterSpheres cClass) {
        this.cClass = cClass;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTextbox() {
        return textbox;
    }

    public void setTextbox(String textbox) {
        this.textbox = textbox;
    }

    public int getArtwork() {
        return artwork;
    }

    public void setArtwork(int artwork) {
        this.artwork = artwork;
    }

    public CombatInstance getContext() {
        return context;
    }

    public void setContext(CombatInstance context) {
        this.context = context;
    }

    public Combatant getActor() {
        return actor;
    }

    public void setActor(Combatant actor) {
        this.actor = actor;
    }

    public Combatant getMainTarget() {
        return mainTarget;
    }

    public void setMainTarget(Combatant mainTarget) {
        this.mainTarget = mainTarget;
    }

    public Queue<Attack> getNextAttacks() {
        return nextAttacks;
    }

    public void setNextAttacks(Queue<Attack> nextAttacks) {
        this.nextAttacks = nextAttacks;
    }

    public List<AttackModifier> getAttackModifiers() {
        return attackModifiers;
    }

    public void setAttackModifiers(List<AttackModifier> attackModifiers) {
        this.attackModifiers = attackModifiers;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
