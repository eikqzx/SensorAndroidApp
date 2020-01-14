package com.example.oa_te.senserapp.models;


public class UserAccounts {
    String usersname;
    String usertype;


    public UserAccounts(){}
//username,age,sex,phone,idSensor,roles

    public UserAccounts(String usersname,String usertype) {
        this.usersname = usersname;
        this.usertype = usertype;

    }

    public String getusersname() {
        return usersname;
    }
    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype){this.usertype = usertype;}
    public void setusersname(String usersname) {
        this.usersname = usersname;
    }


}
