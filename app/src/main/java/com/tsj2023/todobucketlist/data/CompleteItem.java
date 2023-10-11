package com.tsj2023.todobucketlist.data;

import android.util.Log;

import java.io.File;

public class CompleteItem {
    int no;
    public String title;
    public String file;
    public String date;
    private String imgPath;

    public CompleteItem(int no, String title, String file, String date, String imgPath) {
        this.no = no;
        this.title = title;
        this.file = file;
        this.date = date;
        this.imgPath = null;
    }

    public CompleteItem(String title, String file) {
        this.title = title;
        this.file = file;
    }

    public CompleteItem() {
    }

    public CompleteItem(String title) {
        this.title = title;
    }
    public int getNo() {
        return no;
    }

    public String getFile(){
        return file;
    }

    public void setFile(String file){
        this.file = file;
    }

    public String getImgPath(){return imgPath;}
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

}
