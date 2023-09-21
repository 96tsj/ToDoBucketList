package com.tsj2023.todobucketlist.data;

public class TodoItem {
    public TodoItem(String msg, boolean cheked) {
        this.msg = msg;
        this.cheked = cheked;
    }
    public TodoItem() {
    }

    public String msg;
    public boolean cheked;
}
