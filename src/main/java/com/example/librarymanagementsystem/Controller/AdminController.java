package com.example.librarymanagementsystem.Controller;

import com.example.librarymanagementsystem.Model.User;
import com.example.librarymanagementsystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    UserRepository userRepository;

    //displays all the members of showcase bookstore who are not blacklisted
    @RequestMapping(value = "/admin/viewallmembers")
    public String viewAllUsers(Model model)
    {
        String blacklist="No";
        List<User> adminMemberList = userRepository.findAllByBlacklist(blacklist);

        model.addAttribute("adminMemberList",adminMemberList);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to ViewAllUsersAdmin html page
        return "ViewAllMembersAdmin";
    }

//------------------------------------------------------------------------------------------

    //displays all blacklisted members
    @RequestMapping(value = "/admin/viewblacklistmembers")
    public String viewAllBlacklistedUsers(Model model)
    {
        String blacklist="Yes";
        List<User> adminBlackList = userRepository.findAllByBlacklist(blacklist);

        model.addAttribute("adminBlackList",adminBlackList);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to ViewAllUsersAdmin html page
        return "ViewBlackListMembersAdmin";
    }

}
