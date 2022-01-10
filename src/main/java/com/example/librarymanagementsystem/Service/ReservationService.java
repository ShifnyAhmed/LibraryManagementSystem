package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Reservation;

import java.util.List;

public interface ReservationService {

        List<Reservation> getReservationByStatus(String status);

        void getNumberOfReservationByEmail(String email);
}
