package com.example.librarymanagementsystem.Controller;

import com.example.librarymanagementsystem.Model.User;
import com.example.librarymanagementsystem.Repository.UserRepository;
import com.example.librarymanagementsystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

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

        //redirecting to ViewAllMembersAdmin html page
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

        //redirecting to ViewBlackListMembersAdmin html page
        return "ViewBlackListMembersAdmin";
    }

//------------------------------------------------------------------------------------------

    //This is a confirmation page to add the selected user to blacklist
    @GetMapping(value = "/admin/addblacklistpage/{id}")
    public String AddToBlacklistButton(@PathVariable("id") Long id, Model model)
    {
        Optional<User> blacklist_member_profile = userRepository.findById(id);

        model.addAttribute("id",blacklist_member_profile.get().getId());
        model.addAttribute("name",blacklist_member_profile.get().getFullname());
        model.addAttribute("email",blacklist_member_profile.get().getEmail());
        model.addAttribute("contact",blacklist_member_profile.get().getMobile());
        model.addAttribute("password",blacklist_member_profile.get().getPassword());
        model.addAttribute("dateofbirth",blacklist_member_profile.get().getDateofbirth());
        model.addAttribute("level",blacklist_member_profile.get().getLevel());
        model.addAttribute("blacklist",blacklist_member_profile.get().getBlacklist());

        //redirecting to AddToBlacklistAdmin html page
        return "AddToBlacklistAdmin";
    }


//------------------------------------------------------------------------------------------

    //updates the blacklist from no to yes
    @PostMapping (value = "/admin/addtoblacklist")
    public String addToBlacklist(@Valid User user,
                                 @RequestParam("user_id")Long id, @RequestParam("email")String email,
                                 @RequestParam("name") String name,
                                 @RequestParam("contact") String contact,
                                 @RequestParam("password") String password,
                                 @RequestParam("dateofbirth") String dateofbirth,
                                 @RequestParam("level") String level,
                                 @RequestParam("blacklist") String blacklist
                                 ,Model model)
    {
            try {
//                user.setId(id);
//                user.setFullname(name);
//                user.setEmail(email);
//                user.setMobile(contact);
//                user.setPassword(password);
//                user.setDateofbirth(dateofbirth);
//                user.setLevel(level);
//                user.setBlacklist(blacklist);
//
//                userService.saveOrUpdate(user);

                userService.update(blacklist,id);

                Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
                UserDetails userDetails=(UserDetails)authentication.getPrincipal();
                model.addAttribute("useremail",userDetails);
            }
            catch (Exception e){

                return "redirect:/admin/viewallmembers?unsuccess";
            }




        return "redirect:/admin/viewallmembers?blacklistsuccess";
    }

}
