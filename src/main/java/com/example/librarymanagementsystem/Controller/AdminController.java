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

    @Autowired
    ReturnedService returnedService;

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

//    -------------------------------------------------------------------------------------------------

    @GetMapping(value = "/admin/approvereservationpage/{id}")
    public String ApproveReservationPage(@PathVariable("id") Long id,Model model)
    {
        Optional<Reservation> reservation_details = reservationService.getReservationByID(id);

        model.addAttribute("reservation_id",reservation_details.get().getId());
        model.addAttribute("book_id",reservation_details.get().getBook_id());
        model.addAttribute("member_email",reservation_details.get().getEmail());
        model.addAttribute("bookname",reservation_details.get().getBook_name());
        model.addAttribute("reserved_date",reservation_details.get().getReserved_date());
        model.addAttribute("lending_duration",reservation_details.get().getLending_duration());
        model.addAttribute("lending_charges",reservation_details.get().getLending_charges());
        model.addAttribute("allowed_return_date",reservation_details.get().getAllowed_return_date());
        model.addAttribute("overdue_charges",reservation_details.get().getOverdue_charges());


        return "ApproveReservationAdmin";
    }

//    -------------------------------------------------------------------------------------------------

    //Updates status of reservation as "Approved"
    @PostMapping(value = "/admin/approvereservation")
    public String ApproveReservation(@Valid Reservation reservation, @Valid Notification notification,
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
            reservation.setId(reservation_id);
            reservation.setBook_id(book_id);
            reservation.setBook_name(book_name);
            reservation.setEmail(member_email);
            reservation.setLending_duration(Integer.parseInt(lending_duration));
            reservation.setLending_charges(Integer.parseInt(lending_charges));
            reservation.setOverdue_charges(Integer.parseInt(overdue_charges));
            reservation.setReserved_date(reserved_date);
            reservation.setAllowed_return_date(allowed_return_date);
            reservation.setStatus("Approved");

            reservationService.saveReservation(reservation);

            String message = "Your Reservation on Book: "+book_name+" has been approved, collect your book at Showcase Bookstore on " +reserved_date+"";

            //notifying user on approval

            notification.setDate(dateTimeFormatter.format(localDateTime));
            notification.setEmail(member_email);
            notification.setMessage(message);
            notificationService.AddNotification(notification);

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails=(UserDetails)authentication.getPrincipal();
            model.addAttribute("useremail",userDetails);

            return  "redirect:/admin/viewpendingreservations/Pending?approvedsuccess";

        } catch (Exception e) {

            e.printStackTrace();
            return  "redirect:/admin/viewpendingreservations/Pending?approvedunsuccess";
        }

    }

//    -------------------------------------------------------------------------------------------------


    @GetMapping(value = "/admin/rejectreservationpage/{id}")
    public String RejectReservationPage(@PathVariable("id") Long id,Model model)
    {
        Optional<Reservation> reject_reservation_details = reservationService.getReservationByID(id);

        model.addAttribute("reservation_id",reject_reservation_details.get().getId());
        model.addAttribute("book_id",reject_reservation_details.get().getBook_id());
        model.addAttribute("member_email",reject_reservation_details.get().getEmail());
        model.addAttribute("bookname",reject_reservation_details.get().getBook_name());
        model.addAttribute("reserved_date",reject_reservation_details.get().getReserved_date());
        model.addAttribute("lending_duration",reject_reservation_details.get().getLending_duration());
        model.addAttribute("lending_charges",reject_reservation_details.get().getLending_charges());
        model.addAttribute("allowed_return_date",reject_reservation_details.get().getAllowed_return_date());
        model.addAttribute("overdue_charges",reject_reservation_details.get().getOverdue_charges());

        return "RejectReservationAdmin";
    }

