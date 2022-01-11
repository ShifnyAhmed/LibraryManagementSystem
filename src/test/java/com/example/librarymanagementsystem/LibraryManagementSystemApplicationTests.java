package com.example.librarymanagementsystem;

import com.example.librarymanagementsystem.Model.User;
import com.example.librarymanagementsystem.Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.prepost.PreAuthorize;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

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

    @Test
    @DisplayName("Difference of two dates in days")
    public void DifferenceBetweenTwoDatesInDays() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Date firstDate = sdf.parse("06-22-2017");
        Date secondDate = sdf.parse("06-30-2017");

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        System.out.println(diff);

        Assertions.assertEquals(8, diff);
    }


}
