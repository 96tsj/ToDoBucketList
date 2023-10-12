package com.tsj2023.todobucketlist.data;

public class BucketlistItem {

    public String msg;
    public boolean checked;
    public long id;
    public String category = "버킷리스트";
    public String email;

    public BucketlistItem(long id, String msg, boolean checked, String category) {
        this.id = id;
        this.msg = msg;
        this.checked = checked;
        this.category = category;
    }

    public BucketlistItem(String msg, boolean checked) {
        this.msg = msg;
        this.checked = checked;
    }

    public BucketlistItem(String msg, boolean checked, String category) {
        this.msg = msg;
        this.checked = checked;
        this.category = category;
    }

    public BucketlistItem() {
    }
    public boolean isChecked(){
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

}
