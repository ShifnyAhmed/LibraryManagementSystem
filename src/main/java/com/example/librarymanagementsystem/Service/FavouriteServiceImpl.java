package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Favourite;
import com.example.librarymanagementsystem.Repository.FavouriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavouriteServiceImpl implements FavouriteService{

    @Autowired
    FavouriteRepository favouriteRepository;

    @Override
    public void removeFromFavouriteByEmail(String email) {
        favouriteRepository.deleteAllByEmail(email);
    }

    @Override
    public List<Favourite> GetAllFavouritesByEmail(String email) {
        return favouriteRepository.findByEmail(email);
    }
}
