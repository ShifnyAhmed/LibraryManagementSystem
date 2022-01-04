package com.example.librarymanagementsystem.Model;

import javax.persistence.*;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_name")
    private String bookname;

    @Column(name = "book_author")
    private String author;

    @Column(name = "book_publisher")
    private String publisher;

    private byte[] image;

    private byte[] pdf;

    @Column(name = "book_category")
    private String category;

    //Constructors


    public Book(Long id, String bookname, String author, String publisher, byte[] image, byte[] pdf, String category) {
        this.id = id;
        this.bookname = bookname;
        this.author = author;
        this.publisher = publisher;
        this.image = image;
        this.pdf = pdf;
        this.category = category;
    }

    public Book() {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
