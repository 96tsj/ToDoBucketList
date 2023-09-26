package com.tsj2023.todobucketlist.data;

public class BucketlistItem {

    public String msg;
    public boolean checked;

    public BucketlistItem(String msg, boolean checked) {
        this.msg = msg;
        this.checked = checked;
    }

    public BucketlistItem() {
    }
    public boolean isChecked(){
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
