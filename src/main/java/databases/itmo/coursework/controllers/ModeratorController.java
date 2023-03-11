package databases.itmo.coursework.controllers;


import databases.itmo.coursework.model.Verdict;
import databases.itmo.coursework.security.UserPrincipal;
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
    public String mainPage(Model model) {
        model.addAttribute("tickets", moderatorService.getTicketsByModeratorId((Integer) model.getAttribute("moderatorId")));
        return "/moderator/moderator";
    }

    @GetMapping(path = "/newVerdict")
    public String newVerdictPage(){
        return "/moderator/newVerdict";
    }

    @GetMapping(path="/addVerdict")
    public String newVerdictPage(@RequestParam(name="order_id") Integer order_id, Model model) {
        model.addAttribute("order_id", order_id);
        model.addAttribute("verdict", new Verdict());
        return "/moderator/newVerdict";
    }

    @PostMapping(path="/newVerdict")
    public ModelAndView newVerdict(Model model,
                             @RequestParam(name="order_id") Integer order_id) {
        Verdict newVerdict = new Verdict();
        newVerdict.setOrder_id(order_id);
        model.addAttribute("verdict", newVerdict);
        return new ModelAndView("/moderator/newVerdict");
    }

    @PostMapping(path="/addVerdict")
    public String addVerdict(Model model) {
        // Тут нужно загрузить verdict в бд
        return "redirect:/moderator";
    }

    @ModelAttribute
    public void addAttributesForMenu(Model model, Authentication auth){
        UserPrincipal user = (UserPrincipal)auth.getPrincipal();
        model.addAttribute("userName", user.getPersonEntity().getFullName());
        model.addAttribute("moderatorId", user.getUserSpecId());
    }

}
