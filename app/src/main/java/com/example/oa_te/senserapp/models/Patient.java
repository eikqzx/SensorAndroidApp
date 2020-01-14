package com.example.oa_te.senserapp.models;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Patient {
     String id;
     String patientName;
    String phone;
     String sex;
     int age;
    private String idsensor;
    String usertype;
  //  public Patient(String patientName, int patientAge, String patientSex, String patientRelative){}

    public Patient(){}

    public Patient(String patientName, int age, String sex, String phone,String idSensor,String usertype) {
        this.patientName = patientName;
        this.sex = sex;
        this.age = age;
        this.phone = phone;
        this.idsensor = idSensor;
        this.usertype = usertype;
    }

 /*   public Patient(String patientName, int patientAge, String patientSex, String patientRelative) {

        this.patientName = patientName;
        this.patientSex = patientSex;
        this.age = patientAge;
        this.relative = patientRelative;

    }*/

    public String getId() {
        return id;
    }
    public  String getIdsensor(){return idsensor;}
    public void setId(String id) {
        this.id = id;
    }


    public String getPatientName() {
        return patientName;
    }
public  String getUserType(){
        return usertype;
   }
    public int getAge() {
        return age;
    }
    public String getSex() {
        return sex;
    }
    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public void setPatientSex(String sex) {
        this.sex = sex;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setUserType(String userType) {
        this.usertype = userType;
    }

}
