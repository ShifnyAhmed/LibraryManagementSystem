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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    ReturnedService returnedService;

    @Autowired
    FavouriteService favouriteService;

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

            //this is used to get the allowed return date for each member level
                String allowed_return_date = reserved_date;  // Start date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();

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

                long num_of_reservations = reservationService.getNumberOfReservationByEmail(reverving_user_email);

                if (Objects.equals(level, "Bronze"))
                {
                    if(num_of_reservations >= 3)
                    {
                        return "redirect:/user/viewallbooks?bronzeerror";
                    }
                    else {reservationService.saveReservation(reservation);}

                }
                else if (Objects.equals(level, "Silver"))
                {
                    if(num_of_reservations >= 5)
                    {
                        return "redirect:/user/viewallbooks?silvererror";
                    }
                    else {reservationService.saveReservation(reservation);}

                }
                else if (Objects.equals(level, "Gold"))
                {
                    if(num_of_reservations >= 7)
                    {
                        return "redirect:/user/viewallbooks?golderror";
                    }
                    else {reservationService.saveReservation(reservation);}

                }
                else if (Objects.equals(level, "Platinum"))
                {
                    if(num_of_reservations >= 10)
                    {
                        return "redirect:/user/viewallbooks?platinumerror";
                    }
                    else {reservationService.saveReservation(reservation);}

                }



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

//    -------------------------------------------------------------------------------------------------

    @GetMapping(value = "/user/returnbookpage/{id}")
    public String ReturnBookButton(@PathVariable("id") Long id, Model model)
    {
        Optional<Reservation> return_book_details = reservationService.getReservationByID(id);

        model.addAttribute("reservation_id",return_book_details.get().getId());
        model.addAttribute("book_id",return_book_details.get().getBook_id());
        model.addAttribute("member_email",return_book_details.get().getEmail());
        model.addAttribute("bookname",return_book_details.get().getBook_name());
        model.addAttribute("reserved_date",return_book_details.get().getReserved_date());
        model.addAttribute("lending_duration",return_book_details.get().getLending_duration());
        model.addAttribute("lending_charges",return_book_details.get().getLending_charges());
        model.addAttribute("allowed_return_date",return_book_details.get().getAllowed_return_date());
        model.addAttribute("overdue_charges",return_book_details.get().getOverdue_charges());

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);


        return "ReturnBookMember";
    }

