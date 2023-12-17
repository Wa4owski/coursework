package databases.itmo.coursework.controllers;

import databases.itmo.coursework.model.FeedbackDTO;
import databases.itmo.coursework.model.OrderRequest;
import databases.itmo.coursework.security.UserPrincipal;
import databases.itmo.coursework.servises.ExecutorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/executor")
public class ExecutorController {

    private final ExecutorService executorService;

    @GetMapping(path = "/order-requests/{competence}")
    public String getPublicOrderRequests(@PathVariable(name = "competence") String competenceName,
                                         @ModelAttribute("chosenOrderRequest") OrderRequest orderRequest,
                                         Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        model.addAttribute("competenceMatches", executorService.isCompetenceMatchesExecutor(executorId, competenceName));
        model.addAttribute("orderRequests", executorService.getFreeOrderRequestsByCompetence(executorId, competenceName));
        return "/executor/order-requests";
    }

    @GetMapping(path = "/customer-feedbacks")
    public String getExecutorProfilePage(@RequestParam("customerId") Integer executorId,
                                         Model model){
        model.addAttribute("customer", executorService.getCustomerById(executorId));
        model.addAttribute("feedbacks", executorService.getAllCustomerFeedbacks(executorId));
        return "executor/customer-feedbacks";
    }

    @PostMapping(path = "/order-requests/choose")
    public String chooseOrderRequest(@ModelAttribute("chosenOrderRequest") OrderRequest orderRequest,
                                     Model model){
        executorService.chooseOrderRequest(orderRequest, (Integer)model.getAttribute("executorId"));
        return "redirect:/executor/order-requests/" + encodePath(orderRequest.getCompetence());
    }

    @GetMapping(path = "/my-order-requests/public")
    public String myPublicOrderRequests(@ModelAttribute("publicOrderRequest") OrderRequest orderRequest,
                                        Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        model.addAttribute("orderRequests", executorService.getPublicOrderRequestsByExecutorId(executorId));
        return "/executor/my-order-requests";
    }

    @GetMapping(path = "/my-order-requests/private")
    public String myPrivateOrderRequests(@ModelAttribute("privateOrderRequest") OrderRequest orderRequest,
                                        Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        model.addAttribute("orderRequests", executorService.getPrivateOrderRequestsByExecutorId(executorId));
        return "/executor/my-order-requests";
    }

    @PostMapping(path = "/my-order-requests/private/decline")
    public String declineMyPrivateOrderRequest(@ModelAttribute("privateOrderRequest") OrderRequest orderRequest,
                                             Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        executorService.declinePrivateOrderRequest(executorId, orderRequest.getId());
        return "redirect:/executor/my-order-requests/private";
    }

    @PostMapping(path = "/my-order-requests/private/accept")
    public String acceptMyPrivateOrderRequest(@ModelAttribute("privateOrderRequest") OrderRequest orderRequest,
                                               Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        executorService.acceptPrivateOrderRequest(executorId, orderRequest.getId());
        return "redirect:/executor/my-order-requests/private";
    }

    @PostMapping(path = "/my-order-requests/public/revoke")
    public String revokeMyPublicOrderRequest(@ModelAttribute("publicOrderRequest") OrderRequest orderRequest,
                                             Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        executorService.revokeAgrToPublicOrderRequest(executorId, orderRequest.getId());
        return "redirect:/executor/my-order-requests/public";
    }

    @GetMapping(path = "/orders")
    public String getMyActiveOrders(@ModelAttribute("feedback")FeedbackDTO feedbackDTO,
                                    Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        model.addAttribute("orders", executorService.getExecutorOrdersWithNoFeedback(executorId));
        return "executor/orders";
    }

    @PostMapping("/orders/send-feedback")
    public String sendFeedback(@Valid @ModelAttribute("feedback") FeedbackDTO feedback,
                               BindingResult result,
                               Model model){
        Integer executorId = (Integer)model.getAttribute("executorId");
        if(result.hasErrors()){
            model.addAttribute("orders", executorService.getExecutorOrdersWithNoFeedback(executorId));
            return "/customer/orders";
        }
        executorService.sendFeedback(feedback, executorId);
        model.addAttribute("orders", executorService.getExecutorOrdersWithNoFeedback(executorId));
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
