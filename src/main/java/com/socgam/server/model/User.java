package com.socgam.server.model;

import org.springframework.data.annotation.Id;

import java.util.LinkedList;
import java.util.List;

public class User {

    @Id
    private String id;
    private String username;
    private String teamId;
    private Location loc;
    private long lastTimeUpdated;
    private int scrollCount;
    private String activeFightId;
    private CharacterModel activeCharacter;
    private List<FriendRelation> friendRelations;
    private List<CharacterModel> characters;
    private TradeOffer tradeOffers;
    private List<String> friendRequests;
    private List<TeamRequest> teamRequests;
    private List<String> cardCollection;

    //0 -normal
    //1 -trade canceled
    //2 -trade success
    int tradeState;

    private static final long maxUpdateDelay = 30000;

    public User(String id, String username) {
        this.id = id;
        this.username = username;
        friendRelations = new LinkedList<>();
        friendRequests = new LinkedList<>();
        teamId = null;
        characters = new LinkedList<>();
        scrollCount = 0;
        activeCharacter = null;
        tradeOffers = null;
        activeFightId = null;
        cardCollection = new LinkedList<>();
        teamRequests = new LinkedList<>();
        tradeState = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;

    }

    public boolean isOnline() {
        boolean test = lastTimeUpdated + maxUpdateDelay > System.currentTimeMillis();
        return test;
    }

    public void setLastTimeUpdated(long lastTimeUpdated) {
        this.lastTimeUpdated = lastTimeUpdated;
    }

    public List<FriendRelation> getFriendRelations() {
        return friendRelations;
    }

    public List<String> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<String> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamId() {
        return teamId;
    }

    public List<TeamRequest> getTeamRequests() {
        return teamRequests;
    }

    public void setTeamRequests(List<TeamRequest> teamRequests) {
        this.teamRequests = teamRequests;
    }

    public List<CharacterModel> getCharacters() {
        return characters;
    }

    public int getScrollCount() {
        return scrollCount;
    }

    public void setScrollCount(int scrollCount) {
        this.scrollCount = scrollCount;
    }

    public CharacterModel getActiveCharacter() {
        return activeCharacter;
    }

    public void setActiveCharacter(CharacterModel activeCharacter) {
        this.activeCharacter = activeCharacter;
    }

    public TradeOffer getTradeOffers() {
        return tradeOffers;
    }

    public String getActiveFightId() {
        return activeFightId;
    }

    public void setActiveFightId(String activeFightId) {
        this.activeFightId = activeFightId;
    }

    public List<String> getCardCollection() {
        return cardCollection;
    }

    public void setCardCollection(List<String> cardCollection) {
        this.cardCollection = cardCollection;
    }

    public void setTradeOffers(TradeOffer tradeOffers) {
        this.tradeOffers = tradeOffers;
    }

    public long getLastTimeUpdated() {
        return lastTimeUpdated;
    }

    public void setFriendRelations(List<FriendRelation> friendRelations) {
        this.friendRelations = friendRelations;
    }

    public void setCharacters(List<CharacterModel> characters) {
        this.characters = characters;
    }

    public static long getMaxUpdateDelay() {
        return maxUpdateDelay;
    }

    public void setTradeState(int tradeState) {
        this.tradeState = tradeState;
    }

    public int getTradeState() {
        return tradeState;
    }
}