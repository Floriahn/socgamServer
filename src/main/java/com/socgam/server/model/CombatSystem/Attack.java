package com.socgam.server.model.CombatSystem;

public class Attack {
    // Attributes - Basic
    private boolean isPhysical; // Can either be a physical or a magical attack
    private int baseValue;
    private Combatant attacker;
    private Combatant defender;

    // Attributes - Added through Attack modifiers
    private int staticChange;
    private int dynamicChange; // in parts per 100
    private int armorModifier;

    // Constructors
    public Attack(boolean isPhysical, int baseValue, Combatant attacker, Combatant defender){
        this.isPhysical = isPhysical;
        this.baseValue = baseValue;
        this.attacker = attacker;
        this.defender = defender;
        staticChange = 0;
        dynamicChange = 100;
        armorModifier = 0;
    }

    // Methods
    public void executeAttack() {
        // Add attacker static attack modifier
        if(isPhysical) {
            staticChange += attacker.getAttributeValue(Attributes.PHYSICALDAMAGE);
        } else {
            staticChange += attacker.getAttributeValue(Attributes.MAGICALDAMAGE);
        }
        // Clamp dynamic modifier value
        if(dynamicChange < 0) {dynamicChange = 0;}
        // Calculate resulting attack value
        int attackValue = ((baseValue + staticChange) * dynamicChange / 100);
        // Calculate Armor
        int armor;
        if(isPhysical) {
            armor = defender.getAttributeValue(Attributes.ARMOR);
        } else {
            armor = defender.getAttributeValue(Attributes.ABSORPTION);
        }
        armor -= armorModifier;
        if(armor < 0) armor = 0; // armor can not be lower than 0
        // Calculate damage
        int damage = attackValue - armor;
        if(damage<0) damage = 0;
        defender.takeDamage(damage);
        if(CONSTANTS.DEBUG) {
            System.out.println(attacker + " attacked " + defender + " for " + damage + " damage");
            System.out.println("Base attack:" + baseValue + " Static:" + staticChange + " Dynamic:" + dynamicChange + " Armor:" + armor);
        }
    }

    // Public Modifications
    public void addStaticChange(int staticChange) {
        this.staticChange += staticChange;
    }

    public void addDynamicChange(int dynamicChange) {
        this.dynamicChange += dynamicChange;
    }

    public void addArmorModifier(int armorModifier) {
        this.armorModifier += armorModifier;
    }
}
