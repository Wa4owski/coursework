package databases.itmo.coursework.controllers;

import databases.itmo.coursework.model.OrderRequest;
import databases.itmo.coursework.security.UserPrincipal;
import databases.itmo.coursework.servises.ExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/executor")
public class ExecutorController {

    @Autowired
    ExecutorService executorService;

    @GetMapping(path = "/orderRequests/{competence}")
    public String getPublicOrderRequests(@PathVariable(name = "competence") String competenceName,
                                         @ModelAttribute("chosenOrderRequest") OrderRequest orderRequest,
                                         Model model){
        model.addAttribute("orderRequests", executorService.getOrderRequestsByCompetence(competenceName));
        return "/executor/orderRequests";
    }

    @PostMapping(path = "/orderRequests/choose")
    public String chooseOrderRequest(@ModelAttribute("chosenOrderRequest") OrderRequest orderRequest,
                                     Model model){
        executorService.chooseOrderRequest(orderRequest, (Integer)model.getAttribute("executorId"));
        return "redirect:/executor/orderRequests/"+orderRequest.getCompetence();
    }

    @ModelAttribute
    public void addAttributesForMenu(Model model, Authentication auth){
        model.addAttribute("competences", executorService.getAllCompetences());
        UserPrincipal user = (UserPrincipal)auth.getPrincipal();
        model.addAttribute("userName", user.getPersonEntity().getFullName());
        model.addAttribute("executorId", user.getUserSpecId());
    }

}
