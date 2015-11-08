package com.example.sbastien.thieftracker.model;


public class Friends {

    String name;
    String phoneNumber;

    public Friends(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Friends friend = (Friends) o;
        if( !this.name.equals(friend.getName()) && !this.phoneNumber.equals(friend.getPhoneNumber()))
            return false;
        return true;
    }
}
