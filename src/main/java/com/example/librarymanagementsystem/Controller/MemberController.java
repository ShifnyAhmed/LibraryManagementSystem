package com.example.librarymanagementsystem.Controller;

import com.example.librarymanagementsystem.Model.Book;
import com.example.librarymanagementsystem.Model.Contact;
import com.example.librarymanagementsystem.Model.Notification;
import com.example.librarymanagementsystem.Model.User;
import com.example.librarymanagementsystem.Repository.BookRepository;
import com.example.librarymanagementsystem.Repository.UserRepository;
import com.example.librarymanagementsystem.Service.BookService;
import com.example.librarymanagementsystem.Service.ContactService;
import com.example.librarymanagementsystem.Service.NotificationService;
import com.example.librarymanagementsystem.Service.UserService;
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

        model.addAttribute("id",view_book_details.get().getId());
        model.addAttribute("name",view_book_details.get().getBookname());
        model.addAttribute("author",view_book_details.get().getAuthor());
        model.addAttribute("category",view_book_details.get().getCategory());
        model.addAttribute("filename",view_book_details.get().getFileName());
        model.addAttribute("filepath",view_book_details.get().getFilePath());
        model.addAttribute("image",view_book_details.get().getImage());
        model.addAttribute("pdf_filepath",view_book_details.get().getPdfPath());
        model.addAttribute("pdf_filename",view_book_details.get().getPdfName());
        model.addAttribute("pdf",view_book_details.get().getPdfPath());

        model.addAttribute("view_book_details",view_book_details);


        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to EditDrugSupplier html page
        return "ViewBookDetailsMember";
    }

}
