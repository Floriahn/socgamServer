package com.socgam.server.model.CardSystem.Effects;

import com.socgam.server.model.CardSystem.Card;
import com.socgam.server.model.CombatSystem.AttackModifier.*;
import com.socgam.server.model.CombatSystem.CONSTANTS;
import com.socgam.server.model.CombatSystem.Combatant;

public class ModifyAttackEffect extends Effect {

    // Attributes
    private AttackActionEffect attack;
    private AttackModifier modifier;

    // Constructors
    public ModifyAttackEffect(String id, int value, AttackModificationType type) {
        super(id);
        switch(type) {
            case ARMOR:
                this.modifier = new ArmorAttackModifier(value);
                break;

            case DYNAMICMODIFIER:
                this.modifier = new DynamicAttackModifier(value);
                break;

            case STATICMODIFIER:
                this.modifier = new StaticAttackModifier(value);
                break;

            default:
                break;
        }
    }

    public void setAttack(AttackActionEffect attack) {
        this.attack = attack;
    }

    @Override
    public void executeEffect(Combatant mainTarget, Card card, Combatant actor) {
        if(CONSTANTS.DEBUG) {
            System.out.println("Effect executed: added Attackmodifier");
        }
        card.addAttackModifier(modifier);
    }
}
