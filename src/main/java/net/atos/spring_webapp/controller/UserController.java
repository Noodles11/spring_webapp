package net.atos.spring_webapp.controller;

import net.atos.spring_webapp.model.User;
import net.atos.spring_webapp.service.AutoMailingService;
import net.atos.spring_webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    private AutoMailingService autoMailingService;

    @Autowired
    public UserController(AutoMailingService autoMailingService, UserService userService) {
        this.autoMailingService = autoMailingService;
        this.userService = userService;
    }

    private UserService userService;

    @GetMapping("/users")
    public String allUsers(Model model){
        model.addAttribute("users",userService.getAllUsersOrdered(Sort.Direction.ASC));
        return "users";
    }

    @GetMapping("/user&{userId}")
    public String getUserById(Model model, @PathVariable("userId") long userId){
        model.addAttribute("user",userService.getUserById(userId));

        return "user";
    }

    @GetMapping("/register")
    public String registerUser(Model model){
        model.addAttribute("user",new User());
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "register";
        }
        userService.registerUser(user);
        System.out.println("dodano użytkownika!");

        autoMailingService.sendEmail(
                user.getEmail(),
                "Example",
                "Example content"
        );
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

//    @GetMapping("/login_google")
//    public Principal loggedUser(Principal principal){
//        System.out.println("logowanie po google api");
//        return principal;
//    }
}
