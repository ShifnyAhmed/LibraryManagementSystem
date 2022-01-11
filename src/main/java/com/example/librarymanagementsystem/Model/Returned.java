package com.example.librarymanagementsystem.Model;

import javax.persistence.*;

//This is used to save reservations which are confirmed by members as returned which will be checked by admin to double confirm
@Entity
@Table(name = "returned_reservation")
public class Returned {

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

    //constructors
}
