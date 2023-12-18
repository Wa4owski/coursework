package databases.itmo.coursework.controllers;

import databases.itmo.coursework.model.FeedbackDTO;
import databases.itmo.coursework.model.OrderRequest;
import databases.itmo.coursework.model.OrderRequestExecutorIdDto;
import databases.itmo.coursework.model.OrderVisibility;
import databases.itmo.coursework.security.UserPrincipal;
import databases.itmo.coursework.servises.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/customer")
public class CustomerController extends BaseController{

    private final CustomerService customerService;

    @GetMapping(path = "/executors/{competence}")
    public String executorsByCompetence(@PathVariable(name = "competence") String competenceName,
                                        @RequestParam(required = false) Integer orderRequestId,
                                        @RequestParam(name = "message", required = false) String message,
                                        Model model){
        if(orderRequestId != null){
            model.addAttribute("orderRequestId", orderRequestId);
            model.addAttribute("executors", customerService.getFreeExecutorsByCompetence(orderRequestId));
        }
        else{
            model.addAttribute("executors", customerService.getExecutorsByCompetence(competenceName));
        }
        if(message != null){
            model.addAttribute("message", message);
        }
        model.addAttribute("competence", competenceName);
        return "customer/executors";
    }

    @GetMapping(path = "/new-order")
    public String newOrderPage(@ModelAttribute("orderRequest") OrderRequest orderRequest,
                               Model model){
        return "/customer/new-order";
    }

    @GetMapping(path = "/order-requests")
    public String newPerson(@ModelAttribute("chooseOrderRequest") OrderRequest orderRequest,
                            @ModelAttribute("orderRequestIdExecutorId") OrderRequestExecutorIdDto requestIdExecutorIdDto,
                            Model model) {
        Integer customerId = (Integer)model.getAttribute("customerId");
        model.addAttribute("orderRequests", customerService.getOpenedOrderRequests(customerId));
        return "customer/order-requests";
    }


    @PostMapping
    public ModelAndView createOrder(@Valid @ModelAttribute("orderRequest") OrderRequest orderRequest,
                                    BindingResult result,  ModelMap model) {
        if (result.hasErrors()) {
            return new ModelAndView("customer/new-order", model);
        }
        Integer customerId = (Integer)model.getAttribute("customerId");
        Integer orderRequestId = customerService.createNewOrderRequest(customerId, orderRequest);
        orderRequest.setId(orderRequestId);
        if(orderRequest.getAccess().equals(OrderVisibility.public_)) {
            return new ModelAndView("redirect:/customer/order-requests");
        }
        else {
            ModelMap queryParam = new ModelMap("orderRequestId", orderRequestId);
            return new ModelAndView("redirect:/customer/executors/" + encodePath(orderRequest.getCompetence()), queryParam);
        }
    }

    @PostMapping(path = "/add-executor")
    public ModelAndView addExecutor(@RequestParam("orderRequestId") Integer orderRequestId,
                                    @RequestParam("executorId") Integer executorId,
                                    @RequestParam("competence") String competence,
                                    Model model,
                                    ModelMap redirectParams) {
        Integer customerId = (Integer)model.getAttribute("customerId");
        OrderRequestExecutorIdDto orderRequestExecutorId = new OrderRequestExecutorIdDto(orderRequestId, executorId);
        int placesRemain = customerService.addExecutorToOrderRequest(orderRequestExecutorId, customerId);
        if (placesRemain > 0) {
            redirectParams.addAttribute("message", String.format("Исполнитель получил ваше приглашение, отследить его согласие вы можете в разделе" +
                    " Мои заявки. Вы можете выбрать еще %d исполнителя для выполнения вашего заказа.", placesRemain));
            redirectParams.addAttribute("orderRequestId", orderRequestId);
            return new ModelAndView("redirect:/customer/executors/" + encodePath(competence), redirectParams);
        }
        return new ModelAndView("redirect:/customer/order-requests");
    }

    @GetMapping("/orders")
    public String getMyOrders(@ModelAttribute("feedback") FeedbackDTO feedback,
                              Model model){
        Integer customerId = (Integer)model.getAttribute("customerId");
        model.addAttribute("orders", customerService.getCustomerOrdersWithNoFeedback(customerId));
        return "/customer/orders";
    }

    @PostMapping("orders/send-feedback")
    public String sendFeedback(@Valid @ModelAttribute("feedback") FeedbackDTO feedback,
                               BindingResult result,
                               Model model){
        Integer customerId = (Integer)model.getAttribute("customerId");
        if(result.hasErrors()){
            model.addAttribute("orders", customerService.getCustomerOrdersWithNoFeedback(customerId));
            return "/customer/orders";
        }
        customerService.sendFeedback(feedback, customerId);
        model.addAttribute("orders", customerService.getCustomerOrdersWithNoFeedback(customerId));
        return "redirect:/customer/orders";
    }

    @PostMapping("/order-requests/decline")
    public String declineExecutor(@RequestParam Integer orderRequestId,
                                  @RequestParam Integer executorId,
                                  Authentication auth,
                                  Model model){
        Integer customerId = ((UserPrincipal) auth.getPrincipal()).getUserSpecId();
        customerService.declineExecutor(new OrderRequestExecutorIdDto(orderRequestId, executorId), customerId);
        model.addAttribute("orderRequests", customerService.getOpenedOrderRequests(executorId));
        return "customer/order-requests";
    }

    @PostMapping("/order-requests/accept")
    public String acceptExecutorRequest(@ModelAttribute("orderRequestIdExecutorId") OrderRequestExecutorIdDto requestIdExecutorIdDto,
                                        Model model){
        Integer customerId = (Integer) model.getAttribute("customerId");
        customerService.acceptExecutor(requestIdExecutorIdDto,
                customerId);
        model.addAttribute("orderRequests", customerService.getOpenedOrderRequests(customerId));
        return "customer/order-requests";
    }

    @GetMapping("/order-requests/choose")
    public ModelAndView choosePrivateExecutors(@ModelAttribute("chooseOrderRequest") OrderRequest orderRequest,
                                         Model model, ModelMap redirectParams){
        if(customerService.privateExecutorsPlacesRemain(orderRequest.getId(),
                (Integer)model.getAttribute("customerId")) > 0){
            redirectParams.addAttribute("orderRequestId", orderRequest.getId());
            return new ModelAndView("redirect:/customer/executors/" + encodePath(orderRequest.getCompetence()), redirectParams);
        }
        model.addAttribute("error", "Вы пригласили максимально допустимое число исполнителей." +
                "Если хотите заменить кого-то, сперва отзовите запрос");
        return new ModelAndView("redirect:/customer/order-requests");
    }

    @GetMapping(path = "/executor-feedbacks")
    public String getExecutorProfilePage(@RequestParam("executorId") Integer executorId,
                                         Model model){
        model.addAttribute("executor", customerService.getExecutorById(executorId));
        model.addAttribute("feedbacks", customerService.getAllExecutorsFeedbacks(executorId));
        return "/customer/executor-feedbacks";
    }

    @Override
    public void addAttributesForMenu(Model model, Authentication auth){
        model.addAttribute("competences", customerService.getAllCompetences());
        UserPrincipal user = (UserPrincipal)auth.getPrincipal();
        model.addAttribute("userName", user.getPersonEntity().getFullName());
        model.addAttribute("customerId", user.getUserSpecId());
    }

}
