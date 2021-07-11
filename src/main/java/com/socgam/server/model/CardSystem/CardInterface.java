package com.socgam.server.model.CardSystem;

import com.socgam.server.model.CombatSystem.CombatInstance;
import com.socgam.server.model.CombatSystem.Combatant;

public interface CardInterface {
    public void playCard(CombatInstance context, Combatant actor);
}
