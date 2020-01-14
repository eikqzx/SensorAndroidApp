package com.example.oa_te.senserapp.models;

public class Sensor {
    private String name;
    private String room;
    private String id;
    private String pulse;
    private String temp;
    private String status;


    public Sensor(){}

    public Sensor(String id,String name, String room,String pulse,String temp,String status) {
        this.name = name;
        this.room = room;
        this.id = id;
        this.pulse = pulse;
        this.temp = temp;
        this.status = status;

    }

    public String getStatus(){return status;}

    public String getPulse() {return pulse;}

    public String getTemp() {return temp;}

    public String getName() {
        return name;
    }


public void setStatus(String status){this.status = status;}

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

 /*   public void setRoom(String room) {
        this.room = room;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
