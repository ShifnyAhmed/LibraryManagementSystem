package com.example.librarymanagementsystem.Controller;

import com.example.librarymanagementsystem.Model.*;
import com.example.librarymanagementsystem.Repository.BookRepository;
import com.example.librarymanagementsystem.Repository.UserRepository;
import com.example.librarymanagementsystem.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class MemberController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    ContactService contactService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ReservationService reservationService;

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

//    ------------------------------------------------------------------------------------------------

    //displays all details of the logged in user- view profile
    @RequestMapping(value = "/user/viewprofile/{email}")
    public String viewProfile(@PathVariable("email") String email, Model model)
    {
        User view_profile = userService.getUserByEmail(email);

        model.addAttribute("view_profile",view_profile);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to ViewProfile html page
        return "ViewProfileMember";
    }

    //    ------------------------------------------------------------------------------------------------

    //Gets the user details and adding it to a model to access it in EditProfile page
    @GetMapping(value = "/user/editprofile/{id}")
    public String EditProfileButton(@PathVariable("id") Long id, Model model)
    {
        Optional<User> view_profile = userRepository.findById(id);

        model.addAttribute("id",view_profile.get().getId());
        model.addAttribute("name",view_profile.get().getFullname());
        model.addAttribute("email",view_profile.get().getEmail());
        model.addAttribute("contact",view_profile.get().getMobile());
        model.addAttribute("password",view_profile.get().getPassword());
        model.addAttribute("dob",view_profile.get().getDateofbirth());

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to EditProfile html page
        return "EditProfileMember";
    }

//    -------------------------------------------------------------------------------------------------


    //Updates the user details with entered values
    @PostMapping(value = "/user/updateuser")
    public String UpdateUser(@RequestParam("user_id")Long id, @RequestParam("email")String email,
                             @RequestParam("name") String name,
                             @RequestParam("contact") String contact,
                             @RequestParam("dateofbirth") String dateofbirth,
                             @RequestParam("dateofbirthold") String dateofbirth_old,Model model) {


        try {

            if(dateofbirth.isEmpty())
            {
                //updating the user details
                userService.EditProfile(name,contact,email,dateofbirth_old,id);

            }
            else{

                //updating the user details
                userService.EditProfile(name,contact,email,dateofbirth,id);


            }

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails=(UserDetails)authentication.getPrincipal();
            model.addAttribute("useremail",userDetails);

        } catch (Exception e) {

            e.printStackTrace();
            return "ViewProfileMember";

        }

        //redirecting to ViewProfileMember html page
        return "ViewProfileMember";
    }

//    -------------------------------------------------------------------------------------------------

    @GetMapping(value = "/user/viewallbooks")
    public String viewAllBooks(Model model)
    {

        List<Book> member_all_books = bookService.getAllBooks();

        model.addAttribute("member_all_books",member_all_books);
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        return "ViewAllBooksMember";
    }

    //    ------------------------------------------------------------------------------------------------

    //display book details page
    @GetMapping(value = "/user/bookdetailspage/{id}")
    public String ViewBookDetailsButton(@PathVariable("id") Long id, Model model)
    {
        Optional<Book> view_book_details = bookRepository.findById(id);

        model.addAttribute("view_book_details",view_book_details);


        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to ViewBookDetailsMember html page
        return "ViewBookDetailsMember";
    }

    //    ------------------------------------------------------------------------------------------------


    //read book online
    @GetMapping(value = "/user/readbook/{id}")
    public String ReadBookButton(@PathVariable("id") Long id, Model model)
    {
        Optional<Book> read_online = bookRepository.findById(id);

        model.addAttribute("pdf_name",read_online.get().getPdfName());


        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to ReadOnlineMember html page
        return "ReadOnlineMember";
    }

    //    ------------------------------------------------------------------------------------------------

    //reserve book page
    @GetMapping(value = "/user/reservebookpage/{id}")
    public String ReserveBookButton(@PathVariable("id") Long id, Model model)
    {
        Optional<Book> reserve_book = bookRepository.findById(id);

        model.addAttribute("book_id",reserve_book.get().getId());
        model.addAttribute("book_name",reserve_book.get().getBookname());

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        User reserveing_user = userService.getUserByEmail(userDetails.getUsername());

       String blacklist = reserveing_user.getBlacklist();

       //blacklisted members are restricted to reserve books
       if (Objects.equals(blacklist, "Yes"))
       {
           return "redirect:/user/viewallbooks?blacklisted";
       }
       else
       {
           return "ReserveBookMember";
       }


    }


