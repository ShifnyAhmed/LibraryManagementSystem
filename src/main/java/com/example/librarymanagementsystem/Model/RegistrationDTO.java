package com.example.librarymanagementsystem.Model;

//this Data Transfer Object (DTO) Design Pattern file is used for user registration
public class RegistrationDTO {

    private String fullname;

    private String email;

    private String password;

    private String mobile;

    private String dateofbirth;

    private String level;

    private String blacklist;


    //Constructors


    public RegistrationDTO(String fullname, String email, String password, String mobile, String dateofbirth, String level, String blacklist) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.dateofbirth = dateofbirth;
        this.level = level;
        this.blacklist = blacklist;
    }

    public RegistrationDTO() {
    }

    //getters and setters

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String blacklist) {
        this.blacklist = blacklist;
    }
}
