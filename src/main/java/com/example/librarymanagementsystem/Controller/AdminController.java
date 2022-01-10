package com.example.librarymanagementsystem.Controller;

import com.example.librarymanagementsystem.Model.*;
import com.example.librarymanagementsystem.Repository.BookRepository;
import com.example.librarymanagementsystem.Repository.ContactRepository;
import com.example.librarymanagementsystem.Repository.UserRepository;
import com.example.librarymanagementsystem.Service.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime localDateTime = LocalDateTime.now();

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ContactService contactService;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ReservationService reservationService;

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
    public String addToBlacklist(@Valid Notification notification,
                                 @RequestParam("user_id")Long id,
                                 @RequestParam("blacklist") String blacklist,
                                 @RequestParam("email") String email
                                 ,Model model)
    {
        String message="You have been blacklisted by the admin,From now on you will be unable to reserve books online, contact admin to resolve the problem.";
            try {

                userService.update(blacklist,id);

                //Adding notification to user
                //
                notification.setMessage(message);

                //setting date
                notification.setDate(dateTimeFormatter.format(localDateTime));

                //setting blacklisted user email
                notification.setEmail(email);

                //saving notification
                notificationService.AddNotification(notification);

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
    public String promoteMember(@Valid Notification notification,
                                 @RequestParam("user_id")Long id,
                                 @RequestParam("level") String level,
                                @RequestParam("email") String email
            ,Model model)
    {
        String message="You have been promoted/demoted ! Check your member level in profile.";
        try {

            userService.promoteMember(level,id);

            //Adding notification to user

            notification.setDate(dateTimeFormatter.format(localDateTime));
            notification.setEmail(email);
            notification.setMessage(message);
            notificationService.AddNotification(notification);

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
    public String RemoveFromBlacklist(@Valid Notification notification,
                                @RequestParam("user_id")Long id,
                                 @RequestParam("blacklist") String blacklist,
                                      @RequestParam("email") String email
            ,Model model)
    {
        String message="Congrats! You have been removed from blacklist, start reserving books online and have fun!";
        try {

            userService.update(blacklist,id);


            //Adding notification to user

            notification.setDate(dateTimeFormatter.format(localDateTime));
            notification.setEmail(email);
            notification.setMessage(message);
            notificationService.AddNotification(notification);

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails=(UserDetails)authentication.getPrincipal();
            model.addAttribute("useremail",userDetails);
        }
        catch (Exception e){

            return "redirect:/admin/viewallmembers?unsuccess";
        }

        return "redirect:/admin/viewallmembers?removeblacklistsuccess";
    }

//    -------------------------------------------------------------------------------------------------

    //Displays the add book page
    @GetMapping(value = "/admin/addbookpage")
    public String addBookPage(Model model)
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        return "AddBookAdmin";
    }

//    -------------------------------------------------------------------------------------------------

    @PostMapping(value = "/admin/addbook")
    public String addBook(@Valid Book book, @RequestParam("name")final String book_name,
                          @RequestParam("author")final String book_author,
                          @RequestParam("file")final MultipartFile file,
                          @RequestParam("category") final String book_category,
                          @RequestParam("pdf_file")final MultipartFile pdf_file) {


        String Upload_Directory =System.getProperty("user.dir")+"/src/main/resources/static/uploads/";
        try {

            if(book == null) {

                return "redirect:/admin/addbookpage?unsuccess";

            }

            //image
            String file_name = file.getOriginalFilename();
            String file_path = Paths.get(Upload_Directory,file_name).toString();

            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(file_path)));
            stream.write(file.getBytes());
            stream.close();

            byte[] image_data=file.getBytes();
            String base64_Encoded_Image = Base64.encodeBase64String(image_data);


            //pdf
            String pdf_file_name = pdf_file.getOriginalFilename();
            String pdf_file_path = Paths.get(Upload_Directory,pdf_file_name).toString();

            BufferedOutputStream stream2 = new BufferedOutputStream(new FileOutputStream(new File(pdf_file_path)));
            stream2.write(pdf_file.getBytes());
            stream2.close();

            byte[] pdf_data=pdf_file.getBytes();
            String base64_Encoded_PDF = Base64.encodeBase64String(pdf_data);


            book.setBookname(book_name);
            book.setAuthor(book_author);
            book.setImage(base64_Encoded_Image.getBytes(StandardCharsets.UTF_8));
            book.setFileName(file_name);
            book.setFilePath(file_path);
            book.setCategory(book_category);
            book.setPdfName(pdf_file_name);
            book.setPdfPath(pdf_file_path);
            book.setPdf(base64_Encoded_PDF.getBytes(StandardCharsets.UTF_8));

            boolean status = bookService.AddBook(book);

            //displays success msg if status = true (saved the book)
            if(status)
            {
                return "redirect:/admin/addbookpage?success";
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return "redirect:/admin/addbookpage?unsuccess";
    }

//    -------------------------------------------------------------------------------------------------

    @GetMapping(value = "/admin/viewallbooks")
    public String viewAllBooks(Model model)
    {

        List<Book> All_books = bookService.getAllBooks();

        model.addAttribute("All_books",All_books);
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        return "ViewAllBooksAdmin";
    }

    //    ------------------------------------------------------------------------------------------------

    //display edit book page
    @GetMapping(value = "/admin/editbookpage/{id}")
    public String EditBookButton(@PathVariable("id") Long id, Model model)
    {
        Optional<Book> view_edit_book = bookRepository.findById(id);

        model.addAttribute("id",view_edit_book.get().getId());
        model.addAttribute("name",view_edit_book.get().getBookname());
        model.addAttribute("author",view_edit_book.get().getAuthor());
        model.addAttribute("category",view_edit_book.get().getCategory());
        model.addAttribute("filename",view_edit_book.get().getFileName());
        model.addAttribute("filepath",view_edit_book.get().getFilePath());
        model.addAttribute("image",view_edit_book.get().getImage());
        model.addAttribute("pdf_filepath",view_edit_book.get().getPdfPath());
        model.addAttribute("pdf_filename",view_edit_book.get().getPdfName());
        model.addAttribute("pdf",view_edit_book.get().getPdfPath());

        //redirecting to EditDrugSupplier html page
        return "EditBookAdmin";
    }

//    -------------------------------------------------------------------------------------------------

    @PostMapping(value = "/admin/editbook")
    public String EditBook(@Valid Book book, @RequestParam("name")final String book_name,
                          @RequestParam("author")final String book_author,
                          @RequestParam("file")final MultipartFile file,
                           @RequestParam("pdf_file")final MultipartFile pdf_file,
                           @RequestParam("filename")final String book_filename,
                           @RequestParam("filepath")final String book_filepath,
                           @RequestParam("pdf_filename")final String book_pdf_filename,
                           @RequestParam("pdf_filepath")final String book_pdf_filepath,
                          @RequestParam("category") final String book_category,
                            @RequestParam("image") final byte[] image,
                            @RequestParam("pdf") final byte[] pdf) {


        String Upload_Directory =System.getProperty("user.dir")+"/src/main/resources/static/uploads/";
        try {

            if(book == null) {

                return "redirect:/admin/viewallbooks?unsuccess";

            }

            if(file.isEmpty() && pdf_file.isEmpty())
            {
                //if user has not selected both pdf and image in the edit form

                book.setBookname(book_name);
                book.setAuthor(book_author);
                book.setImage(image);
                book.setFileName(book_filename);
                book.setFilePath(book_filepath);
                book.setCategory(book_category);
                book.setPdf(pdf);
                book.setPdfPath(book_pdf_filepath);
                book.setPdfName(book_pdf_filename);

                boolean status = bookService.AddBook(book);

                //displays success msg if status = true (saved the book)
                if(status)
                {
                    return "redirect:/admin/viewallbooks?editsuccess";
                }

            }
            else if (pdf_file.isEmpty())
            {
                //if user has not selected pdf but has selected image in the edit form

                book.setBookname(book_name);
                book.setAuthor(book_author);
                book.setPdf(pdf);
                book.setPdfPath(book_pdf_filepath);
                book.setPdfName(book_pdf_filename);
                book.setCategory(book_category);


                //image
                String file_name=file.getOriginalFilename();
                String file_path= Paths.get(Upload_Directory,file_name).toString();

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(file_path)));
                stream.write(file.getBytes());
                stream.close();

                byte[] image_data=file.getBytes();
                String base64_Encoded_Image = Base64.encodeBase64String(image_data);

                book.setImage(base64_Encoded_Image.getBytes(StandardCharsets.UTF_8));
                book.setFileName(file_name);
                book.setFilePath(file_path);

                boolean status = bookService.AddBook(book);


                if(status)
                {
                    return "redirect:/admin/viewallbooks?editsuccess";
                }


            }
            else if (file.isEmpty())
            {
                //if user has not selected image but has selected pdf in the edit form

                book.setBookname(book_name);
                book.setAuthor(book_author);
                book.setImage(image);
                book.setFileName(book_filename);
                book.setFilePath(book_filepath);
                book.setCategory(book_category);


                //pdf
                String pdf_file_name = pdf_file.getOriginalFilename();
                String pdf_file_path = Paths.get(Upload_Directory,pdf_file_name).toString();

                BufferedOutputStream stream2 = new BufferedOutputStream(new FileOutputStream(new File(pdf_file_path)));
                stream2.write(pdf_file.getBytes());
                stream2.close();

                byte[] pdf_data=pdf_file.getBytes();
                String base64_Encoded_PDF = Base64.encodeBase64String(pdf_data);

                book.setPdfName(pdf_file_name);
                book.setPdfPath(pdf_file_path);
                book.setPdf(base64_Encoded_PDF.getBytes(StandardCharsets.UTF_8));

                boolean status = bookService.AddBook(book);


                if(status)
                {
                    return "redirect:/admin/viewallbooks?editsuccess";
                }

            }
            else
            {
                //if user has selected both pdf and image in the edit form

                //----image-----
                String file_name=file.getOriginalFilename();
                String file_path= Paths.get(Upload_Directory,file_name).toString();

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(file_path)));
                stream.write(file.getBytes());
                stream.close();

                byte[] image_data=file.getBytes();
                String base64_Encoded_Image = Base64.encodeBase64String(image_data);

                //----pdf----
                String pdf_file_name = pdf_file.getOriginalFilename();
                String pdf_file_path = Paths.get(Upload_Directory,pdf_file_name).toString();

                BufferedOutputStream stream2 = new BufferedOutputStream(new FileOutputStream(new File(pdf_file_path)));
                stream2.write(pdf_file.getBytes());
                stream2.close();

                byte[] pdf_data=pdf_file.getBytes();
                String base64_Encoded_PDF = Base64.encodeBase64String(pdf_data);


                book.setBookname(book_name);
                book.setAuthor(book_author);
                book.setImage(base64_Encoded_Image.getBytes(StandardCharsets.UTF_8));
                book.setFileName(file_name);
                book.setFilePath(file_path);
                book.setCategory(book_category);
                book.setPdfName(pdf_file_name);
                book.setPdfPath(pdf_file_path);
                book.setPdf(base64_Encoded_PDF.getBytes(StandardCharsets.UTF_8));

                boolean status = bookService.AddBook(book);


                if(status)
                {
                    return "redirect:/admin/viewallbooks?editsuccess";
                }

            }
//            end of else



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return "redirect:/admin/viewallbooks?editunsuccess";
    }

