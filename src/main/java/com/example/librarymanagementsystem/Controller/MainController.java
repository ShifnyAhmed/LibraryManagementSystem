package com.example.librarymanagementsystem.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/login")
    public String login(Model model)
    {
        //this redirects user to the login page
        return "login";
    }

    @GetMapping("/")
    public String home(Model model)
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //this redirects user to the home page, home.html will check the role of the user and display the homepage based on the role
        return "home";
    }

    @GetMapping("/logout")
    public String logout(Model model)
    {
        //this logs out the user
        return "logout";
    }

//    @GetMapping("/error")
//    public String Error(Model model)
//    {
//        //this gets displayed when error occurs
//        return "error";
//    }

}
