package com.socgam.server.model.CombatSystem.SnapShotSystem;

public abstract class ActionSnapshot {
    private final String actorID;

    public ActionSnapshot(String actorID) {
        this.actorID = actorID;
    }
}
