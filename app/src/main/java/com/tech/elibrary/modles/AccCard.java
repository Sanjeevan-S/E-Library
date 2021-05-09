package com.tech.elibrary.modles;

public class AccCard {

    String id;
    String type;
    String name;
    String number;
    String expire;
    String cvs;

    public AccCard() {
    }

    public AccCard(String id,String type, String name, String number, String expire, String cvs) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.number = number;
        this.expire = expire;
        this.cvs = cvs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getCvs() {
        return cvs;
    }

    public void setCvs(String cvs) {
        this.cvs = cvs;
    }
}
