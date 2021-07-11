package com.socgam.server.model.CardSystem.Effects;

import com.socgam.server.model.CardSystem.Card;
import com.socgam.server.model.CombatSystem.CombatInstance;
import com.socgam.server.model.CombatSystem.Combatant;

public interface EffectInterface {
    public void executeEffect(Combatant target, Card card, Combatant actor);

    public void setContext(CombatInstance context);

    public void setActor(Combatant actor);
}
