package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Favourite;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FavouriteService {

    void removeFromFavouriteByEmail(String email);

    void removeFromFavouriteByID(Long id);

    List<Favourite> GetAllFavouritesByEmail(String email);

    Optional<Favourite> GetFavouriteBookByID(Long id);

    boolean AddToFavourite(Favourite favourite) throws IOException;

    Optional<Favourite> CheckIfBook_IsAlreadyAddedTo_Favorite(String bookname,String email);

}
