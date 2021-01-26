package com.asaru.projectmcq;

public class userhadler {
    String firstName,email,photoUrl;

    public userhadler(String firstName, String email, String photoUrl) {
        this.firstName = firstName;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public userhadler() {
    }

    public userhadler(String firstName,  String email) {
        this.firstName = firstName;
        this.email = email;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
