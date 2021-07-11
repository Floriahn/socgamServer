package com.socgam.server.model.CombatSystem.SnapShotSystem;

import com.socgam.server.model.CombatSystem.CombatInstance;
import com.socgam.server.model.CombatSystem.CombatPhases;
import com.socgam.server.model.CombatSystem.Combatant;
import com.socgam.server.model.CombatSystem.Faction;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CombatSnapshot {
    // Attributes - identification
    private final int sequenceNumber; // Snapshot-ID
    // Fight-ID
    private final Timestamp timeOfCreation;// Time of creation
    //private CombatInstance parentInstance;
    private final String combatID;
    private final int currentRound;
    private final CombatPhases currentPhase;
    private final boolean stillRunning;
    private final Faction winner;

    // Attributes - content
    private final List<CombatantSnapshot> combatantSnapshotList;
    //private final Queue<ActionSnapshot> actionsSinceLastSnapshot;

    // Constructor
    public CombatSnapshot(int sequenceNumber, CombatInstance combatInstance) {
        this.sequenceNumber = sequenceNumber;
        this.combatID = combatInstance.getCombatID();
        this.currentPhase = combatInstance.getCurrentPhase();
        this.currentRound = combatInstance.getCombatRound();
        this.stillRunning = combatInstance.isRunning();
        this.winner = combatInstance.getWinner();
        this.combatantSnapshotList = new LinkedList<>();
        //this.parentInstance = combatInstance;
        // Creating CombatantSnapshots
        for(Combatant combatant : combatInstance.getCombatants()) {
            combatantSnapshotList.add(new CombatantSnapshot(combatant, this));
        }
        //this.actionsSinceLastSnapshot = combatInstance.getActionsSinceLastSnapshot();
        combatInstance.resetActionQueue();
        this.timeOfCreation = new Timestamp(System.currentTimeMillis());
    }

    // Getter

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public Timestamp getTimeOfCreation() {
        return timeOfCreation;
    }

    public List<CombatantSnapshot> getCombatantSnapshotList() {
        return combatantSnapshotList;
    }

    /*
    public Queue<ActionSnapshot> getActionsSinceLastSnapshot() {
        return actionsSinceLastSnapshot;
    }
    */

    public String getCombatID() {
        return combatID;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public CombatPhases getCurrentPhase() {
        return currentPhase;
    }

    public boolean isStillRunning() {
        return stillRunning;
    }

    public Faction getWinner() {
        return winner;
    }
}
