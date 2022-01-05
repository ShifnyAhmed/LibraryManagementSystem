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

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to AddToBlacklistAdmin html page
        return "AddToBlacklistAdmin";
    }


//------------------------------------------------------------------------------------------

    //updates the blacklist from no to yes
    @PostMapping (value = "/admin/addtoblacklist")
    public String addToBlacklist(@RequestParam("user_id")Long id,
                                 @RequestParam("blacklist") String blacklist
                                 ,Model model)
    {
            try {

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

//------------------------------------------------------------------------------------------

    //This is a confirmation page to Promote member level
    @GetMapping(value = "/admin/promotememberpage/{id}")
    public String PromoteMemberButton(@PathVariable("id") Long id, Model model)
    {
        Optional<User> promote_member_profile = userRepository.findById(id);

        model.addAttribute("id",promote_member_profile.get().getId());
        model.addAttribute("name",promote_member_profile.get().getFullname());
        model.addAttribute("email",promote_member_profile.get().getEmail());
        model.addAttribute("contact",promote_member_profile.get().getMobile());
        model.addAttribute("password",promote_member_profile.get().getPassword());
        model.addAttribute("dateofbirth",promote_member_profile.get().getDateofbirth());
        model.addAttribute("level",promote_member_profile.get().getLevel());
        model.addAttribute("blacklist",promote_member_profile.get().getBlacklist());

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to PromoteMemberAdmin html page
        return "PromoteMemberAdmin";
    }

//------------------------------------------------------------------------------------------

    //updates the member level(bronze,sliver,platinum,gold)
    @PostMapping (value = "/admin/promotemember")
    public String promoteMember(@RequestParam("user_id")Long id,
                                 @RequestParam("level") String level
            ,Model model)
    {
        try {

            userService.promoteMember(level,id);

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails=(UserDetails)authentication.getPrincipal();
            model.addAttribute("useremail",userDetails);
        }
        catch (Exception e){
            return "redirect:/admin/viewallmembers?unsuccess";
        }

        return "redirect:/admin/viewallmembers?promotionsuccess";
    }

//------------------------------------------------------------------------------------------

    //This is a confirmation page to delete the selected member
    @GetMapping(value = "/admin/deletepage/{id}")
    public String RemoveButton(@PathVariable("id") Long id, Model model)
    {
        Optional<User> delete_profile = userRepository.findById(id);

        model.addAttribute("id",delete_profile.get().getId());
        model.addAttribute("name",delete_profile.get().getFullname());
        model.addAttribute("email",delete_profile.get().getEmail());
        model.addAttribute("contact",delete_profile.get().getMobile());
        model.addAttribute("password",delete_profile.get().getPassword());
        model.addAttribute("dateofbirth",delete_profile.get().getDateofbirth());
        model.addAttribute("level",delete_profile.get().getLevel());
        model.addAttribute("blacklist",delete_profile.get().getBlacklist());

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to EditProfile html page
        return "DeleteMemberAdmin";
    }

    //    -------------------------------------------------------------------------------------------------

    //this deletes the selected user from database
    @GetMapping(value = "/admin/deletemember/{id}")
    public String deleteMember(@RequestParam("user_id") Long id)
    {
        userService.deleteMember(id);
        return  "redirect:/admin/viewallmembers?deletemembersuccess";
    }

    //------------------------------------------------------------------------------------------

    //This is a confirmation page to remove the selected user from blacklist
    @GetMapping(value = "/admin/removeblacklistpage/{id}")
    public String RemoveFromBlacklistButton(@PathVariable("id") Long id, Model model)
    {
        Optional<User> Non_blacklist_member_profile = userRepository.findById(id);

        model.addAttribute("id",Non_blacklist_member_profile.get().getId());
        model.addAttribute("name",Non_blacklist_member_profile.get().getFullname());
        model.addAttribute("email",Non_blacklist_member_profile.get().getEmail());
        model.addAttribute("contact",Non_blacklist_member_profile.get().getMobile());
        model.addAttribute("password",Non_blacklist_member_profile.get().getPassword());
        model.addAttribute("dateofbirth",Non_blacklist_member_profile.get().getDateofbirth());
        model.addAttribute("level",Non_blacklist_member_profile.get().getLevel());
        model.addAttribute("blacklist",Non_blacklist_member_profile.get().getBlacklist());

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to AddToBlacklistAdmin html page
        return "RemoveFromBlacklistAdmin";
    }

    //------------------------------------------------------------------------------------------

    //updates the blacklist from yes to no
    @PostMapping (value = "/admin/removefromblacklist")
    public String RemoveFromBlacklist(@RequestParam("user_id")Long id,
                                 @RequestParam("blacklist") String blacklist
            ,Model model)
    {
        try {

            userService.update(blacklist,id);

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails=(UserDetails)authentication.getPrincipal();
            model.addAttribute("useremail",userDetails);
        }
        catch (Exception e){

            return "redirect:/admin/viewallmembers?unsuccess";
        }

        return "redirect:/admin/viewallmembers?removeblacklistsuccess";
    }

}
