package com.socgam.server.model.CardSystem;

public class CardPlay {
    // Attributes
    private int cardID;
    private int playerID;
    private int targetID;
    private int combatID;

    public CardPlay(int cardID, int playerID, int targetID, int combatID) {
        this.cardID = cardID;
        this.playerID = playerID;
        this.targetID = targetID;
        this.combatID = combatID;
    }
}
