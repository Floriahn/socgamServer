package com.socgam.server.model.CardSystem.Effects;

import com.socgam.server.model.CardSystem.Card;
import com.socgam.server.model.CardSystem.CardTargetClass;
import com.socgam.server.model.CombatSystem.*;
import com.socgam.server.model.CombatSystem.Status.Status;

public class CreateStatusEffect extends ActionEffect {
    // Attributes
    private int duration;
    private Attributes attribute;
    private int value;

    // Constructors
    public CreateStatusEffect(String id, EffectTargetClass effectTargetClass, Attributes attribute, int value, int duration) {
        super(id, effectTargetClass);
        this.attribute = attribute;
        this.value = value;
        this.duration = duration;

    }

    @Override
    public void executeEffect(Combatant mainTarget, Card card, Combatant actor) {
        // Calculate actual targets based on effect
        setTargets(mainTarget);
        // If actor is player faction, duration +1
        if(actor.getFaction().equals(Faction.Players)) duration++;
        // Create new statuses for every target
        for(Combatant target : targets) {
            new Status(target, attribute, value, duration);
        }
        if(CONSTANTS.DEBUG) {
            System.out.println("Effect executed: Status. "+value+" to"+attribute+" for "+duration+" rounds.");
        }
    }
}
