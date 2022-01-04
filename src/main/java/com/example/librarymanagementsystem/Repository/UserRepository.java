package com.example.librarymanagementsystem.Repository;

import com.example.librarymanagementsystem.Model.Role;
import com.example.librarymanagementsystem.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findDetailsByEmail(String email);

    User findByEmail(String email);

    List<User> findAllByBlacklist(String blacklist);

}
