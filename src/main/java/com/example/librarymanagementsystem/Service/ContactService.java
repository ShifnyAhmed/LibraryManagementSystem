package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Contact;

import java.io.IOException;

public interface ContactService {

    boolean AddMessage(Contact contact) throws IOException;

    //Admin- delete message by id
    void deleteMessage(Long id);
}
