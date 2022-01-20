package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Book;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BookService {

    boolean AddBook(Book book) throws IOException;

    List<Book> getAllBooks();

    Optional<Book> checkIfBookExist(String bookname);

    void deleteBook(Long id);
}
