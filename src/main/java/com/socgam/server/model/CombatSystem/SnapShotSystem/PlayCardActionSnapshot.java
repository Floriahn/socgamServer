package com.socgam.server.model.CombatSystem.SnapShotSystem;

public class PlayCardActionSnapshot extends ActionSnapshot {
    // Attributes
    public final String targetID;
    public final String cardID;

    public PlayCardActionSnapshot(String actorID, String targetID, String cardID) {
        super(actorID);
        this.targetID = targetID;
        this.cardID = cardID;
    }
}
