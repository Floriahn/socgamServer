package com.socgam.server.model.CardSystem.Effects;

import com.socgam.server.model.CombatSystem.AttackModifier.AttackModificationType;
import com.socgam.server.model.CombatSystem.Attributes;
import org.springframework.data.annotation.Id;

public class EffectDatabase {

    @Id
    private String effectId;
    private Subtypes subtype;
    private EffectTargetClass effectTargetClass;
    private boolean isPhysical;
    private int baseValue;
    private Attributes attribute;
    private int duration;
    private AttackModificationType attackModifier;

    /*public EffectDatabase(String effectId, Subtypes subtype, com.socgam.server.model.CardSystem.TargetClass targetClass, boolean isPhysical, int baseValue, Attributes attribute, int duration, AttackModificationType attackModifier) {
        this.effectId = effectId;
        Subtype = subtype;
        TargetClass = targetClass;
        this.isPhysical = isPhysical;
        this.baseValue = baseValue;
        Attribute = attribute;
        this.duration = duration;
        this.attackModifier = attackModifier;
    }*/

    /*public Effect parseToEffect(){
        switch (Subtype){
            case attack:
                Effect e = new AttackActionEffect(effectId)
                break;
            case status:
                break;
            case modifyAttack:
                break;
            case changeAttribute:
                break;
        }
    }*/

    public String getEffectId() {
        return effectId;
    }

    public void setEffectId(String effectId) {
        this.effectId = effectId;
    }

    public Subtypes getSubtype() {
        return subtype;
    }

    public void setSubtype(Subtypes subtype) {
        this.subtype = subtype;
    }

    public EffectTargetClass getTargetClass() {
        return effectTargetClass;
    }

    public void setTargetClass(EffectTargetClass effectTargetClass) {
        this.effectTargetClass = effectTargetClass;
    }

    public boolean isPhysical() {
        return isPhysical;
    }

    public void setPhysical(boolean physical) {
        isPhysical = physical;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }

    public Attributes getAttribute() {
        return attribute;
    }

    public void setAttribute(Attributes attribute) {
        this.attribute = attribute;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public AttackModificationType getAttackModifier() {
        return attackModifier;
    }

    public void setAttackModifier(AttackModificationType attackModifier) {
        this.attackModifier = attackModifier;
    }
}
