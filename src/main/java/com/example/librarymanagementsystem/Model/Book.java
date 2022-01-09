package com.example.librarymanagementsystem.Model;

import javax.persistence.*;

@Entity
@Table(name = "book", uniqueConstraints = @UniqueConstraint(columnNames = "book_name"))
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_name")
    private String bookname;

    @Column(name = "book_author")
    private String author;

    @Column(name = "book_category")
    private String category;

    private byte[] image;

    @Column(name = "file_Name")
    private String fileName;

    @Column(name = "file_Path")
    private String filePath;

    private byte[] pdf;

    @Column(name = "pdf_Name")
    private String pdfName;

    @Column(name = "pdf_Path")
    private String pdfPath;



    //Constructors


    public Book(Long id, String bookname, String author, String category, byte[] image, String fileName, String filePath, byte[] pdf, String pdfName, String pdfPath) {
        this.id = id;
        this.bookname = bookname;
        this.author = author;
        this.category = category;
        this.image = image;
        this.fileName = fileName;
        this.filePath = filePath;
        this.pdf = pdf;
        this.pdfName = pdfName;
        this.pdfPath = pdfPath;
    }

    public Book(String bookname, String author, String category, byte[] image, String fileName, String filePath, byte[] pdf, String pdfName, String pdfPath) {
        this.bookname = bookname;
        this.author = author;
        this.category = category;
        this.image = image;
        this.fileName = fileName;
        this.filePath = filePath;
        this.pdf = pdf;
        this.pdfName = pdfName;
        this.pdfPath = pdfPath;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
}
