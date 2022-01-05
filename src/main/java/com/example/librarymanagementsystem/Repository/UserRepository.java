package com.example.librarymanagementsystem.Repository;

import com.example.librarymanagementsystem.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findDetailsByEmail(String email);

    User findByEmail(String email);

    List<User> findAllByBlacklist(String blacklist);

    //This runs a nativeSql query bcz the usual update function of JPARepo is more similar to save function which removes the user role on update
    @Modifying
    @Query(value = "UPDATE user u set blacklisted =?1 where u.id = ?2",
            nativeQuery = true)
    void updateUser(@Param("blacklist") String blacklist, @Param("id") Long id);


    //used to promote the member
    @Modifying
    @Query(value = "UPDATE user u set level =?1 where u.id = ?2",
            nativeQuery = true)
    void updateUserLevel(@Param("level") String level, @Param("id") Long id);

}