//    -------------------------------------------------------------------------------------------------

    //this will save the reservation as pending to reservation table in database
    @PostMapping(value = "/user/reservebook")
    public String reserveBook(@Valid Reservation reservation,
                             @RequestParam("book_id") Long book_id,
                             @RequestParam("book_name") String book_name,
                              @RequestParam("reserved_date") String reserved_date ,Model model) {

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails=(UserDetails)authentication.getPrincipal();
            model.addAttribute("useremail",userDetails);

            try {
            User reserveing_user = userService.getUserByEmail(userDetails.getUsername());

            String reverving_user_email = userDetails.getUsername();
            String blacklist = reserveing_user.getBlacklist();
            String current_status = "Pending";
            String level = reserveing_user.getLevel();

            //blacklisted members are restricted to reserve books
            if (Objects.equals(blacklist, "Yes"))
            {
                return "redirect:/user/viewallbooks?blacklisted";
            }
            else{

                //member level checking
                    if(Objects.equals(level, "Bronze"))
                    {
                        reservation.setLending_charges(50);
                        reservation.setLending_duration(3);
                        reservation.setOverdue_charges(20);

                        String allowed_return_date = reserved_date;  // Start date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        c.setTime(sdf.parse(allowed_return_date));
                        c.add(Calendar.DATE, 21);  // number of days to add
                        allowed_return_date = sdf.format(c.getTime());  // allowed_return_date is now the new date

                        reservation.setAllowed_return_date(allowed_return_date);

                        model.addAttribute("lending_charges","50");
                        model.addAttribute("lending_duration","3");
                        model.addAttribute("overdue_charges","20");
                        model.addAttribute("allowed_return_date",allowed_return_date);
                        model.addAttribute("overdue_charges","20");

                    }
                    else if(Objects.equals(level, "Silver"))
                    {
                        reservation.setLending_charges(40);
                        reservation.setLending_duration(4);
                        reservation.setOverdue_charges(15);

                        String allowed_return_date = reserved_date;  // Start date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        c.setTime(sdf.parse(allowed_return_date));
                        c.add(Calendar.DATE, 28);  // number of days to add
                        allowed_return_date = sdf.format(c.getTime());  // allowed_return_date is now the new date

                        reservation.setAllowed_return_date(allowed_return_date);

                        model.addAttribute("lending_charges","40");
                        model.addAttribute("lending_duration","4");
                        model.addAttribute("overdue_charges","15");
                        model.addAttribute("allowed_return_date",allowed_return_date);
                        model.addAttribute("overdue_charges","15");
                    }
                    else if(Objects.equals(level, "Gold"))
                    {
                        reservation.setLending_charges(30);
                        reservation.setLending_duration(4);
                        reservation.setOverdue_charges(10);

                        String allowed_return_date = reserved_date;  // Start date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        c.setTime(sdf.parse(allowed_return_date));
                        c.add(Calendar.DATE, 28);  // number of days to add
                        allowed_return_date = sdf.format(c.getTime());  // allowed_return_date is now the new date

                        reservation.setAllowed_return_date(allowed_return_date);

                        model.addAttribute("lending_charges","30");
                        model.addAttribute("lending_duration","4");
                        model.addAttribute("overdue_charges","10");
                        model.addAttribute("allowed_return_date",allowed_return_date);
                        model.addAttribute("overdue_charges","10");
                    }
                    else if(Objects.equals(level, "Platinum"))
                    {
                        reservation.setLending_charges(20);
                        reservation.setLending_duration(5);
                        reservation.setOverdue_charges(5);

                        String allowed_return_date = reserved_date;  // Start date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        c.setTime(sdf.parse(allowed_return_date));
                        c.add(Calendar.DATE, 35);  // number of days to add
                        allowed_return_date = sdf.format(c.getTime());  // allowed_return_date is now the new date

                        reservation.setAllowed_return_date(allowed_return_date);

                        model.addAttribute("lending_charges","20");
                        model.addAttribute("lending_duration","4");
                        model.addAttribute("overdue_charges","5");
                        model.addAttribute("allowed_return_date",allowed_return_date);
                        model.addAttribute("overdue_charges","5");
                    }

                reservation.setEmail(reverving_user_email);
                reservation.setStatus(current_status);
                reservation.setBook_id(book_id);
                reservation.setBook_name(book_name);
                reservation.setReserved_date(reserved_date);

                        reservationService.saveReservation(reservation);


            }//end of (blacklist check) else


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        model.addAttribute("book_name",book_name);
        model.addAttribute("status","Pending");
        model.addAttribute("reserved_date",reserved_date);
        model.addAttribute("member_email",userDetails.getUsername());


        return "ReservationReceiptMember";
    }

//    -------------------------------------------------------------------------------------------------

    //Displays all the pending reservations of the logged in member
    @RequestMapping(value = "/user/viewpendingreservations/{email}/{status}")
    public String viewMyPendingReservationlist(@PathVariable ("email")String email, @PathVariable("status") String status, Model model)
    {
        List<Reservation> my_pending_reservations = reservationService.getReservationByEmailAndStatus(email, status);

        model.addAttribute("my_pending_reservations",my_pending_reservations);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to PendingReservationsMember html page
        return "PendingReservationsMember";
    }

//    -------------------------------------------------------------------------------------------------

    //Displays all the approved reservations of the logged in member
    @RequestMapping(value = "/user/viewapprovedreservations/{email}/{status}")
    public String viewMyApprovedReservationlist(@PathVariable ("email")String email, @PathVariable("status") String status, Model model)
    {
        List<Reservation> my_approved_reservations = reservationService.getReservationByEmailAndStatus(email, status);

        model.addAttribute("my_approved_reservations",my_approved_reservations);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to PendingReservationsMember html page
        return "ApprovedReservationsMember";
    }

}
