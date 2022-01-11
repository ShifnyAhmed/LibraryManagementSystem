package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Returned;

import java.util.List;

public interface ReturnedService {

    String saveReturnedReservation(Returned returned);

    List<Returned> getReturnedReservationByStatus(String status);

    void deleteReturnedReservationById(Long id);

    List<Returned> getReturnedReservationByEmailAndStatus(String email, String status);
}
