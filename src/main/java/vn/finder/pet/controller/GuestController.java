package vn.finder.pet.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.finder.pet.config.VNPayService;
import vn.finder.pet.entity.*;
import vn.finder.pet.service.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class GuestController {
    private UsersService usersService;
    private FavoritesService favoritesService;
    private AnimalsService animalsService;
    private AdoptService adoptService;
    private SponsService sponsService;
    private VNPayService vnPayService;
    private SheltersService sheltersService;
    private OtpService otpService;
    private MailService mailService;
    private BreedService breedService;
    private TwoFactorAuthPasswordsService twoFactorAuthPasswordsService;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Autowired
    public GuestController(UsersService userService, FavoritesService favoritesService, AnimalsService animalsService, AdoptService adoptService, SponsService sponsService, VNPayService vnPayService, SheltersService sheltersService, OtpService otpService, MailService mailService, TwoFactorAuthPasswordsService twoFactorAuthPasswordsService, BreedService breedService) {
        this.usersService = userService;
        this.favoritesService = favoritesService;
        this.animalsService = animalsService;
        this.adoptService = adoptService;
        this.sponsService = sponsService;
        this.vnPayService = vnPayService;
        this.sheltersService = sheltersService;
        this.otpService = otpService;
        this.mailService = mailService;
        this.twoFactorAuthPasswordsService = twoFactorAuthPasswordsService;
        this.breedService = breedService;
    }

    public String getEmailLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = "";
        System.out.println(authentication.getName());
        if (authentication != null && !authentication.getName().equals("anonymousUser")) {
            if(this.usersService.findById(authentication.getName()).isEmpty()){
                OAuth2User principal = (OAuth2User) authentication.getPrincipal();
                email = principal.getAttribute("email");
                System.out.println("email: "+email);
            } else {
                System.out.println("email: "+email);
                email = authentication.getName();
            }
        } else {
            return null;
        }
        return email;
    }

    @PostMapping("/saveUserLoginWithOAuth2")
    public String saveUserLoginWithOAuth2(@ModelAttribute(value = "country") String country, HttpSession session){
        Users userInfo = (Users) session.getAttribute("userLogin");
        userInfo.setCountry(country);
        this.usersService.createdUser(userInfo);
        session.removeAttribute("userLogin");
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model, @RequestParam(value = "message", required = false) String message){
        session.removeAttribute("emailUs");
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("listFavorite", this.favoritesService.findAll(0,4).stream().toList());
        model.addAttribute("listBreed", this.breedService.findAllBreedType());
        model.addAttribute("breedService", this.breedService);
        model.addAttribute("animalsService", this.animalsService);
        model.addAttribute("sheltersService", this.sheltersService);
        if(this.getEmailLogin() == null){
            model.addAttribute("listExplore", this.animalsService.findRandom(0, 12).stream().toList());
        } else {
            Users users = this.usersService.findById(this.getEmailLogin()).get();
            model.addAttribute("listExplore", this.animalsService.findByShelterAddressOrderByCustom(0, 12, users.getCountry()).stream().toList());
        }
        if(message != null){
            model.addAttribute("status", message);
        }
        return "/index";
    }

    @GetMapping("/sign-in")
    public String signIn(HttpSession session, Model model, @RequestParam(value = "messageComplete", required = false) String messageComplete){
        session.removeAttribute("emailUs");
        Users userInfo = (Users) session.getAttribute("userLogin");
        if(userInfo != null){
            model.addAttribute("modal", true);
        }
        model.addAttribute("messageComplete", messageComplete);
        return "/sign-in";
    }

    @GetMapping("/sign-up")
    public String signUp(Model model, HttpSession session,
                         @RequestParam(value = "errorMessageFirstName", required = false) String errorMessageFirstName,
                         @RequestParam(value = "errorMessageLastName", required = false) String errorMessageLastName,
                         @RequestParam(value = "errorMessageUserName", required = false) String errorMessageUserName,
                         @RequestParam(value = "errorMessagePassword", required = false) String errorMessagePassword,
                         @RequestParam(value = "errorMessageConfirmPassword", required = false) String errorMessageConfirmPassword,
                         @RequestParam(value = "errorMessageCountry", required = false) String errorMessageCountry){
        session.removeAttribute("emailUs");
        Users users = new Users();
        model.addAttribute("users", users);
        model.addAttribute("errorMessageFirstName", errorMessageFirstName);
        model.addAttribute("errorMessageLastName", errorMessageLastName);
        model.addAttribute("errorMessageUserName", errorMessageUserName);
        model.addAttribute("errorMessagePassword", errorMessagePassword);
        model.addAttribute("errorMessageConfirmPassword", errorMessageConfirmPassword);
        model.addAttribute("errorMessageCountry", errorMessageCountry);
        return "/sign-up";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        model.addAttribute("errorMessage", errorMessage);
        return "/forgot-password";
    }

    @GetMapping("/two-factor-auth")
    public String getMethodName(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage, @RequestParam(value = "reset", required = false) String reset, @RequestParam(value = "pagePrev", required = false) String pagePrev, @RequestParam(value = "email", required = false) String email) {
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("reset", reset);
        model.addAttribute("email", email);
        return "/two-factor-auth";
    }

    @GetMapping("/two-factor-auth-password")
    public String twoFatorAuthPass(Model model, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "errorMessagePasswordCode", required = false) String errorMessagePasswordCode, @RequestParam(value = "errorMessagePassword", required = false) String errorMessagePassword, @RequestParam(value = "errorMessageConfirmPassword", required = false) String errorMessageConfirmPassword) {
        model.addAttribute("email", email);
        model.addAttribute("errorMessagePasswordCode", errorMessagePasswordCode);
        model.addAttribute("errorMessagePassword", errorMessagePassword);
        model.addAttribute("errorMessageConfirmPassword", errorMessageConfirmPassword);
        return "/two-factor-auth-password";
    }

    @PostMapping("/check-sign-up")
    public String signUp(@ModelAttribute("users") Users users, @RequestParam(value = "cfPassword") String cfPassword, RedirectAttributes redirectAtb, HttpSession session){
        int valid = 0;
        if (users.getFirstName().isEmpty()) {
            redirectAtb.addAttribute("errorMessageFirstName", "Thiếu thông tin");
            valid++;
        }

        if (users.getLastName().isEmpty()) {
            redirectAtb.addAttribute("errorMessageLastName", "Thiếu thông tin");
            valid++;
        }

        if (users.getUserName().isEmpty()) {
            redirectAtb.addAttribute("errorMessageUserName", "Thiếu thông tin");
            valid++;
        } else {
            if (!usersService.isValidEmailAddress(users.getUserName())) {
                redirectAtb.addAttribute("errorMessageUserName", "Email không hợp lệ");
                valid++;
            } else {
                if (usersService.isUserExists(users.getUserName())) {
                    redirectAtb.addAttribute("errorMessageUserName", "Username đã tồn tại");
                    valid++;
                }
            }
        }

        if (users.getPassword().isEmpty()) {
            redirectAtb.addAttribute("errorMessagePassword", "Thiếu thông tin");
            valid++;
        } else {
            if (!usersService.isValidPassword(users.getPassword())) {
                redirectAtb.addAttribute("errorMessagePassword", "Mật khẩu không hợp lệ");
                valid++;
            }
        }

        if (cfPassword.isEmpty()) {
            redirectAtb.addAttribute("errorMessageConfirmPassword", "Thiếu thông tin");
            valid++;
        } else {
            if (!users.getPassword().equals(cfPassword)) {
                redirectAtb.addAttribute("errorMessageConfirmPassword", "Mật khẩu không khớp");
                valid++;
            }
        }

        if (users.getCountry().isEmpty()) {
            redirectAtb.addAttribute("errorMessageCountry", "Thiếu thông tin");
            if (users.getCountry().equals("Select Country")) {
                redirectAtb.addAttribute("errorMessageCountry", "Thiếu thông tin");
            }
            valid++;
        }

        if(valid == 0){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String enCode = bCryptPasswordEncoder.encode(users.getPassword());
            users.setPassword("{bcrypt}"+enCode);
            users.setEnabled(true);
            users.setCreatedDate(Date.valueOf(LocalDate.now()));
            users.setAvatar("defaulAvatar.jpg");
            mailService.MaxacNhan(users.getUserName());

            session.setAttribute("username",users.getUserName());
            session.setAttribute("userByName",users);

            return "redirect:/two-factor-auth";
        }
        return "redirect:/sign-up";
    }

    @PostMapping("/checkMail")
    public String checkMail(@ModelAttribute(value = "emailUs") String emailus, HttpSession session, RedirectAttributes ra)  throws ExecutionException {
        if (emailus.trim().isEmpty()) {
            ra.addAttribute("errorMessage","Email không được bỏ trống");
        } else {
            Matcher matcher = pattern.matcher(emailus);
            if(matcher.matches()){
                if(usersService.findById(emailus).isPresent()) {
                    mailService.MaxacNhan(emailus);
                    session.setAttribute("emailUs", emailus);
                    ra.addAttribute("email", emailus);
                    return "redirect:/two-factor-auth";
                } else {
                    ra.addAttribute("errorMessage","Email chưa được đăng ký vào hệ thống");
                }
            } else {
                ra.addAttribute("errorMessage", "Email không đúng định dạng");
            }
        }

        return "redirect:/forgot-password";
    }

    @PostMapping("/checkOtp")
    public String checkOtp(@ModelAttribute(value = "so1") String so1,
                           @ModelAttribute(value = "so2") String so2,
                           @ModelAttribute(value = "so3") String so3,
                           @ModelAttribute(value = "so4") String so4,
                           @ModelAttribute(value = "so5") String so5,
                           HttpSession session, RedirectAttributes ra) {
        String userFogotPw = (String)session.getAttribute("emailUs");
        String userSignUp = (String) session.getAttribute("username");
        Users users= (Users) session.getAttribute("userByName");
        String ma=so1+so2+so3+so4+so5;
        if(!ma.trim().isEmpty() && ma.length() == 5){
            if(userFogotPw!=null) {
                if ((otpService.GetOtp(userFogotPw).equals(ma))) {
                    //đúng mã
                    twoFactorAuthPasswordsService.sendOneTimePasswords(userFogotPw);
                    ra.addAttribute("email", session.getAttribute("emailUs"));
                    return "redirect:/two-factor-auth-password";
                } else {
                    ra.addAttribute("errorMessage", "Mã xác nhận không đúng");
                }
            }if(userSignUp!=null){
                if(otpService.GetOtp(userSignUp).equals(ma)) {
                    //đúng Mã
                    this.usersService.createdUser(users);
                    return "redirect:/index";
                }
            }


        } else {
            ra.addAttribute("errorMessage","Vui lòng không để trống field nào");
        }
        return "redirect:/two-factor-auth";
    }

    @GetMapping("/resetOtp")
    public String resetOtp(HttpSession session, RedirectAttributes ra) throws ExecutionException {
        String emailus = (String)session.getAttribute("emailUs");
        String userSignUp = (String) session.getAttribute("username");
        if(emailus!=null){
            mailService.MaxacNhan(emailus);
            ra.addAttribute("reset", true);
            ra.addAttribute("email", emailus);
        }else if(userSignUp!=null){
            mailService.MaxacNhan(userSignUp);
            ra.addAttribute("reset", true);
            ra.addAttribute("email", emailus);
        }
        return"redirect:/two-factor-auth";
    }

    @PostMapping("/checkPass")
    public String checkTwoFactorAuthPass(@RequestParam String passwordCode,
                                         @RequestParam String password,
                                         @RequestParam String confirmPassword,
                                         RedirectAttributes ra, HttpSession session) {
        int valid = 0;
        //Trống field input
        if (passwordCode.trim().isEmpty()) {
            ra.addAttribute("errorMessagePasswordCode", "Trống field");
        } else {
            //Kiểm tra password code
            if (!twoFactorAuthPasswordsService.validateOneTimePassword(passwordCode)) {
                ra.addAttribute("errorMessagePasswordCode", "Sai code");
            } else {
                valid++;
            }
        }

        if (password.trim().isEmpty()) {
            ra.addAttribute("errorMessagePassword", "Trống field");
        } else {
            // Kiểm tra định dạng và khác nhau với password cũ > Thực hiện đổi password
            if (twoFactorAuthPasswordsService.checkTwoFactorAuthPassword(password)) {
                valid++;
            } else {
                ra.addAttribute("errorMessagePassword", "Sai định dạng password");
            }
        }

        if (confirmPassword.trim().isEmpty()) {
            ra.addAttribute("errorMessageConfirmPassword", "Trống field");
        } else {
            //Kiểm tra password với password confirm
            if (!password.equals(confirmPassword)) {
                ra.addAttribute("errorMessageConfirmPassword", "Mật khẩu không trùng khớp với xác nhận mật khẩu");
            } else {
                valid++;
            }
        }

        if (valid == 3) {
            ra.addAttribute("messageComplete", "Đổi mật khẩu thành công");
            String emailus = (String)session.getAttribute("emailUs");
            this.twoFactorAuthPasswordsService.updatePassword(emailus, password.trim());
            return "redirect:/sign-in";
        }
        return "redirect:/two-factor-auth-password";
    }


    @GetMapping("/booking-confirm")
    public String getBookingConfirm(@RequestParam Long id, Model model){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        Animals animals = this.animalsService.findById(id);
        Users users = this.usersService.findById(this.getEmailLogin()).get();
        Adopt adopt = new Adopt(null, Date.valueOf(LocalDate.now()), "Awaiting", users, animals);
        if(this.adoptService.save(adopt)){
            model.addAttribute("adopt", adopt);
            model.addAttribute("adoptService", this.adoptService);
            model.addAttribute("breedService", this.breedService);
            model.addAttribute("animalsService", this.animalsService);
            adoptService.sendMailToShelters(id);
            return "/booking-confirm";
        }
        return "";
    }

    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              @RequestParam("usage") String usage,
                              @RequestParam("name") String idShelter,
                              HttpServletRequest request){
        Long id = 0L;
        if(usage.equals("center")){
            id = Long.valueOf(idShelter);
        }
        orderInfo += ("idShelterDonate: " + id);
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model){
        int paymentStatus =vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        model.addAttribute("transactionId", transactionId);

        if(paymentStatus == 1){
            model.addAttribute("status", "thành công");
            String message = orderInfo.trim().substring(0, orderInfo.indexOf("idShelterDonate: "));
            Long idShelter = Long.valueOf(orderInfo.trim().substring(orderInfo.indexOf("idShelterDonate: ")).replace("idShelterDonate: ", ""));
            Spons s = new Spons(null, Double.valueOf(totalPrice)/100, message, Date.valueOf(LocalDate.now()), this.usersService.findById(this.getEmailLogin()).get(), this.sheltersService.findById(idShelter));
            this.sponsService.save(s);
            model.addAttribute("spon", s);
            model.addAttribute("sponsService", this.sponsService);
        } else {
            model.addAttribute("status", "thất bại");
        }

        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        return "donate-confirm";
    }

    @GetMapping("/shop-token")
    public String shopToken(Model model){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        return "redirect:/coming-soon";
    }

    @GetMapping("/coming-soon")
    public String comingSoon(Model model){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        return "/coming-soon";
    }
    @GetMapping("/policy")
    public String policy(){
        return "/privacy-policy";
    }
    @GetMapping("/blog")
    public String blog(){
        return "/blog";
    }
    @GetMapping("/blogDetail")
    public String blogDetail(){
        return "/blog-detail";
    }
    @GetMapping("/about")
    public String about(){
        return "/about";
    }
}
