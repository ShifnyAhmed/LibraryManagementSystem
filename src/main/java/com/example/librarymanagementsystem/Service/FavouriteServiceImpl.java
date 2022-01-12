package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Favourite;
import com.example.librarymanagementsystem.Repository.FavouriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FavouriteServiceImpl implements FavouriteService{

    @Autowired
    FavouriteRepository favouriteRepository;

    @Override
    public void removeFromFavouriteByEmail(String email) {
        favouriteRepository.deleteAllByEmail(email);
    }

    @Override
    public void removeFromFavouriteByID(Long id) {
        favouriteRepository.deleteById(id);
    }

    @Override
    public List<Favourite> GetAllFavouritesByEmail(String email) {
        return favouriteRepository.findByEmail(email);
    }

    @Override
    public Optional<Favourite> GetFavouriteBookByID(Long id) {
        return favouriteRepository.findById(id);
    }

    @Override
    public boolean AddToFavourite(Favourite favourite) throws IOException {
        try
        {
            if(favourite!=null)
            {
                favouriteRepository.save(favourite);
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
