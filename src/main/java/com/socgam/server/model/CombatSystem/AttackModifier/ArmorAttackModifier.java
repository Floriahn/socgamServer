package com.socgam.server.model.CombatSystem.AttackModifier;

import com.socgam.server.model.CombatSystem.Attack;

public class ArmorAttackModifier extends AttackModifier{
    // Attributes
    private int armorModifier; // negative values will decrease armor, positive values will increase armor

    // Constructors
    public ArmorAttackModifier(int armorModifier) {
        this.armorModifier = armorModifier;
    }

    @Override
    public void applyModifier(Attack attack) {
        attack.addArmorModifier(armorModifier);
    }
}
