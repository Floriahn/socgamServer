package com.socgam.server.model.CombatSystem;

import com.socgam.server.model.CardSystem.Card;

import java.util.Optional;

public class CombatLoop implements Runnable {
    // Attributes
    private CombatInstance parentInstance;

    // Constructor
    public CombatLoop(CombatInstance parentInstance) {
        this.parentInstance = parentInstance;
    }

    @Override
    public void run() {
        // Must be called after Initialization is complete
        while(parentInstance.isRunning()) {

            // RoundStartPhase
            startRoundPhaseRoutine();

            // EnemyCombatPhase
            enemyCombatPhaseRoutine();

            // PlayerCombatPhase
            playerCombatPhaseRoutine();

            // RoundEndPhase
            endRoundPhaseRoutine();

        }
        return;
    }

    private void startRoundPhaseRoutine() {
        // SubRound starts
        parentInstance.setCurrentPhase(CombatPhases.ROUNDSTART);
        parentInstance.advanceCombatRound();
        if(CONSTANTS.DEBUG) {
            System.out.println("Starting round "+parentInstance.getCombatRound());
        }
        // Delay
        try {
            Thread.sleep(CONSTANTS.COMBATSTARTPHASESTARTDELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Give Combatants cards & energy
        for (Combatant c : parentInstance.getCombatants()) {
            c.decreaseStatusesTTL();
            c.drawRoundCards();
            if(parentInstance.getCombatRound()==1) c.drawRoundCards();
            c.gainRoundEnergy();
            if(parentInstance.getCombatRound()>1) c.regenerate();
        }
        // Delay
        try {
            Thread.sleep(CONSTANTS.COMBATSTARTPHASEENDDELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // End subround
        return;
    }

    private void enemyCombatPhaseRoutine() {
        // SubRound starts
        parentInstance.setCurrentPhase(CombatPhases.ENEMYPHASE);
        // Set pass variables of all active combatants to false
        for(Combatant combatant : parentInstance.getCombatantByFaction(Faction.Enemies)) {
            combatant.pass(false);
        }
        // Initiate subround loop
        boolean allPassed = false;
        // Wait until all active combatants have passed
        while(!allPassed) {
            // Check for card plays
            for(Combatant combatant : parentInstance.getCombatantByFaction(Faction.Enemies)) {
                Optional<Card> c = SimpleAI.cardToPlay(combatant);
                // ToDo: Choose target somehow (at the moment just targets first player
                if(c.isPresent()) {
                    Optional<Combatant> target = SimpleAI.chooseTarget(c.get(), parentInstance, combatant);
                    c.get().addMainTarget(target.get());
                    c.get().playCard(parentInstance, combatant);
                } else {
                    combatant.pass(true);
                }
            }
            // Delay
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Check if active combatants have passed
            allPassed = true;
            for(Combatant combatant : parentInstance.getCombatantByFaction(Faction.Enemies)) {
                allPassed = allPassed && combatant.hasPassed();
            }
        }
        // End subround
        return;
    }

    private void playerCombatPhaseRoutine() {
        // SubRound starts
        parentInstance.setCurrentPhase(CombatPhases.PLAYERPHASE);
        // Set pass variables of all active combatants to false
        for(Combatant combatant : parentInstance.getCombatantByFaction(Faction.Players)) {
            combatant.pass(false);
        }
        // Initiate subround loop
        boolean allPassed = false;
        // Wait until all active combatants have passed
        while(!allPassed) {
            // Check for card plays
            for(Combatant combatant : parentInstance.getCombatantByFaction(Faction.Players)) {
                Optional<Card> c = combatant.retrievePlayedCard();
                if(c.isPresent()) {
                    c.get().playCard(parentInstance, combatant);
                }
            }

            // Check if active combatants have passed or timeout
            // ToDo: TimeOut mechanism
            allPassed = true;
            for(Combatant combatant : parentInstance.getCombatantByFaction(Faction.Players)) {
                allPassed = allPassed && combatant.hasPassed();
            }
        }
        // End subround
        return;
    }

    private void endRoundPhaseRoutine() {
        // SubRound starts
        parentInstance.setCurrentPhase(CombatPhases.ROUNDEND);
        // Delay
        try { Thread.sleep(CONSTANTS.COMBATENDPHASESTARTDELAY); }
        catch (InterruptedException e) { e.printStackTrace(); }

        // Check victory conditions
        boolean allPlayersDead = true;
        for(Combatant c : parentInstance.getCombatantByFaction(Faction.Players)) {
            if(!c.isDead()) allPlayersDead = false;
        }

        boolean allEnemiesDead = true;
        for(Combatant c : parentInstance.getCombatantByFaction(Faction.Enemies)) {
            if(!c.isDead()) allEnemiesDead = false;
        }

        // Apply victory
        if(allPlayersDead) {
            parentInstance.endCombat(Faction.Enemies);
        }
        else if(allEnemiesDead) {
            parentInstance.endCombat(Faction.Players);
        }

        // Delay
        try { Thread.sleep(CONSTANTS.COMBATENDPHASEENDDELAY); }
        catch (InterruptedException e) { e.printStackTrace(); }
        // End subround
        return;

    }
}
