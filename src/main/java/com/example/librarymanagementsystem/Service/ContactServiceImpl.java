package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Contact;
import com.example.librarymanagementsystem.Repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ContactServiceImpl implements ContactService{


    @Autowired
    ContactRepository contactRepository;

    @Override
    public boolean AddMessage(Contact contact) throws IOException {

        try
        {
            if(contact!=null)
            {
                contactRepository.save(contact);
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

    @Override
    public void deleteMessage(Long id) {

        contactRepository.deleteById(id);

    }
}
