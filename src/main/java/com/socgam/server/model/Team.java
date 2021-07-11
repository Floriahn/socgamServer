package com.socgam.server.model;

import org.springframework.data.annotation.Id;

import java.util.LinkedList;
import java.util.List;

public class Team {

    @Id
    private String teamId;
    private String teamname;
    private List<String> playerIds;

    public Team(String teamId, String teamname, List<String> playerIds) {
        this.teamId = teamId;
        this.teamname = teamname;
        this.playerIds = playerIds;
    }

    public boolean tryJoinTeam(String playerId){
        if(playerIds.contains(playerId)) return false;
        if(playerIds.size() < 3){
            playerIds.add(playerId);
            return true;
        }
        return false;
    }

    public void leaveTeam(String playerId){
        playerIds.remove(playerId);
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public List<String> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<String> playerIds) {
        this.playerIds = playerIds;
    }
}
