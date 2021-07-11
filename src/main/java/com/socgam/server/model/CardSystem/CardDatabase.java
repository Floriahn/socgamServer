package com.socgam.server.model.CardSystem;

import com.socgam.server.model.CardSystem.Effects.EffectDatabase;
import org.springframework.data.annotation.Id;

import java.util.List;

public class CardDatabase {
    // Unique card ID
    @Id
    private String cardID;

    // card attributes
    private String subtype;
    private String title;
    private CardTargetClass cardTargetClass;
    private int cost;
    private String effects;
    private int mainImageId;
    private int characterClassImageId;
    private Rarity rarity;
    private int level;
    private CharacterSpheres sphere;
    private String textbox;
    private int priority;
    private List<EffectDatabase> effectsList;

    // Constructors
    /*public CardDatabase(int ID, String title, int cost, List<EffectInterface> effects, String artwork, Rarity rarity, int level,
                CharacterSpheres cClass, String textbox) {
        this.cardID = ID;
        this.title = title;
        this.cost = cost;
        this.cardEffectList = effects;
        this.artwork = artwork;
        this.rarity = rarity;
        this.level = level;
        this.cClass = cClass;
        this.textbox = textbox;
    }*/

    // generic methods

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getMainImageId() {
        return mainImageId;
    }

    public void setMainImageId(int mainImageId) {
        this.mainImageId = mainImageId;
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

    public CharacterSpheres getSphere() {
        return sphere;
    }

    public void setSphere(CharacterSpheres sphere) {
        this.sphere = sphere;
    }

    public String getTextbox() {
        return textbox;
    }

    public void setTextbox(String textbox) {
        this.textbox = textbox;
    }

    public void setEffects(String effects) {
        this.effects = effects;
    }

    public String getEffects() {
        return effects;
    }

    public List<EffectDatabase> getEffectsList() {
        return effectsList;
    }

    public void setEffectsList(List<EffectDatabase> effectsList) {
        this.effectsList = effectsList;
    }

    public CardTargetClass getCardTargetClass() {
        return cardTargetClass;
    }

    public void setCardTargetClass(CardTargetClass cardTargetClass) {
        this.cardTargetClass = cardTargetClass;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCharacterClassImageId() {
        return characterClassImageId;
    }

    public void setCharacterClassImageId(int characterClassImageId) {
        this.characterClassImageId = characterClassImageId;
    }
}