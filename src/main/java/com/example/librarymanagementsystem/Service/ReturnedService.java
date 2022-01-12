package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Returned;

import java.util.List;
import java.util.Optional;

public interface ReturnedService {

    String saveReturnedReservation(Returned returned);

    List<Returned> getReturnedReservationByStatus(String status);

    void deleteReturnedReservationById(Long id);

    Optional<Returned> getReturnedReservationByID(Long id);

    List<Returned> getReturnedReservationByEmailAndStatus(String email, String status);
}
