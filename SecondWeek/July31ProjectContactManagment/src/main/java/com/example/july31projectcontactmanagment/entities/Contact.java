package com.example.july31projectcontactmanagment.entities;

public class Contact implements IEntity{
    private int id;
    private int userid;
    private String contactname;
    private String contactnumber;

    public Contact(int id, int userid, String contactname, String contactnumber) {
        this.id = id;
        this.userid = userid;
        this.contactname = contactname;
        this.contactnumber = contactnumber;
    }



    public Contact() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }
}
