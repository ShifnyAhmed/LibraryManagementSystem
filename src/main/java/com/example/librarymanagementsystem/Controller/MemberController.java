package com.example.librarymanagementsystem.Controller;

import com.example.librarymanagementsystem.Model.Notification;
import com.example.librarymanagementsystem.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MemberController {

    @Autowired
    NotificationService notificationService;

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

}
