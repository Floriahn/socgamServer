package com.socgam.server.ControllerArrays;

import com.socgam.server.model.CardSystem.CardDatabase;

import java.util.List;

public class CardDatabaseList {
    private List<CardDatabase> list;

    public CardDatabaseList(List<CardDatabase> list) {
        this.list = list;
    }

    public List<CardDatabase> getList() {
        return list;
    }

    public void setList(List<CardDatabase> list) {
        this.list = list;
    }
}
