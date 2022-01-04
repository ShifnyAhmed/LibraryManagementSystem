package com.example.librarymanagementsystem.Model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "dateofbirth")
    private String dateofbirth;

    @Column(name = "level")
    private String level;

    @Column(name = "blacklisted")
    private String blacklist;

    //Joining Tables
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(

                    name = "user_id", referencedColumnName = "id"

            ),
            inverseJoinColumns = @JoinColumn(

                    name = "role_id", referencedColumnName = "id"
            )
    )

    private Collection<Role> roles;


    //Constructors

    public User(Long id, String fullname, String email, String password, String mobile, String dateofbirth, String level, String blacklist, Collection<Role> roles) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.dateofbirth = dateofbirth;
        this.level = level;
        this.blacklist = blacklist;
        this.roles = roles;
    }

    public User(Long id, String fullname, String email, String password, String mobile, String dateofbirth, String level, String blacklist) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.dateofbirth = dateofbirth;
        this.level = level;
        this.blacklist = blacklist;
    }

    //used for registration
    public User(String fullname, String email, String password, String mobile,String dateofbirth,String level, String blacklist, Collection<Role> roles) {
        super();
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.dateofbirth = dateofbirth;
        this.level = level;
        this.blacklist = blacklist;
        this.roles = roles;
    }

    public User(Long id, String fullname, String email, String password, String mobile) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
    }

    public User() {
    }



    //Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
