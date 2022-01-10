package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Reservation;
import com.example.librarymanagementsystem.Repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ReservationRepository reservationRepository;


    //overided methods
    @Override
    public List<Reservation> getReservationByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }

    @Override
    public void getNumberOfReservationByEmail(String email) {

        reservationRepository.countReservationByEmail(email);
    }
}
