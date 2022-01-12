package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Returned;
import com.example.librarymanagementsystem.Repository.ReturnedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReturnedServiceImpl implements ReturnedService{

    @Autowired
    ReturnedRepository returnedRepository;


    @Override
    public String saveReturnedReservation(Returned returned) {
        if(returned != null)
        {
            returnedRepository.save(returned);
        }
        else
        {
            return "Error";
        }
        return "Error";
    }

    @Override
    public List<Returned> getReturnedReservationByStatus(String status) {
        return returnedRepository.findByStatus(status);
    }

    @Override
    public void deleteReturnedReservationById(Long id) {
                returnedRepository.deleteById(id);
    }

    @Override
    public Optional<Returned> getReturnedReservationByID(Long id) {
        return returnedRepository.findById(id);
    }

    @Override
    public List<Returned> getReturnedReservationByEmailAndStatus(String email, String status) {
        return returnedRepository.findByEmailAndStatus(email, status);
    }
}
