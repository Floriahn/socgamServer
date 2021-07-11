package com.socgam.server.model.CombatSystem;

public enum CombatPhases {
    ROUNDSTART,
    ENEMYPHASE,
    PLAYERPHASE,
    ROUNDEND;

    @Override
    public String toString() {
        switch (this) {
            case ROUNDSTART:
                return "Rundenbeginn";
            case ENEMYPHASE:
                return "Gegnerphase";
            case PLAYERPHASE:
                return "Spielerphase";
            default:
                return "";
        }
    }
}
