package com.example.librarymanagementsystem.Repository;


import com.example.librarymanagementsystem.Model.Returned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnedRepository extends JpaRepository<Returned,Long> {

    List<Returned> findByStatus(String status);

    List<Returned> findByEmailAndStatus(String email, String status);

}