//    -------------------------------------------------------------------------------------------------

    //Rejects the Reservation-deletes the reservaion from list and notifies user
    @PostMapping(value = "/admin/rejectreservation")
    public String DeleteReservation(@Valid Notification notification,
                                    @RequestParam("reservation_id") Long reservation_id,
                                    @RequestParam("member_email") String member_email,
                                    @RequestParam("book_name") String book_name,
                                    @RequestParam("reserved_date") String reserved_date
            , Model model) {


        try{

            reservationService.deleteReservationById(reservation_id);

            String message = "Your Reservation on Book: "+book_name+" for Date: "+reserved_date+" has been rejected by the admin";

            //notifying user on approval

            notification.setDate(dateTimeFormatter.format(localDateTime));
            notification.setEmail(member_email);
            notification.setMessage(message);
            notificationService.AddNotification(notification);

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails=(UserDetails)authentication.getPrincipal();
            model.addAttribute("useremail",userDetails);

            return  "redirect:/admin/viewpendingreservations/Pending?rejectedsuccess";

        } catch (Exception e)
        {
            e.printStackTrace();
            return  "redirect:/admin/viewpendingreservations/Pending?rejectedunsuccess";

        }

    }

//    -------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/admin/viewreturnedbooks/{status}")
    public String ViewReservationMarkedAsReturnedByMember(@PathVariable("status") String status, Model model)
    {
        List<Returned> returned_reservations_list = returnedService.getReturnedReservationByStatus(status);

        model.addAttribute("returned_reservations_list",returned_reservations_list);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        return "ReturnedReservationsAdmin";
    }

 //    -------------------------------------------------------------------------------------------------

    @GetMapping(value = "/admin/confirmreturnedpage/{id}")
    public String ConfirmReturnedPage(@PathVariable("id") Long id,Model model)
    {
        Optional<Returned> returned_reservation_details = returnedService.getReturnedReservationByID(id);

        model.addAttribute("returned_reservation_id",returned_reservation_details.get().getId());
        model.addAttribute("book_id",returned_reservation_details.get().getBook_id());
        model.addAttribute("member_email",returned_reservation_details.get().getEmail());
        model.addAttribute("bookname",returned_reservation_details.get().getBook_name());
        model.addAttribute("reserved_date",returned_reservation_details.get().getReserved_date());
        model.addAttribute("lending_duration",returned_reservation_details.get().getLending_duration());
        model.addAttribute("lending_charges",returned_reservation_details.get().getLending_charges());
        model.addAttribute("allowed_return_date",returned_reservation_details.get().getAllowed_return_date());
        model.addAttribute("overdue_charges",returned_reservation_details.get().getOverdue_charges());
        model.addAttribute("actual_return_date",returned_reservation_details.get().getAllowed_return_date());
        model.addAttribute("total",returned_reservation_details.get().getTotal());

        return "ConfirmReturnedAdmin";
    }

    //    -------------------------------------------------------------------------------------------------

    //Updates status of returned reservation as "Returned"
    @PostMapping(value = "/admin/confirmreturned")
    public String ConfirmReturnedReservation(@Valid Returned returned, @Valid Notification notification,
                                     @RequestParam("returned_reservation_id")Long returned_reservation_id,
                                     @RequestParam("book_id")Long book_id,
                                     @RequestParam("member_email")String member_email,
                                     @RequestParam("book_name") String book_name,
                                     @RequestParam("lending_duration") String lending_duration,
                                     @RequestParam("lending_charges")String lending_charges,
                                     @RequestParam("overdue_charges")String overdue_charges,
                                     @RequestParam("reserved_date")String reserved_date,
                                     @RequestParam("allowed_return_date")String allowed_return_date,
                                     @RequestParam("actual_return_date")String actual_return_date,
                                     @RequestParam("total")String total,
                                     Model model) {
        try {
            returned.setId(returned_reservation_id);
            returned.setBook_id(book_id);
            returned.setBook_name(book_name);
            returned.setEmail(member_email);
            returned.setLending_duration(Integer.parseInt(lending_duration));
            returned.setLending_charges(Integer.parseInt(lending_charges));
            returned.setOverdue_charges(Integer.parseInt(overdue_charges));
            returned.setReserved_date(reserved_date);
            returned.setAllowed_return_date(allowed_return_date);
            returned.setActual_return_date(actual_return_date);
            returned.setTotal(Integer.parseInt(total));

            returned.setStatus("Returned");

            returnedService.saveReturnedReservation(returned);



            String message = "The Book: "+book_name+" has been confirmed as returned on "+actual_return_date+", see more details of your returned books in Reservation History Page";

            //notifying user on admin returned confirmation

            notification.setDate(dateTimeFormatter.format(localDateTime));
            notification.setEmail(member_email);
            notification.setMessage(message);
            notificationService.AddNotification(notification);

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails=(UserDetails)authentication.getPrincipal();
            model.addAttribute("useremail",userDetails);

            return  "redirect:/admin/viewreturnedbooks/Checking%20Returned?returnedsuccess";

        } catch (Exception e) {

            e.printStackTrace();
            return  "redirect:/admin/viewreturnedbooks/Checking%20Returned?returnedfailure";
        }

    }


    //    -------------------------------------------------------------------------------------------------

    @GetMapping(value = "/admin/notreturnedpage/{id}")
    public String NotReturnedPage(@PathVariable("id") Long id,Model model)
    {
        Optional<Returned> returned_reservation_details = returnedService.getReturnedReservationByID(id);

        model.addAttribute("returned_reservation_id",returned_reservation_details.get().getId());
        model.addAttribute("book_id",returned_reservation_details.get().getBook_id());
        model.addAttribute("member_email",returned_reservation_details.get().getEmail());
        model.addAttribute("bookname",returned_reservation_details.get().getBook_name());
        model.addAttribute("reserved_date",returned_reservation_details.get().getReserved_date());
        model.addAttribute("lending_duration",returned_reservation_details.get().getLending_duration());
        model.addAttribute("lending_charges",returned_reservation_details.get().getLending_charges());
        model.addAttribute("allowed_return_date",returned_reservation_details.get().getAllowed_return_date());
        model.addAttribute("overdue_charges",returned_reservation_details.get().getOverdue_charges());
        model.addAttribute("actual_return_date",returned_reservation_details.get().getActual_return_date());
        model.addAttribute("total",returned_reservation_details.get().getOverdue_charges());

        return "NotReturnedAdmin";
    }

    //    -------------------------------------------------------------------------------------------------


    //This functions saves the details of the selected row of returned reservation table to reservation table with status as "Checking Returned"
    //And deletes the selected row from returned reservation table
    @PostMapping(value = "/admin/notreturned")
    public String NotReturnedReservation(@Valid Reservation reservation, @Valid Notification notification,
                                             @RequestParam("returned_reservation_id")Long returned_reservation_id,
                                             @RequestParam("book_id")Long book_id,
                                             @RequestParam("member_email")String member_email,
                                             @RequestParam("book_name") String book_name,
                                             @RequestParam("lending_duration") String lending_duration,
                                             @RequestParam("lending_charges")String lending_charges,
                                             @RequestParam("overdue_charges")String overdue_charges,
                                             @RequestParam("reserved_date")String reserved_date,
                                             @RequestParam("allowed_return_date")String allowed_return_date,
                                             Model model) {
        try {

            reservation.setBook_id(book_id);
            reservation.setBook_name(book_name);
            reservation.setEmail(member_email);
            reservation.setLending_duration(Integer.parseInt(lending_duration));
            reservation.setLending_charges(Integer.parseInt(lending_charges));
            reservation.setOverdue_charges(Integer.parseInt(overdue_charges));
            reservation.setReserved_date(reserved_date);
            reservation.setAllowed_return_date(allowed_return_date);

            reservation.setTotal(0);
            reservation.setActual_return_date(null);

            reservation.setStatus("Approved");

            reservationService.saveReservation(reservation);

            returnedService.deleteReturnedReservationById(returned_reservation_id);



            String message = "The Book: "+book_name+" you marked as returned is found out to be be not returned yet and was set back to previous state, continuing to make such kind of fake returned confirmation would lead to your account being Blacklisted.";

            //notifying user on admin returned confirmation

            notification.setDate(dateTimeFormatter.format(localDateTime));
            notification.setEmail(member_email);
            notification.setMessage(message);
            notificationService.AddNotification(notification);

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails=(UserDetails)authentication.getPrincipal();
            model.addAttribute("useremail",userDetails);

            return  "redirect:/admin/viewreturnedbooks/Checking%20Returned?notreturnedsuccess";

        } catch (Exception e) {

            e.printStackTrace();
            return  "redirect:/admin/viewreturnedbooks/Checking%20Returned?notreturnedfailure";
        }

    }

    //    -------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/admin/viewreservationhistory/{status}")
    public String ViewReservationHistory(@PathVariable("status") String status, Model model)
    {
        List<Returned> reservation_history = returnedService.getReturnedReservationByStatus(status);

        model.addAttribute("reservation_history",reservation_history);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        model.addAttribute("useremail",userDetails);

        return "ReservationHistoryAdmin";
    }
}
