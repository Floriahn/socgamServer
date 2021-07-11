package com.socgam.server.model.CardSystem.Effects;

import com.socgam.server.model.CombatSystem.CombatInstance;
import com.socgam.server.model.CombatSystem.Combatant;
import com.socgam.server.repository.EffectDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;

public abstract class Effect implements EffectInterface {
    // Attributes
    @Id
    private String effectID;
    protected CombatInstance context;
    protected Combatant actor;


    // Constructor
    public Effect(String id) {
        this.effectID = id;
    }

    // Methods


    public void setContext(CombatInstance context) {
        this.context = context;
    }

    @Override
    public void setActor(Combatant actor) {
        this.actor = actor;
    }


    public static Effect loadEffect(EffectDatabase ed) {
        switch (ed.getSubtype()){
            case changeAttribute:
                return new ChangeAttributeEffect(ed.getEffectId(),ed.getTargetClass(),ed.getBaseValue(),ed.getAttribute());
            case modifyAttack:
                return new ModifyAttackEffect(ed.getEffectId(), ed.getBaseValue(),ed.getAttackModifier());
            case status:
                return new CreateStatusEffect(ed.getEffectId(), ed.getTargetClass(), ed.getAttribute(), ed.getBaseValue(), ed.getDuration());
            case attack:
                return new AttackActionEffect(ed.getEffectId(), ed.getTargetClass(), ed.isPhysical(), ed.getBaseValue());
        }
        return null;
    }


}
