package vn.finder.pet.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.finder.pet.service.UsersService;

@Controller
public class ExceptionController implements ErrorController {
    private UsersService usersService;

    @Autowired
    public ExceptionController(UsersService usersService) {
        this.usersService = usersService;
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

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        String errorPage = "error";

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorPage = "redirect:/exception/404";

            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorPage = "redirect:/exception/403";

            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorPage = "redirect:/exception/500";
            }
        }

        return errorPage;
    }

    @GetMapping("/exception/{id}")
    public String exceptionId(@PathVariable String id, Model model){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("exception", id);
        return "/error";
    }
}