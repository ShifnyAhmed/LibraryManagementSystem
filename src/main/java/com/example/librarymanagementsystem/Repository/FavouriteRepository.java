package com.example.librarymanagementsystem.Repository;

import com.example.librarymanagementsystem.Model.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite,Long> {

    void deleteAllByEmail(String email);

    List<Favourite> findByEmail(String email);

}
