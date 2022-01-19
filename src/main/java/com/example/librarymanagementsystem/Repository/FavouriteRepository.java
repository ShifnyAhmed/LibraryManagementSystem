package com.example.librarymanagementsystem.Repository;

import com.example.librarymanagementsystem.Model.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite,Long> {

    void deleteAllByEmail(String email);

    List<Favourite> findByEmail(String email);

    Optional<Favourite> findByBooknameAndEmail(String bookname, String email);

}
