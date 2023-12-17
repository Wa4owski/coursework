package databases.itmo.coursework.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PingController {

    @GetMapping(path = "/")
    public String base(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("customer", auth.getName());
        return "redirect:customer/new-order";
    }

    @GetMapping(path = "/executor/1")
    String adminGreeting1(){
        return "Hello, admin1!";
    }


    @GetMapping(path = "/customer/1")
    String userGreeting1(){
        return "Hello, customer1!";
    }

}
