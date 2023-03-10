package databases.itmo.coursework.controllers;


import databases.itmo.coursework.model.Verdict;
import databases.itmo.coursework.servises.ModeratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(path="/moderator")
public class ModeratorController {

    @Autowired
    ModeratorService moderatorService;

    @GetMapping
    public String mainPage() {
        return "/moderator/moderator";
    }

    @GetMapping(path = "tickets")
    public String getTickets(@ModelAttribute("orderId") Integer orderId, Model model){

        return "";
    }

    @GetMapping(path="/addVerdict")
    public String newVerdictPage(@RequestParam(name="order_id") Integer order_id, Model model) {
        model.addAttribute("order_id", order_id);
        model.addAttribute("verdict", new Verdict());
        return "/moderator/newVerdict";
    }

    @PostMapping(path="/newVerdict")
    public String addVerdict(@RequestParam(name="new_rate_for_executor") Integer new_rate_for_executor,
                             @RequestParam(name="delete_feedback_about_executor") Boolean delete_feedback_about_executor,
                             @RequestParam(name="ban_executor") Boolean ban_executor,
                             @RequestParam(name="new_rate_for_customer") Integer new_rate_for_customer,
                             @RequestParam(name="delete_feedback_about_customer") Boolean delete_feedback_about_customer,
                             @RequestParam(name="ban_customer") Boolean ban_customer,
                             @RequestParam(name="order_id") Integer order_id) {
        return "/moderator/moderator";
    }

}
