package com.example.tiki.unispace_app;

public class ClassroomObject {

    private int classroom;
    private String freeuntil;
        private int icon;
    private int building;


    public ClassroomObject(int b, int c, String fr, int icon) {
        this.building = b;
        this.classroom = c;
        this.freeuntil = fr;
        this.icon = icon;
    }

    public void setFreeUntil(String freeUntil) {
        this.freeuntil = freeUntil;
    }

    public int getBuilding() {
        return building;
    }

    public int getClassroom() {
        return classroom;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public void setClassroom(int classroom) {
        this.classroom = classroom;
    }

    public int getImageResource() {
        return icon;
    }
    public String getFreeUntil() {
        return freeuntil;
    }

    public void TEST_CHANGE(String s) {
        this.freeuntil = s;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


}
