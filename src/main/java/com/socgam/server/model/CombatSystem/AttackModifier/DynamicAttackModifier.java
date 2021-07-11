package com.socgam.server.model.CombatSystem.AttackModifier;

import com.socgam.server.model.CombatSystem.Attack;

public class DynamicAttackModifier extends AttackModifier {
    // Attributes
    private int dynamicChange; // Percentage that will be added to 1.0f (= 100%), negative values will decrease total

    // Constructors
    public DynamicAttackModifier(int dynamicChange) {
        this.dynamicChange = dynamicChange;
    }

    @Override
    public void applyModifier(Attack attack) {
        attack.addDynamicChange(dynamicChange);
    }
}
