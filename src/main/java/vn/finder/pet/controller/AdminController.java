package vn.finder.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.finder.pet.entity.Adopt;
import vn.finder.pet.entity.Authorities;
import vn.finder.pet.entity.Shelters;
import vn.finder.pet.entity.Users;
import vn.finder.pet.service.*;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private SheltersService sheltersService;
    private UsersService usersService;
    private AnimalsService animalsService;
    private AdoptService adoptService;
    private SponsService sponsService;
    private BreedService breedService;
    private AuthoritiesService authoritiesService;

    @Autowired
    public AdminController(AuthoritiesService authoritiesService,SheltersService sheltersService, UsersService usersService, AnimalsService animalsService, AdoptService adoptService, SponsService sponsService, BreedService breedService) {
        this.sheltersService = sheltersService;
        this.usersService = usersService;
        this.animalsService = animalsService;
        this.adoptService = adoptService;
        this.sponsService = sponsService;
        this.breedService = breedService;
        this.authoritiesService= authoritiesService;
    }

    public String getEmailLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = "";
        if (authentication != null && !authentication.getName().equals("anonymousUser")) {
            if(this.usersService.findById(authentication.getName()).isEmpty()){
                OAuth2User principal = (OAuth2User) authentication.getPrincipal();
                email = principal.getAttribute("email");
            } else {
                email = authentication.getName();
            }
        } else {
            return null;
        }
        return email;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model){
        model.addAttribute("totalPending", this.sheltersService.findByShelterStatus("Awaiting").size());
        model.addAttribute("listTotalPending", this.sheltersService.findByShelterStatus("Awaiting"));

        model.addAttribute("totalShelters", this.sheltersService.findSheltersByStatusContaining("Opening", 0, Integer.MAX_VALUE).getTotalElements());
        model.addAttribute("listTotalShelters", this.sheltersService.findSheltersByStatusContaining("Opening", 0, 6).stream());

        model.addAttribute("totalUsers", this.usersService.findAll(0, Integer.MAX_VALUE).getTotalElements());
        model.addAttribute("listTotalUsers", this.usersService.findAll(0, 7).stream());

        model.addAttribute("listSpon", this.sponsService.findAllToPage(0, 6));

        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "dashboard");

        ArrayList<Double> listIncome = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
        String currentYear = currentDate.format(yearFormatter);

        listIncome.add(this.sponsService.findCountGiftInMonth(1, Integer.valueOf(currentYear)) != null ? this.sponsService.findCountGiftInMonth(1, Integer.valueOf(currentYear)) : 0);
        listIncome.add(this.sponsService.findCountGiftInMonth(2, Integer.valueOf(currentYear)) != null ? this.sponsService.findCountGiftInMonth(2, Integer.valueOf(currentYear)) : 0);
        listIncome.add(this.sponsService.findCountGiftInMonth(3, Integer.valueOf(currentYear)) != null ? this.sponsService.findCountGiftInMonth(3, Integer.valueOf(currentYear)) : 0);
        listIncome.add(this.sponsService.findCountGiftInMonth(4, Integer.valueOf(currentYear)) != null ? this.sponsService.findCountGiftInMonth(4, Integer.valueOf(currentYear)) : 0);
        listIncome.add(this.sponsService.findCountGiftInMonth(5, Integer.valueOf(currentYear)) != null ? this.sponsService.findCountGiftInMonth(5, Integer.valueOf(currentYear)) : 0);
        listIncome.add(this.sponsService.findCountGiftInMonth(6, Integer.valueOf(currentYear)) != null ? this.sponsService.findCountGiftInMonth(6, Integer.valueOf(currentYear)) : 0);
        listIncome.add(this.sponsService.findCountGiftInMonth(7, Integer.valueOf(currentYear)) != null ? this.sponsService.findCountGiftInMonth(7, Integer.valueOf(currentYear)) : 0);
        listIncome.add(this.sponsService.findCountGiftInMonth(8, Integer.valueOf(currentYear)) != null ? this.sponsService.findCountGiftInMonth(8, Integer.valueOf(currentYear)) : 0);
        listIncome.add(this.sponsService.findCountGiftInMonth(9, Integer.valueOf(currentYear)) != null ? this.sponsService.findCountGiftInMonth(9, Integer.valueOf(currentYear)) : 0);
        listIncome.add(this.sponsService.findCountGiftInMonth(10, Integer.valueOf(currentYear)) != null ? this.sponsService.findCountGiftInMonth(10, Integer.valueOf(currentYear)) : 0);
        listIncome.add(this.sponsService.findCountGiftInMonth(11, Integer.valueOf(currentYear)) != null ? this.sponsService.findCountGiftInMonth(11, Integer.valueOf(currentYear)) : 0);
        listIncome.add(this.sponsService.findCountGiftInMonth(12, Integer.valueOf(currentYear)) != null ? this.sponsService.findCountGiftInMonth(12, Integer.valueOf(currentYear)) : 0);

        model.addAttribute("listIncome", listIncome);

        ArrayList<Long> listRegist = new ArrayList<>();
        listRegist.add(this.sheltersService.findSheltersByStatusContaining("Opening", 0, Integer.MAX_VALUE).getTotalElements());
        listRegist.add(this.sheltersService.findSheltersByStatusContaining("Canceled", 0, Integer.MAX_VALUE).getTotalElements());
        model.addAttribute("countDonate", this.sponsService.findCountUserDonateByWebsite(0L));

        model.addAttribute("listRegist", listRegist);
        return "/admin-dashboard";
    }

    @GetMapping("/dashboard/accept-shelter")
    public String dashboardAcceptShelter(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        this.sheltersService.updateShelter(id, "Opening");
        Shelters  shelter= sheltersService.findById(id);
        Users u = shelter.getUsers();
        Authorities authorities = u.getAuthorities();
        authorities.setAuthority("ROLE_MANAGER");
        authoritiesService.save(authorities);
        redirectAttributes.addAttribute("page", 0);
        adoptService.sendMailToAgree(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard/remove-shelter")
    public String dashboardRefuseShelter(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        this.sheltersService.updateShelter(id, "Canceled");
        redirectAttributes.addAttribute("page", 0);
        adoptService.sendMailToDisable(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/shelter-list")
    public String adminShelterList(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("listTotalShelters", this.sheltersService.findSheltersByStatusContaining("Opening", pg == 0 ? 0 : pg - 1, 10));
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "shelter");
        return "/admin-shelter-list";
    }

    @GetMapping("/shelter-list/changePage")
    public String changePageAdminShelter(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/admin/shelter-list";
    }

    @GetMapping("/shelter-detail")
    public String adminShelterDetail(Model model, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "id") Long id){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);

        ArrayList<List<String>> listOpen = new ArrayList<>();
        Arrays.stream(this.sheltersService.findById(id).getShelterInfoOperatingTime().split(", ")).forEach(e -> {
            listOpen.add(Arrays.stream(e.split(": ")).toList());
        });
        model.addAttribute("listOpen", listOpen);
        model.addAttribute("shelter", this.sheltersService.findById(id));
        model.addAttribute("listAnimal", this.animalsService.findAllPet(this.sheltersService.findById(id).getUsers().getUserName(), pg == 0 ? 0 : pg - 1, 8));
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "shelter");
        model.addAttribute("animalsService", this.animalsService);

        if(status != null){
            model.addAttribute("status", status);

        }
        return "/admin-shelter-detail";
    }

    @GetMapping("/shelter-detail/changePage")
    public String changePageAdminShelterDetail(@RequestParam("page") int page, @RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/admin/shelter-detail";
    }

    @GetMapping("/shelter-detail/delete")
    public String deleteShelter(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        this.sheltersService.updateShelter(id, "Canceled");
        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addAttribute("status", "Tắt hoạt động của Shelter thành công");
        adoptService.sendMailToDelete(id);
        return "redirect:/admin/shelter-detail";
    }

    @GetMapping("/shelter-detail/accept")
    public String acceptShelter(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        this.sheltersService.updateShelter(id, "Opening");
        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addAttribute("status", "Mở hoạt động của Shelter thành công");
        adoptService.sendMailToAgree(id);
        return "redirect:/admin/shelter-detail";
    }

    @GetMapping("/regist-list")
    public String adminRegistList(Model model, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "status", required = false) String status){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        if(status == null){
            model.addAttribute("listTotalShelters", this.sheltersService.findSheltersByStatusContaining("%",  pg == 0 ? 0 : pg - 1, 6));
        } else {
            model.addAttribute("listTotalShelters", this.sheltersService.findSheltersByStatusContaining(status,  pg == 0 ? 0 : pg - 1, 6));
        }
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("status", status);
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "regist");
        model.addAttribute("sheltersService", this.sheltersService);
        return "/admin-regist-list";
    }

    @GetMapping("/regist-list/changePage")
    public String changePageAdminRegistList(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/admin/regist-list";
    }

    @GetMapping("/regist-list/accept-shelter")
    public String adminAcceptShelter(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        this.sheltersService.updateShelter(id, "Opening");
        redirectAttributes.addAttribute("page", 0);
        adoptService.sendMailToAgree(id);
        return "redirect:/admin/regist-list";
    }

    @GetMapping("/regist-list/remove-shelter")
    public String refuseShelter(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        this.sheltersService.updateShelter(id, "Canceled");
        redirectAttributes.addAttribute("page", 0);
        adoptService.sendMailToDisable(id);
        return "redirect:/admin/regist-list";
    }

    @GetMapping("/user-list")
    public String adminUserList(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("listTotalUsers", this.usersService.findAll(pg == 0 ? 0 : pg - 1, 12));
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "user");
        return "/admin-user-list";
    }

    @GetMapping("/user-list/changePage")
    public String changePageAdminUser(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/admin/user-list";
    }

    @GetMapping("/user-detail")
    public String adminUserDetail(Model model, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "changePassword", required = false) Boolean changePassword, @RequestParam(value = "id") String userName){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("userDetail", this.usersService.findById(userName).get());
        model.addAttribute("page", pg == 0 ? 1 : pg);
        int i = 0;
        for(Adopt a : this.usersService.findById(userName).get().getListAdopt()){
            if(a.getAdopt_status().equals("Cancel")){
                i++;
            }
        }
        model.addAttribute("totalCanceled", i);
        model.addAttribute("listAdopt", this.adoptService.findByUsers(userName, "%", pg == 0 ? 0 : pg - 1, 10));
        if(changePassword != null){
            model.addAttribute("changePassword", changePassword ? "thành công" : "thất bại");
        }
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "user");
        model.addAttribute("breedService", this.breedService);
        model.addAttribute("adoptService", this.adoptService);
        return "/admin-user-detail";
    }

    @GetMapping("/user-detail/changePage")
    public String changePageAdminUserDetail(@RequestParam("page") int page, @RequestParam("id") String id, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/admin/user-detail";
    }

    @PostMapping("/user-detail/resetPassword")
    public String resetPassword(@RequestParam("id") String id, RedirectAttributes redirectAttributes, @RequestParam("password") String password){
        redirectAttributes.addAttribute("id", id);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String enCode = bCryptPasswordEncoder.encode(password);
        Users users = this.usersService.findById(id).get();
        users.setPassword("{bcrypt}"+enCode);
        redirectAttributes.addAttribute("changePassword", this.usersService.changeInfoUser(users,null));
        return "redirect:/admin/user-detail";
    }

    @GetMapping("/user-donate")
    public String adminUserDonate(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMAN);

        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("listSpon", this.sponsService.findAllToPage(pg == 0 ? 0 : pg - 1, 10));
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "donate");

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);
        model.addAttribute("currentDate", formattedDate);

        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        this.sponsService.findAllToPage(0, Integer.MAX_VALUE).stream().toList().forEach(e -> {
            sum.updateAndGet(v -> v + e.getSponsGift());
        });
        String formattedNumber = numberFormat.format(sum.get());
        model.addAttribute("allMoney", formattedNumber);

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        String currentMonth = currentDate.format(monthFormatter);
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
        String currentYear = currentDate.format(yearFormatter);

        Double sumMonth = this.sponsService.findCountGiftInMonth(Integer.valueOf(currentMonth), Integer.valueOf(currentYear));
        model.addAttribute("sumMonth", numberFormat.format(sumMonth));

        Double sumYear = this.sponsService.findCountGiftInYear(Integer.valueOf(currentYear));
        model.addAttribute("sumYear", numberFormat.format(sumYear));

        LocalDate previousMonthDate = currentDate.minusMonths(1);
        String previousMonth = previousMonthDate.format(monthFormatter);
        Double sumPrevMonth = this.sponsService.findCountGiftInMonth(Integer.valueOf(previousMonth), Integer.valueOf(currentYear));
        if(sumMonth - sumPrevMonth < 0){
            model.addAttribute("upToDownMonth", "Giảm");
        } else {
            model.addAttribute("upToDownMonth", "Tăng");
        }
        model.addAttribute("muchMonth", ((sumMonth - sumPrevMonth)/sumPrevMonth)*100);

        LocalDate previousYearDate = currentDate.minusYears(1);
        String previousYear = previousYearDate.format(yearFormatter);
        Double sumPrevYear = this.sponsService.findCountGiftInYear(Integer.valueOf(previousYear));
        if(sumYear - sumPrevYear < 0){
            model.addAttribute("upToDownYear", "Giảm");
        } else {
            model.addAttribute("upToDownYear", "Tăng");
        }
        model.addAttribute("muchYear", ((sumYear - sumPrevYear)/sumPrevYear)*100);

        return "/admin-user-donate";
    }

    @GetMapping("/user-donate/changePage")
    public String changePageAdminDonate(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/admin/user-donate";
    }

    @GetMapping("/donate-detail")
    public String adminUserDonateDetail(Model model, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "id") Long id){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        Users users = this.sponsService.findById(id).getUsers();
        model.addAttribute("users", users);
        model.addAttribute("id", id);
        model.addAttribute("listSpon", this.sponsService.findAllByUser(pg == 0 ? 0 : pg - 1, 10, users.getUserName()));
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "donate");
        return "/admin-user-donate-detail";
    }

    @GetMapping("/donate-detail/changePage")
    public String changePageAdminDonateDetail(@RequestParam("page") int page, @RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/admin/donate-detail";
    }

    @GetMapping("/token-shop")
    public String tokenShop(Model model){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "tokenShop");
        return "redirect:/coming-soon";
    }

    @GetMapping("/setting")
    public String adminSetting(Model model){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "setting");
        return "redirect:/coming-soon";
    }
}
