package databases.itmo.coursework.controllers;

import databases.itmo.coursework.model.FeedbackDTO;
import databases.itmo.coursework.model.OrderRequest;
import databases.itmo.coursework.security.UserPrincipal;
import databases.itmo.coursework.servises.ExecutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("orderRequests", executorService.getFreeOrderRequestsByCompetence(competenceName));
        return "/executor/orderRequests";
    }

    @PostMapping(path = "/orderRequests/choose")
    public String chooseOrderRequest(@ModelAttribute("chosenOrderRequest") OrderRequest orderRequest,
                                     Model model){
        executorService.chooseOrderRequest(orderRequest, (Integer)model.getAttribute("executorId"));
        return "redirect:/executor/orderRequests/"+orderRequest.getCompetence();
    }

    @GetMapping(path = "/myOrderRequests/public")
    public String myPublicOrderRequests(@ModelAttribute("publicOrderRequest") OrderRequest orderRequest,
                                        Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        model.addAttribute("orderRequests", executorService.getPublicOrderRequestsByExecutorId(executorId));
        return "executor/myOrderRequests";
    }

    @GetMapping(path = "/myOrderRequests/private")
    public String myPrivateOrderRequests(@ModelAttribute("privateOrderRequest") OrderRequest orderRequest,
                                        Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        model.addAttribute("orderRequests", executorService.getPrivateOrderRequestsByExecutorId(executorId));
        return "executor/myOrderRequests";
    }

    @PostMapping(path = "/myOrderRequests/private/decline")
    public String declineMyPrivateOrderRequest(@ModelAttribute("privateOrderRequest") OrderRequest orderRequest,
                                             Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        executorService.declinePrivateOrderRequest(executorId, orderRequest.getId());
        return "redirect:/executor/myOrderRequests/private";
    }

    @PostMapping(path = "/myOrderRequests/private/accept")
    public String acceptMyPrivateOrderRequest(@ModelAttribute("privateOrderRequest") OrderRequest orderRequest,
                                               Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        executorService.acceptPrivateOrderRequest(executorId, orderRequest.getId());
        return "redirect:/executor/myOrderRequests/private";
    }

    @PostMapping(path = "/myOrderRequests/public/revoke")
    public String revokeMyPublicOrderRequest(@ModelAttribute("publicOrderRequest") OrderRequest orderRequest,
                                             Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        executorService.revokeAgrToPublicOrderRequest(executorId, orderRequest.getId());
        return "redirect:/executor/myOrderRequests/public";
    }

    @GetMapping(path = "/orders")
    public String getMyActiveOrders(@ModelAttribute("feedback")FeedbackDTO feedbackDTO,
                                    Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        model.addAttribute("orders", executorService.getExecutorOrdersWithNoFeedback(executorId));
        return "executor/orders";
    }

    @PostMapping("orders/sendFeedback")
    public String sendFeedback(@Valid @ModelAttribute("feedback") FeedbackDTO feedback,
                               BindingResult result,
                               Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        if(result.hasErrors()){
            model.addAttribute("orders", executorService.getExecutorOrdersWithNoFeedback(executorId));
            return "/customer/orders";
        }
        executorService.sendFeedback(feedback, executorId);
        model.addAttribute("orders",executorService.getExecutorOrdersWithNoFeedback(executorId));
        return "redirect:/customer/orders";
    }


    @ModelAttribute
    public void addAttributesForMenu(Model model, Authentication auth){
        model.addAttribute("competences", executorService.getAllCompetences());
        UserPrincipal user = (UserPrincipal)auth.getPrincipal();
        model.addAttribute("userName", user.getPersonEntity().getFullName());
        model.addAttribute("executorId", user.getUserSpecId());
    }

}
