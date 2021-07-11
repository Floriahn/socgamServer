package com.socgam.server.model.CombatSystem.SnapShotSystem;

import com.socgam.server.model.CombatSystem.CombatInstance;

import java.util.TimerTask;

public class CombatSnapshotTimerTask extends TimerTask {
    private CombatInstance combatInstance;

    public CombatSnapshotTimerTask(CombatInstance combatInstance) {
        this.combatInstance = combatInstance;
    }

    @Override
    public void run() {
        // Create new Snapshot
        CombatSnapshot cs = new CombatSnapshot(combatInstance.getLastSequence()+1, combatInstance);
        // Put Snapshot into map

        combatInstance.addSnapshot(cs);
    }
}
