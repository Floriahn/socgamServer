package com.socgam.server.model;

import java.util.Objects;

public class FriendRelation {
    private String friendId;
    private int bonus;

    public FriendRelation(String friendId) {
        this.friendId = friendId;
        bonus = 0;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRelation that = (FriendRelation) o;
        return Objects.equals(friendId, that.friendId);
    }
}
