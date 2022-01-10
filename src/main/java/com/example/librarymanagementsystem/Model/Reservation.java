package com.example.librarymanagementsystem.Model;

import javax.persistence.*;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id")
    private Long book_id;

    @Column(name = "email")
    private String email;

    @Column(name = "book_name")
    private String book_name;

    @Column(name = "reserved_date")
    private String reserved_date;

    @Column(name = "allowed_return_date")
    private String allowed_return_date;

    @Column(name = "actual_return_date")
    private String actual_return_date;

    @Column(name = "lending_duration")
    private int lending_duration;

    @Column(name = "lending_charges")
    private int lending_charges;

    @Column(name = "overdue_charges")
    private int overdue_charges;

    @Column(name = "total_charges")
    private int total;

    @Column(name = "status")
    private String status;



    //Constructors

    public Reservation() {
    }

    public Reservation(Long id, Long book_id, String email, String book_name, String reserved_date, String allowed_return_date, String actual_return_date, int lending_duration, int lending_charges, int overdue_charges, int total, String status) {
        this.id = id;
        this.book_id = book_id;
        this.email = email;
        this.book_name = book_name;
        this.reserved_date = reserved_date;
        this.allowed_return_date = allowed_return_date;
        this.actual_return_date = actual_return_date;
        this.lending_duration = lending_duration;
        this.lending_charges = lending_charges;
        this.overdue_charges = overdue_charges;
        this.total = total;
        this.status = status;
    }

    public Reservation(Long id, Long book_id, String email, String book_name, String reserved_date, String allowed_return_date, int lending_duration, int lending_charges, String status) {
        this.id = id;
        this.book_id = book_id;
        this.email = email;
        this.book_name = book_name;
        this.reserved_date = reserved_date;
        this.allowed_return_date = allowed_return_date;
        this.lending_duration = lending_duration;
        this.lending_charges = lending_charges;
        this.status = status;
    }


    //getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBook_id() {
        return book_id;
    }

    public void setBook_id(Long book_id) {
        this.book_id = book_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getReserved_date() {
        return reserved_date;
    }

    public void setReserved_date(String reserved_date) {
        this.reserved_date = reserved_date;
    }

    public String getAllowed_return_date() {
        return allowed_return_date;
    }

    public void setAllowed_return_date(String allowed_return_date) {
        this.allowed_return_date = allowed_return_date;
    }

    public String getActual_return_date() {
        return actual_return_date;
    }

    public void setActual_return_date(String actual_return_date) {
        this.actual_return_date = actual_return_date;
    }

    public int getLending_duration() {
        return lending_duration;
    }

    public void setLending_duration(int lending_duration) {
        this.lending_duration = lending_duration;
    }

    public int getLending_charges() {
        return lending_charges;
    }

    public void setLending_charges(int lending_charges) {
        this.lending_charges = lending_charges;
    }

    public int getOverdue_charges() {
        return overdue_charges;
    }

    public void setOverdue_charges(int overdue_charges) {
        this.overdue_charges = overdue_charges;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
