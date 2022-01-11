package com.example.librarymanagementsystem.Repository;

import com.example.librarymanagementsystem.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findByStatus(String status);

   long countReservationByEmail(String email);

    List<Reservation> findByEmailAndStatus(String email, String status);
}
