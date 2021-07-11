package com.socgam.server.model.CombatSystem.AttackModifier;

import com.socgam.server.model.CombatSystem.Attack;

public class StaticAttackModifier extends AttackModifier {
    // Attributes
    private int staticChange; // positive values will increase attack value, negative values will decrease attack value

    // Constructors
    public StaticAttackModifier(int staticChange) {
        this.staticChange = staticChange;
    }

    @Override
    public void applyModifier(Attack attack) {
        attack.addStaticChange(staticChange);
    }
}
