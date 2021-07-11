package com.socgam.server.model.CardSystem.Effects;

import com.socgam.server.model.CardSystem.Card;
import com.socgam.server.model.CardSystem.CardTargetClass;
import com.socgam.server.model.CombatSystem.Attributes;
import com.socgam.server.model.CombatSystem.CONSTANTS;
import com.socgam.server.model.CombatSystem.Combatant;

public class ChangeAttributeEffect extends ActionEffect {
    // Attributes
    protected int amount;
    protected Attributes attribute;
    // Constructor
    public ChangeAttributeEffect(String id, EffectTargetClass effectTargetClass, int amount, Attributes attribute) {
        super(id, effectTargetClass);
        this.amount = amount;
        this.attribute = attribute;

    }
    @Override
    public void executeEffect(Combatant mainTarget, Card card, Combatant actor) {
        // Calculate actual target list based on effect data
        setTargets(mainTarget);
        // Execute effect on all targets
        targets.stream().forEach(c -> c.changeAttribute(attribute,amount));
        if(CONSTANTS.DEBUG) {
            System.out.println("Effect executed: "+amount+" to "+attribute+" of "+targets.get(0));
        }
    }
}
