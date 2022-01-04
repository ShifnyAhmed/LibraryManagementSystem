package com.example.librarymanagementsystem.Repository;

import com.example.librarymanagementsystem.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findDetailsByCategory(String category);

    Book findByBookname(String bookname);
}
