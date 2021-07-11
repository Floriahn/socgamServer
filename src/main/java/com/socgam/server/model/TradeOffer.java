package com.socgam.server.model;

public class TradeOffer {

    private String fromId;
    private String toId;
    private String cardFromId;
    private String cardToId;

    public TradeOffer(String fromId, String toId, String cardFromId, String cardToId) {
        this.fromId = fromId;
        this.toId = toId;
        this.cardFromId = cardFromId;
        this.cardToId = cardToId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getCardFromId() {
        return cardFromId;
    }

    public void setCardFromId(String cardFromId) {
        this.cardFromId = cardFromId;
    }

    public String getCardToId() {
        return cardToId;
    }

    public void setCardToId(String cardToId) {
        this.cardToId = cardToId;
    }
}