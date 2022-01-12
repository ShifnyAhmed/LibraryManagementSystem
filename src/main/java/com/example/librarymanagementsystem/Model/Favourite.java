package com.example.librarymanagementsystem.Model;

import javax.persistence.*;

@Entity
@Table(name = "favourite", uniqueConstraints = @UniqueConstraint(columnNames = "book_name"))
public class Favourite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id")
    private Long bookid;

    @Column(name = "book_name")
    private String bookname;

    @Column(name = "email")
    private String email;

    @Column(name = "book_author")
    private String author;

    @Column(name = "book_category")
    private String category;

    @Column(name = "image_name")
    private String image_file_name;

    @Column(name = "pdf_name")
    private String pdf_file_name;


    //Constructors

    public Favourite() {

    }

    public Favourite(Long id, Long bookid, String bookname, String email, String author, String category, String image_file_name, String pdf_file_name) {
        this.id = id;
        this.bookid = bookid;
        this.bookname = bookname;
        this.email = email;
        this.author = author;
        this.category = category;
        this.image_file_name = image_file_name;
        this.pdf_file_name = pdf_file_name;
    }

    public Favourite(Long bookid, String bookname, String email, String author, String category, String image_file_name, String pdf_file_name) {
        this.bookid = bookid;
        this.bookname = bookname;
        this.email = email;
        this.author = author;
        this.category = category;
        this.image_file_name = image_file_name;
        this.pdf_file_name = pdf_file_name;
    }

    //getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage_file_name() {
        return image_file_name;
    }

    public void setImage_file_name(String image_file_name) {
        this.image_file_name = image_file_name;
    }

    public String getPdf_file_name() {
        return pdf_file_name;
    }

    public void setPdf_file_name(String pdf_file_name) {
        this.pdf_file_name = pdf_file_name;
    }

    public Long getBookid() {
        return bookid;
    }

    public void setBookid(Long bookid) {
        this.bookid = bookid;
    }
}