//    -------------------------------------------------------------------------------------------------

    //This deletes the selected row from reservation page and adds row with same and few additional details in returned table
    @PostMapping(value = "/user/returnbook")
    public String ReturnBook(@Valid Notification notification,
                              @Valid Returned returned,
                              @RequestParam("reservation_id")Long reservation_id,
                              @RequestParam("book_id")Long book_id,
                              @RequestParam("member_email")String member_email,
                              @RequestParam("book_name") String book_name,
                              @RequestParam("lending_duration") String lending_duration,
                              @RequestParam("lending_charges")String lending_charges,
                              @RequestParam("overdue_charges")String overdue_charges,
                              @RequestParam("reserved_date")String reserved_date,
                              @RequestParam("allowed_return_date")String allowed_return_date
            , Model model) {

        try {
            DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            LocalDateTime localDateTime = LocalDateTime.now();

            Date reserved_date_date = sdf.parse(reserved_date);
            Date should_return_date = sdf.parse(allowed_return_date);
            Date returned_date = sdf.parse(dateTimeFormatter.format(localDateTime));

            model.addAttribute("returned_on",dateTimeFormatter.format(localDateTime));

            //if book was marked returned after the reserved date
            if(returned_date.after(reserved_date_date))
            {

                //if the book is marked returned after the allowed returned date
                if(returned_date.after(should_return_date))
                {
                    long diffInMillies = Math.abs(returned_date.getTime() - should_return_date.getTime());
                    long diff_In_Days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                    int total_overdue = (int) ((Integer.parseInt(overdue_charges))*diff_In_Days);

                    int lending_charges_int = Integer.parseInt(lending_charges);
                    int total_payable = total_overdue+lending_charges_int;


                    returned.setTotal(total_payable);
                    returned.setBook_id(book_id);
                    returned.setBook_name(book_name);
                    returned.setEmail(member_email);
                    returned.setLending_duration(Integer.parseInt(lending_duration));
                    returned.setLending_charges(Integer.parseInt(lending_charges));
                    returned.setOverdue_charges(Integer.parseInt(overdue_charges));
                    returned.setReserved_date(reserved_date);
                    returned.setAllowed_return_date(allowed_return_date);
                    returned.setStatus("Checking Returned");
                    returned.setActual_return_date(dateTimeFormatter.format(localDateTime));

                    returnedService.saveReturnedReservation(returned);
                    reservationService.deleteReservationById(reservation_id);

                    notification.setDate(dateTimeFormatter2.format(localDateTime));
                    notification.setEmail(member_email);
                    notification.setMessage("The Book you marked as returned is now being checked by the admin, you will be notified once the admin has confirmed the book as returned");
                    notificationService.AddNotification(notification);

                    model.addAttribute("total",total_payable);
                    model.addAttribute("total_overdue",total_overdue);
                    model.addAttribute("overdue_days",diff_In_Days);

                }
                else
                {
                    //if the book is marked returned between the allowed returned date and reserved date

                    int total_payable = Integer.parseInt(lending_charges);
                    int total_overdue = 0;

                    returned.setTotal(total_payable);
                    returned.setBook_id(book_id);
                    returned.setBook_name(book_name);
                    returned.setEmail(member_email);
                    returned.setLending_duration(Integer.parseInt(lending_duration));
                    returned.setLending_charges(Integer.parseInt(lending_charges));
                    returned.setOverdue_charges(Integer.parseInt(overdue_charges));
                    returned.setReserved_date(reserved_date);
                    returned.setAllowed_return_date(allowed_return_date);
                    returned.setStatus("Checking Returned");
                    returned.setActual_return_date(dateTimeFormatter.format(localDateTime));

                    returnedService.saveReturnedReservation(returned);
                    reservationService.deleteReservationById(reservation_id);

                    notification.setDate(dateTimeFormatter2.format(localDateTime));
                    notification.setEmail(member_email);
                    notification.setMessage("The Book you marked as returned is now being checked by the admin, you will be notified once the admin has confirmed the book as returned");
                    notificationService.AddNotification(notification);

                    model.addAttribute("total",total_payable);
                    model.addAttribute("total_overdue",total_overdue);
                    model.addAttribute("overdue_days",0);

                }
                model.addAttribute("reservation_id",reservation_id);
                model.addAttribute("book_id",book_id);
                model.addAttribute("member_email",member_email);
                model.addAttribute("bookname",book_name);
                model.addAttribute("reserved_date",reserved_date);
                model.addAttribute("lending_duration",lending_duration);
                model.addAttribute("lending_charges",lending_charges);
                model.addAttribute("allowed_return_date",allowed_return_date);
                model.addAttribute("overdue_charges",overdue_charges);
                return "ReturnedReceiptMember";

            }
            else if(returned_date.equals(reserved_date_date)){

                //if book was marked returned on the same day the book was reserved

                return "redirect:/user/viewapprovedreservations/"+member_email+"/Approved?samedateerror";
            }
            else
            {
                //if book was marked returned before the reserved date
                return "redirect:/user/viewapprovedreservations/"+member_email+"/Approved?beforedateerror";
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "redirect:/user/viewapprovedreservations/"+member_email+"/Approved?error";
        }


    }


//    -------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/user/viewreturnedreservations/{email}/{status}")
    public String ViewReservationHistory(@PathVariable("status") String status,@PathVariable("email") String email, Model model)
    {
        List<Returned> returned_reservation_history = returnedService.getReturnedReservationByEmailAndStatus(email, status);

        model.addAttribute("returned_reservation_history",returned_reservation_history);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        return "ReservationHistoryMember";
    }

    //    -------------------------------------------------------------------------------------------------

    @GetMapping(value = "/user/addtofavourite/{id}")
    public String AddToFavourite(@Valid Notification notification,
                                 @Valid Favourite favourite,
                                 @PathVariable("id") Long id, Model model) {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        try{

            Optional<Book> favourite_book = bookRepository.findById(id);

            long book_id = favourite_book.get().getId();
            String book_name = favourite_book.get().getBookname();
            String member_email = userDetails.getUsername();
            String book_author = favourite_book.get().getAuthor();
            String image_file_name = favourite_book.get().getFileName();
            String pdf_file_name = favourite_book.get().getPdfName();
            String book_category = favourite_book.get().getCategory();

            favourite.setBookid(book_id);
            favourite.setBookname(book_name);
            favourite.setEmail(member_email);
            favourite.setAuthor(book_author);
            favourite.setCategory(book_category);
            favourite.setImage_file_name(image_file_name);
            favourite.setPdf_file_name(pdf_file_name);


           Optional<Favourite> already_exist = favouriteService.CheckIfBook_IsAlreadyAddedTo_Favorite(book_name,member_email);

           if(already_exist.isPresent()){

               return "redirect:/user/viewallbooks?alreadyexist";

           }
           else
           {
                 boolean check = favouriteService.AddToFavourite(favourite);

            String message;
           if(check)
           {
               message = "Book: "+book_name+" is added to your favourites.";
               DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
               LocalDateTime localDateTime = LocalDateTime.now();

               notification.setDate(dateTimeFormatter2.format(localDateTime));
               notification.setEmail(member_email);
               notification.setMessage(message);
               notificationService.AddNotification(notification);
               return "redirect:/user/viewallbooks?favouritesuccess";
           }
           else
           {
               message = "Book: "+book_name+" couldn't be added to favourites, try informing the issue to admin.";
               DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
               LocalDateTime localDateTime = LocalDateTime.now();

               notification.setDate(dateTimeFormatter2.format(localDateTime));
               notification.setEmail(member_email);
               notification.setMessage(message);
               notificationService.AddNotification(notification);
               return "redirect:/user/viewallbooks?favouritefailure";
           }





        }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "redirect:/user/viewallbooks?favouritefailure";
        }

    }

//   -------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/user/viewmyfavourites/{email}")
    public String ViewFavourites(@PathVariable("email") String email, Model model)
    {
        List<Favourite> view_my_favourite_books = favouriteService.GetAllFavouritesByEmail(email);

        model.addAttribute("view_my_favourite_books",view_my_favourite_books);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        return "ViewFavouritesMember";
    }

//   -------------------------------------------------------------------------------------------------

    //this deletes the selected message
    @GetMapping(value = "/user/removefromfavourites/{id}")
    public String RemoveFromFavourites(@Valid Notification notification,@PathVariable("id") Long id, Model model) throws IOException {

        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);
        String member_email = userDetails.getUsername();

        try
        {
            Optional<Favourite> favourite = favouriteService.GetFavouriteBookByID(id);

            String book_name = favourite.get().getBookname();

            String notify = "Book: "+book_name+" has been removed from your favourites";

                notification.setMessage(notify);
                notification.setDate(dateTimeFormatter2.format(localDateTime));
                notification.setEmail(member_email);

                notificationService.AddNotification(notification);
                favouriteService.removeFromFavouriteByID(id);

            return  "redirect:/user/viewmyfavourites/"+member_email+"?removesuccess";

        }
        catch (Exception e)
        {
            return  "redirect:/user/viewmyfavourites/"+member_email+"?removefailure";
        }

    }




}