//    -------------------------------------------------------------------------------------------------

    //This displays a confirmation page to delete book
    @GetMapping(value = "/admin/deletebookpage/{id}")
    public String DeleteBookButton(@PathVariable("id") Long id, Model model)
    {
        Optional<Book> delete_book = bookRepository.findById(id);

        model.addAttribute("id",delete_book.get().getId());
        model.addAttribute("name",delete_book.get().getBookname());
        model.addAttribute("author",delete_book.get().getAuthor());
        model.addAttribute("category",delete_book.get().getCategory());
        model.addAttribute("filename",delete_book.get().getFileName());
        model.addAttribute("filepath",delete_book.get().getFilePath());
        model.addAttribute("image",delete_book.get().getImage());


        return "DeleteBookAdmin";
    }

//    -------------------------------------------------------------------------------------------------

    //this deletes the selected book from book table in database
    @GetMapping(value = "/admin/deletebook/{id}")
    public String deleteBook(@RequestParam("id") Long id)
    {
        try{
            bookService.deleteBook(id);
            return  "redirect:/admin/viewallbooks?deletesuccess";
        }
        catch (Exception e){
            return  "redirect:/supplier/viewallbooks?deletesuccess";
        }

    }

//---------------------------------------------------------------------------------------------------------

    //displays all the contact messages-admin
    @RequestMapping(value = "/admin/allmessages")
    public String viewAllMessage(Model model)
    {
        List<Contact> adminMessageList = contactRepository.findAll();

        model.addAttribute("adminMessageList",adminMessageList);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to ViewAllUsersAdmin html page
        return "ViewAllMessageAdmin";
    }

    //    -------------------------------------------------------------------------------------------------

    //this deletes the selected message
    @GetMapping(value = "/admin/deletemessage/{id}")
    public String deleteMessage(@Valid Notification notification,@PathVariable("id") Long id) throws IOException {

        String notify = "Your Message Has Been Read By The Admin And Resolved";

        contactService.deleteMessage(id);

            Optional<Contact> cntct = contactRepository.findById(id);

            if(cntct.isPresent())
            {
                Contact contact = cntct.get();

                String member_email = contact.getEmail();


                notification.setMessage(notify);
                notification.setDate(dateTimeFormatter.format(localDateTime));
                notification.setEmail(member_email);

                notificationService.AddNotification(notification);
            }


        return  "redirect:/admin/allmessages?deletemsgsuccess";
    }

    //    -------------------------------------------------------------------------------------------------

    //Displays all the pending reservations of the logged in member
    @RequestMapping(value = "/admin/viewpendingreservations/{status}")
    public String PendingReservationlist(@PathVariable("status") String status, Model model)
    {
        List<Reservation> pending_reservations = reservationService.getReservationByStatus(status);

        model.addAttribute("pending_reservations",pending_reservations);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        //redirecting to PendingReservationsMember html page
        return "PendingReservationsAdmin";
    }

}
