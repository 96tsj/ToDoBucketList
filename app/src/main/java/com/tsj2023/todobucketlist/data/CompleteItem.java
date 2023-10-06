package com.tsj2023.todobucketlist.data;

public class CompleteItem {
    int no;
    public String title;
    public String img;
    String date;

    public CompleteItem(int no, String title, String img, String date) {
        this.no = no;
        this.title = title;
        this.img = img;
        this.date = date;
    }

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
