package com.tsj2023.todobucketlist.data;

public class TodoItem {

    public String msg;
    public boolean checked;
    public String category = "오늘의 할일";
    public boolean isSelected;
    public long id; // 아이템의 고유 ID

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
    public TodoItem(long id, String msg, boolean checked, String category) {
        this.id = id;
        this.msg = msg;
        this.checked = checked;
        this.category = category;
    }

    // Getter 및 Setter 메서드 추가
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
