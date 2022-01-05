package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {

    boolean AddBook(Book book) throws IOException;

    List<Book> getAllBooks();

    //boolean checkIfBookExist(String book_name);
}
