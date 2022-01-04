package com.example.librarymanagementsystem.Controller;

import com.example.librarymanagementsystem.Model.RegistrationDTO;
import com.example.librarymanagementsystem.Repository.UserRepository;
import com.example.librarymanagementsystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    public UserRegistrationController(UserService userService) {
        super();
        this.userService = userService;
    }

    @ModelAttribute("user")
    public RegistrationDTO registrationDTO() {

        return new RegistrationDTO();

    }

    //--------------------------------------------------------------------------------------
    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid RegistrationDTO registrationDto, BindingResult bindingResult)
    {

        try{
            if (CheckIfUserExists(registrationDto.getEmail()))
            {
                return "redirect:/registration?unsuccess";
            }
            else {
                userService.save(registrationDto);
                return "redirect:/registration?success";
            }
        }
        catch (Exception e){
            return "redirect:/registration?error";
        }

    }

    public boolean CheckIfUserExists(String email) {

        //returns true if there is user in the database with the same email
        return userRepository.findByEmail(email) != null;

    }



}
