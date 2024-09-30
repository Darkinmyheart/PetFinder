package vn.finder.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.finder.pet.service.AnimalsService;
import vn.finder.pet.service.BreedService;
import vn.finder.pet.service.SheltersService;
import vn.finder.pet.service.UsersService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/shelter")
public class ShelterController {
    private SheltersService sheltersService;
    private UsersService usersService;
    private AnimalsService animalsService;
    private BreedService breedService;

    @Autowired
    public ShelterController(SheltersService sheltersService, UsersService usersService, AnimalsService animalsService, BreedService breedService) {
        this.sheltersService = sheltersService;
        this.usersService = usersService;
        this.animalsService = animalsService;
        this.breedService = breedService;
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

    @GetMapping("/list")
    public String getShelterList(Model model, @RequestParam(value = "page", required = false) String page , @RequestParam(value = "location", required = false) String location , @RequestParam(value = "name", required = false) String name){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);

        if(location != null || name != null){
            model.addAttribute("listShelter", this.sheltersService.findByShelterNameAndShelterAddress("Opening", name, location, 0, Integer.MAX_VALUE));
            model.addAttribute("totalShelter", this.sheltersService.findByShelterNameAndShelterAddress("Opening", name, location, 0, Integer.MAX_VALUE).getTotalElements());
        } else {
            model.addAttribute("listShelter", this.sheltersService.findSheltersByStatusContaining("Opening", pg == 0 ? 0 : pg - 1, 10));
            model.addAttribute("totalShelter", this.sheltersService.findSheltersByStatusContaining("Opening", 0, Integer.MAX_VALUE).getTotalElements());
        }

        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("page", pg == 0 ? 1 : pg);
        return "shelter-list";
    }

    @GetMapping("/list/changePage")
    public String changePageShelterList(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/shelter/list";
    }

    @GetMapping("/detail")
    public String getShelterDetail(Model model, @RequestParam(value = "id") Long id, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "active", required = false) String active){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        if(active == null){
            active = "overview";
        }

        ArrayList<List<String>> listOpen = new ArrayList<>();
        Arrays.stream(this.sheltersService.findById(id).getShelterInfoOperatingTime().split(", ")).forEach(e -> {
            listOpen.add(Arrays.stream(e.split(": ")).toList());
        });
        model.addAttribute("listOpen", listOpen);
        model.addAttribute("listAnimal", this.animalsService.findAllPet(this.sheltersService.findById(id).getUsers().getUserName(),  pg == 0 ? 0 : pg - 1, 6));
        model.addAttribute("animalsService", this.animalsService);
        model.addAttribute("breedService", this.breedService);
        model.addAttribute("active", active);
        model.addAttribute("shelter", this.sheltersService.findById(id));
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("page", pg == 0 ? 1 : pg);

        return "shelter-detail";
    }

    @GetMapping("/detail/changePage")
    public String changePageShelterDetail(@RequestParam("page") int page, @RequestParam("id") Long id, @RequestParam("active") String active, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addAttribute("active", active);
        return "redirect:/shelter/detail";
    }
}
