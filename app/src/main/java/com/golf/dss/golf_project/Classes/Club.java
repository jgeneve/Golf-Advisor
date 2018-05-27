package com.golf.dss.golf_project.Classes;

public class Club {
    private int id;
    private String name;
    private int menMinDistance;
    private int menMaxDistance;
    private int menAvgDistance;
    private int womenMinDistance;
    private int womenMaxDistance;
    private int womenAvgDistance;

    public Club(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Club(String name, int menMinDistance, int menAvgDistance, int menMaxDistance, int womenMinDistance, int womenAvgDistance, int womenMaxDistance) {
        this.name = name;
        this.menMinDistance = menMinDistance;
        this.menMaxDistance = menMaxDistance;
        this.menAvgDistance = menAvgDistance;
        this.womenMinDistance = womenMinDistance;
        this.womenMaxDistance = womenMaxDistance;
        this.womenAvgDistance = womenAvgDistance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMenMinDistance() {
        return menMinDistance;
    }

    public void setMenMinDistance(int menMinDistance) {
        this.menMinDistance = menMinDistance;
    }

    public int getMenMaxDistance() {
        return menMaxDistance;
    }

    public void setMenMaxDistance(int menMaxDistance) {
        this.menMaxDistance = menMaxDistance;
    }

    public int getMenAvgDistance() {
        return menAvgDistance;
    }

    public void setMenAvgDistance(int menAvgDistance) {
        this.menAvgDistance = menAvgDistance;
    }

    public int getWomenMinDistance() {
        return womenMinDistance;
    }

    public void setWomenMinDistance(int womenMinDistance) {
        this.womenMinDistance = womenMinDistance;
    }

    public int getWomenMaxDistance() {
        return womenMaxDistance;
    }

    public void setWomenMaxDistance(int womenMaxDistance) {
        this.womenMaxDistance = womenMaxDistance;
    }

    public int getWomenAvgDistance() {
        return womenAvgDistance;
    }

    public void setWomenAvgDistance(int womenAvgDistance) {
        this.womenAvgDistance = womenAvgDistance;
    }
}
