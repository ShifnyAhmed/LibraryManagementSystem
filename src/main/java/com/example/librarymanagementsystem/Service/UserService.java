package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.RegistrationDTO;
import com.example.librarymanagementsystem.Model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

//user detail service - used as DAO for user authentication
public interface UserService extends UserDetailsService {

    //to get user linked with the email provided
    User getUserByEmail(String email);

    //to save user to database- registration process
    User save(RegistrationDTO registrationDTO);

    void update(String blacklist, Long id);

    void promoteMember(String level, Long id);

    void EditProfile(String fullname,String mobile,String email, String dateofbirth, Long id);


    void saveOrUpdate(User user);

    //Admin- delete user by id
    void deleteMember(Long id);


}
