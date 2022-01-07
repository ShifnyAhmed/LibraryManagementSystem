package com.example.librarymanagementsystem.Controller;

import com.example.librarymanagementsystem.Model.Contact;
import com.example.librarymanagementsystem.Model.Notification;
import com.example.librarymanagementsystem.Service.ContactService;
import com.example.librarymanagementsystem.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class MemberController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    ContactService contactService;

//    -------------------------------------------------------------------------------------------------

    //Displays all notifications sent to currently logged in member email
    @RequestMapping(value = "/user/viewmynotification/{email}")
    public String viewMyNotification(@PathVariable("email")String email,Model model)
    {
        List<Notification> member_notification = notificationService.ViewAllNotificationByEmail(email);

        model.addAttribute("member_notification",member_notification);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to OnTheWayOrdersUser html page
        return "ViewNotificationMember";
    }
//    -------------------------------------------------------------------------------------------------

    //this deletes all notifications sent to currently logged in user
    @GetMapping(value = "/user/clearallnotification/{email}")
    public String ClearAllNotificationOfLoggedInUser(@PathVariable("email") String email,Model model) {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        try {
            notificationService.ClearAllNotificationByEmail(email);

            return "ViewNotificationMember";

        } catch (Exception e) {

            e.printStackTrace();
            return "ViewNotificationMember";
        }
    }

//    -------------------------------------------------------------------------------------------------

    //Display Contact Admin Page
    @GetMapping(value = "/user/contactadminpage")
    public String ContactAdminPage(Model model)
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        return "ContactAdminMember";
    }


    //    ------------------------------------------------------------------------------------------------

    //sends the message entered by currently logged in member to admin with the date and time
    @PostMapping(value = "/user/contactadmin")
    public String contactAdmin(@Valid Contact contact, @RequestParam("msg") String msg) {

        try {

            if (contact == null) {

                return "redirect:/user/contactadminpage?contactunsuccess";

            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();


            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.now();

            //setting email
            contact.setEmail(userDetails.getUsername());
            //setting message
            contact.setMessage(msg);
            //setting date
            contact.setDate(dateTimeFormatter.format(localDateTime));

            //adds the message to contact table in database
            boolean message = contactService.AddMessage(contact);
            if (message) {

                return "redirect:/user/contactadminpage?contactsuccess";

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/user/contactadminpage?contactunsuccess";
    }
}
