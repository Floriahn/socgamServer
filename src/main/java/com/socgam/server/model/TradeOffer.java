package com.socgam.server.model;

public class TradeOffer {

    private String fromId;
    private String toId;
    private String CardFromId;
    private String CardToId;

    public TradeOffer(String from, String to, String cardFromId, String cardToId) {
        this.fromId = from;
        this.toId = to;
        CardFromId = cardFromId;
        CardToId = cardToId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getTo() {
        return toId;
    }

    public void setTo(String to) {
        this.toId = to;
    }

    public String getCardFromId() {
        return CardFromId;
    }

    public void setCardFromId(String cardFromId) {
        CardFromId = cardFromId;
    }

    public String getCardToId() {
        return CardToId;
    }

    public void setCardToId(String cardToId) {
        CardToId = cardToId;
    }
}