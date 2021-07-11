package com.socgam.server.model.CombatSystem.Status;

import com.socgam.server.model.CombatSystem.Attributes;
import com.socgam.server.model.CombatSystem.CONSTANTS;
import com.socgam.server.model.CombatSystem.Combatant;

public class Status {
    private int timeToLive; // -1 is unlimited
    private int modValue;
    private Attributes attribute;
    private Combatant target;

    public Status(Combatant target, Attributes attribute, int value, int timeToLive) {
        this.target = target;
        this. attribute = attribute;
        this.modValue = value;
        this.timeToLive = timeToLive;
        addStatus();

    }
    public void addStatus() {
        target.addStatus(this);
        target.modifyAttribute(attribute, modValue);
        if(CONSTANTS.DEBUG) {
            System.out.println("Status added: "+this+" to "+target);
        }
    }

    public void removeStatus() {
        target.modifyAttribute(attribute, -modValue);
        if(CONSTANTS.DEBUG) {
            System.out.println("Status removed: "+this+" from "+target);
        }
    }

    public void decreaseTTL() {
        if(timeToLive > 0) timeToLive -= 1;
        if(CONSTANTS.DEBUG) {
            System.out.println("Status TTL decreased: "+this+" on "+target);
        }
        if(timeToLive <= 0) removeStatus();
    }

    public int getTTL() {
        return timeToLive;
    }

    @Override
    public String toString() {
        return "'" +
                attribute +
                " " + modValue +
                " (" + timeToLive +
                "R)'";
    }
}
