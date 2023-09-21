package com.tsj2023.todobucketlist.data;

public class CompleteItem {
    public String title;
    public String img;

    public CompleteItem(String title, String img) {
        this.title = title;
        this.img = img;
    }

    public CompleteItem() {
    }

    public CompleteItem(String title) {
        this.title = title;
    }
}
