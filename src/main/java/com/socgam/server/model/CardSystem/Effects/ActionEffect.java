package com.socgam.server.model.CardSystem.Effects;

import com.socgam.server.model.CardSystem.CardTargetClass;
import com.socgam.server.model.CombatSystem.CombatInstance;
import com.socgam.server.model.CombatSystem.Combatant;
import com.socgam.server.model.CombatSystem.Faction;

import java.util.LinkedList;
import java.util.List;

public abstract class ActionEffect extends Effect {
    // Special Attributes
    protected List<Combatant> targets;
    protected EffectTargetClass effectTargetClass;

    // Constructor
    public ActionEffect(String id, EffectTargetClass effectTargetClass){
        super(id);
        this.targets = new LinkedList<>();
        this.effectTargetClass = effectTargetClass;
    }

    protected void setTargets(Combatant mainTarget) {
        switch(effectTargetClass) {
            case MAINTARGET:
                targets.add(mainTarget);
                break;
            case SELF:
                targets.add(actor);
                break;
            case ALLALLIES:
                targets = new LinkedList<>(context.getCombatantByFaction(actor.getFaction()));
                break;
            case ALLENEMIES:
                Faction enemies;
                if(actor.getFaction().equals(Faction.Enemies)) {
                    enemies = Faction.Players;
                } else {
                    enemies = Faction.Enemies;
                }
                targets = new LinkedList<>(context.getCombatantByFaction(enemies));
                break;
            default:
                break;
        }
    }
}
