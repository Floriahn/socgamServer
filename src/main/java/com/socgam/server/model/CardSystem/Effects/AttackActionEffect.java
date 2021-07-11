package com.socgam.server.model.CardSystem.Effects;

import com.socgam.server.model.CardSystem.Card;
import com.socgam.server.model.CardSystem.CardTargetClass;
import com.socgam.server.model.CombatSystem.Attack;
import com.socgam.server.model.CombatSystem.AttackModifier.AttackModifier;
import com.socgam.server.model.CombatSystem.Combatant;

import java.util.LinkedList;
import java.util.List;

public class AttackActionEffect extends ActionEffect {
    // Attributes
    private List<AttackModifier> attackModifiers;
    private boolean isPhysical;
    private int baseValue;


    // Constructors
    public AttackActionEffect(String id, EffectTargetClass effectTargetClass, boolean isPhysical, int baseValue) {
        super(id, effectTargetClass);
        this.isPhysical = isPhysical;
        this.baseValue = baseValue;
    }

    @Override
    public void executeEffect(Combatant mainTarget, Card card, Combatant actor) {
        // Calculate actual target list based on effect data
        setTargets(mainTarget);
        // For each target of the attack action:
        for(Combatant target : targets) {
            // Create Attack instance
            Attack attack = new Attack(isPhysical, baseValue, actor, target);
            card.addAttack(attack);
            /*
            // Check for modifiers and apply them to Attack instance
            for(AttackModifier modifier : attackModifiers) {
                modifier.applyModifier(attack);
            }
            // Add Attack instance to current Attack queue
            attacker.getCombatContext().getCurrentCombatPhase().addToAttackQueue(attack, attacker);
            */

        }
    }


}
