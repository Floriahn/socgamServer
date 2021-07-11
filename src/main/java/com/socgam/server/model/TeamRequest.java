package com.socgam.server.model;

public class TeamRequest {
    private String teamId;
    private String fromId;

    public TeamRequest(String teamId, String fromId) {
        this.teamId = teamId;
        this.fromId = fromId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
}