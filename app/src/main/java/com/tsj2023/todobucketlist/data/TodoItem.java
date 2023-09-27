package com.tsj2023.todobucketlist.data;

public class TodoItem {

    public String msg;
    public boolean checked;
    public String category = "오늘의 할일";
    public boolean isSelected;

    public TodoItem(long id, String msg, boolean isChecked, String category) {
    }

    public boolean getSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }
    public TodoItem(String msg, boolean cheked) {
        this.msg = msg;
        this.checked = cheked;
    }
    public TodoItem() {
    }

    public TodoItem(String msg, boolean cheked, String category) {
        this.msg = msg;
        this.checked = checked;
        this.category = category;
    }
}
