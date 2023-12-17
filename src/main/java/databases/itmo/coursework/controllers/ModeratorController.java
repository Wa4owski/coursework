package databases.itmo.coursework.controllers;


import databases.itmo.coursework.model.Verdict;
import databases.itmo.coursework.security.UserPrincipal;
import databases.itmo.coursework.servises.ModeratorService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@AllArgsConstructor
@RequestMapping(path="/moderator")
public class ModeratorController {

    private final ModeratorService moderatorService;

    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("tickets", moderatorService.getTicketsByModeratorId((Integer) model.getAttribute("moderatorId")));
        return "/moderator/moderator";
    }

    @GetMapping(path = "/new-verdict")
    public String newVerdictPage(){
        return "new-verdict";
    }

    @GetMapping(path="/addVerdict")
    public String newVerdictPage(@RequestParam(name="orderId") Integer orderId, Model model) {
        model.addAttribute("orderId", orderId);
        model.addAttribute("verdict", new Verdict());
        return "/moderator/new-verdict";
    }

    @PostMapping(path="/new-verdict")
    public ModelAndView newVerdict(Model model,
                             @RequestParam(name="order_id") Integer orderId) {
        Verdict newVerdict = new Verdict();
        newVerdict.setOrderId(orderId);
        model.addAttribute("verdict", newVerdict);
        return new ModelAndView("moderator/new-verdict");
    }

    @PostMapping(path="/addVerdict")
    public String addVerdict(@ModelAttribute("verdict") Verdict verdict,
                             Model model) {
        moderatorService.putVerdict(verdict);
        return "redirect:/moderator";
    }

    @ModelAttribute
    public void addAttributesForMenu(Model model, Authentication auth){
        UserPrincipal user = (UserPrincipal)auth.getPrincipal();
        model.addAttribute("userName", user.getPersonEntity().getFullName());
        model.addAttribute("moderatorId", user.getUserSpecId());
    }

}
