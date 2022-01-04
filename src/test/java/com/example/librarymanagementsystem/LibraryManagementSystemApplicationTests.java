package com.example.librarymanagementsystem;

import com.example.librarymanagementsystem.Model.User;
import com.example.librarymanagementsystem.Repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class LibraryManagementSystemApplicationTests {


    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("All Non Blacklist Members")
    public void TestGetAllNonBlacklistMembers()
    {


        List<User> adminMemberList = userRepository.findAllByBlacklist("No");

    }




}
