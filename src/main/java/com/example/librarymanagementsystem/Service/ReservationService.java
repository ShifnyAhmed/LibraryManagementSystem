package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

        List<Reservation> getReservationByStatus(String status);

        long getNumberOfReservationByEmail(String email);

        String saveReservation(Reservation reservation);

        void deleteReservation(Reservation reservation);

        void deleteReservationById(Long id);

        Optional<Reservation> getReservationByID(Long id);

        List<Reservation> getReservationByEmailAndStatus(String email, String status);
}
