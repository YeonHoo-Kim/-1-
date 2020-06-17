package org.techtown.diary;

public class JsonModel {


    private int ID;
    private String DATE_TIME;
    private  String CAR_NUMBER;
    private String COUNT;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDATE_TIME() {
        return DATE_TIME;
    }

    public void setDATE_TIME(String DATE_TIME) {
        this.DATE_TIME = DATE_TIME;
    }

    public String getCAR_NUMBER() {
        return CAR_NUMBER;
    }

    public void setCAR_NUMBER(String CAR_NUMBER) {
        this.CAR_NUMBER = CAR_NUMBER;
    }
    public String get1COUNT(){
        return COUNT;
    }
    public void setCOUNT(String COUNT){
        this.COUNT=COUNT;
    }
}