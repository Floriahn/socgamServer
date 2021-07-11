package com.socgam.server.ControllerArrays;

import com.socgam.server.model.User;

import java.util.List;

public class UserList {
    private List<User> list;

    public UserList(List<User> list) {
        this.list = list;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
}
