package vn.finder.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.finder.pet.entity.AnimalInfo;
import vn.finder.pet.entity.Animals;
import vn.finder.pet.entity.Breed;
import vn.finder.pet.service.*;

import java.util.ArrayList;
import java.util.Calendar;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    private AnimalsService animalsService;
    private AdoptService adoptService;
    private FavoritesService favoritesService;
    private UsersService usersService;
    private SponsService sponsService;
    private BreedService breedService;
    private SheltersService sheltersService;

    @Autowired
    public ManagerController(AnimalsService animalsService, AdoptService adoptService, FavoritesService favoritesService, UsersService usersService, SponsService sponsService, BreedService breedService, SheltersService sheltersService) {
        this.animalsService = animalsService;
        this.adoptService = adoptService;
        this.favoritesService = favoritesService;
        this.usersService = usersService;
        this.sponsService = sponsService;
        this.breedService = breedService;
        this.sheltersService = sheltersService;
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
    public String agentDashboard(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);

        model.addAttribute("availablePets", this.animalsService.findCountAnimalsAvailable(this.getEmailLogin()));
        model.addAttribute("awaitingApproval", this.animalsService.findCountAwaitingAnimals(this.getEmailLogin()));
        model.addAttribute("totalInteractions", this.animalsService.findCountAdopt(this.getEmailLogin()) + this.animalsService.findCountFavorite(this.getEmailLogin()));
        model.addAttribute("listAwaiting", this.animalsService.findByStatus(this.getEmailLogin(), "Awaiting",  pg == 0 ? 0 : pg - 1, 6));
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("user", this.usersService.findById(this.getEmailLogin()).get());

        ArrayList<Integer> listInteraction = new ArrayList<>();
        listInteraction.add(this.favoritesService.findByMonthAndShelter(1, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(1, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(2, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(2, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(3, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(3, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(4, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(4, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(5, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(5, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(6, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(6, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(7, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(7, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(8, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(8, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(9, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(9, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(10, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(10, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(11, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(11, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(12, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(12, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));

        model.addAttribute("listInteraction", listInteraction);
        model.addAttribute("statusActive", "dashboard");

        return "/agent-dashboard";
    }

    @GetMapping("/dashboard/changePage")
    public String changePageAgentDashboard(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/manager/dashboard";
    }

    @GetMapping("/dashboard/disable-pet")
    public String disablePetDashboard(@RequestParam(value = "id") Long id){
        this.adoptService.disablePet(id);
        return "redirect:/manager/dashboard";
    }

    @GetMapping("/dashboard/enable-pet")
    public String enablePetDashboard(@RequestParam(value = "id") Long id){
        this.adoptService.enablePet(id);
        return "redirect:/manager/dashboard";
    }

    @GetMapping("/listings")
    public String agentListings(Model model, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "status", required = false) String status){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);

        model.addAttribute("allPets", this.animalsService.findCountAnimalsInShelter(this.getEmailLogin()));
        model.addAttribute("awaitingPets", this.animalsService.findCountAwaitingAnimals(this.getEmailLogin()));
        model.addAttribute("adpotedPets", this.animalsService.findCountAnimalsAdopted(this.getEmailLogin()));
        model.addAttribute("availablePets", this.animalsService.findCountAnimalsAvailable(this.getEmailLogin()));
        Page<Animals> listAnimals = this.animalsService.findAllPet(this.getEmailLogin(), pg == 0 ? 0 : pg - 1, 6);
        ArrayList<Long> l = new ArrayList<>();
        listAnimals.stream().toList().forEach(e -> {
            for (int i = 0; i < e.getListAdopt().size(); i++) {
                if(e.getListAdopt().get(i).getAnimals().getId() == e.getId() && !e.getListAdopt().get(i).getAdopt_status().equals("Cancel")){
                    l.add(e.getListAdopt().get(i).getAnimals().getId());
                    break;
                }
            }
        });
        model.addAttribute("listAdopt", l);
        model.addAttribute("listAnimals", listAnimals);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("user", this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "listings");
        model.addAttribute("status", status);
        model.addAttribute("animalsService", this.animalsService);
        model.addAttribute("breedService", this.breedService);

        return "/agent-listings";
    }

    @GetMapping("/listings/changePage")
    public String changePageAgentListings(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/manager/listings";
    }

    @GetMapping("/delete-animal")
    public String deleteAnimal(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        if(this.animalsService.deleteAnimal(id)){
            redirectAttributes.addAttribute("status", "Delete thành công");
        } else {
            redirectAttributes.addAttribute("status", "Delete thất bại");
        }
        return "redirect:/manager/listings";
    }

    @GetMapping("/listings/disable-pet")
    public String disablePet(@RequestParam(value = "id") Long id){
        this.adoptService.disablePet(id);
        return "redirect:/manager/listings";
    }

    @GetMapping("/listings/enable-pet")
    public String enablePet(@RequestParam(value = "id") Long id){
        this.adoptService.enablePet(id);
        return "redirect:/manager/listings";
    }

    @GetMapping("/bookings")
    public String agentBookings(Model model, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "query", required = false) String query){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        if(status == null){
            status = "%";
        }

        if(query != null){
            model.addAttribute("listDonate", this.sponsService.findByStatus(this.getEmailLogin(), pg == 0 ? 0 : pg - 1, 10));
            model.addAttribute("queryStatus", "Donate");
        } else {
            model.addAttribute("listAdopt", this.animalsService.findByStatus(this.getEmailLogin(), status, pg == 0 ? 0 : pg - 1, 10));
            model.addAttribute("queryStatus", "Adopt");
        }
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("user", this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "bookings");

        return "/agent-bookings";
    }

    @GetMapping("/bookings/changePage")
    public String changePageAgentBooking(@RequestParam("page") int page, @RequestParam(value = "query", required = false) String query, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        if(query != null){
            redirectAttributes.addAttribute("query", query);
        }
        return "redirect:/manager/bookings";
    }

    @GetMapping("/bookings/disable-pet")
    public String disablePetBookings(@RequestParam(value = "id") Long id){
        this.adoptService.disablePet(id);
        return "redirect:/manager/bookings";
    }

    @GetMapping("/bookings/enable-pet")
    public String enablePetBookings(@RequestParam(value = "id") Long id){
        this.adoptService.enablePet(id);
        return "redirect:/manager/bookings";
    }

    @GetMapping("/history")
    public String agentNotify(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("user", this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("listNotify", this.sheltersService.findAllHistoryActiveOfShelter(pg == 0 ? 0 : pg - 1, 10, this.getEmailLogin()));
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("statusActive", "notify");
        return "/agent-notify";
    }

    @GetMapping("/notify/changePage")
    public String changePageAgentNotify(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/manager/history";
    }

    @GetMapping("/add-animal")
    public String addListingMinimal(Model model, @RequestParam(value = "error", required = false) Boolean error){
        model.addAttribute("user", this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("title", "Thêm Thú Cưng");
        model.addAttribute("button", "Thêm thú cưng");
        Animals animals = new Animals();
        animals.setBreed(new Breed(null, "", ""));
        animals.setAnimalInfo(new AnimalInfo(null, "", "", "", "", "", "", animals));
        model.addAttribute("animal", animals);
        model.addAttribute("listBreed", this.breedService.findAllBreedType());
        model.addAttribute("listName", new ArrayList<>());
        model.addAttribute("breedService", this.breedService);
        if(error != null){
            if(!error){
                model.addAttribute("error", "Thêm animal thành công");
            } else {
                model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            }
        }
        return "/add-listing-minimal";
    }

    @GetMapping("/edit-animal")
    public String editListingMinimal(Model model, @RequestParam(value = "id") Long id, @RequestParam(value = "error", required = false) Boolean error){
        model.addAttribute("user", this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("title", "Cập Nhật Thú Cưng");
        model.addAttribute("button", "Cập nhật");
        model.addAttribute("id", id);
        Animals animals = this.animalsService.findById(id);
        model.addAttribute("animal", animals);
        model.addAttribute("listBreed", this.breedService.findAllBreedType());
        model.addAttribute("listName", this.breedService.findByBreed_type(animals.getBreed().getBreed_type()));
        model.addAttribute("breedService", this.breedService);

        if(error != null){
            if(!error){
                model.addAttribute("error", "Chỉnh sửa animal thành công");
            } else {
                model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            }
        }
        return "/add-listing-minimal";
    }
}
