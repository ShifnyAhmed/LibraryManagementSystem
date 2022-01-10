package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Favourite;

import java.util.List;

public interface FavouriteService {

    void removeFromFavouriteByEmail(String email);

    List<Favourite> GetAllFavouritesByEmail(String email);

}
