package databases.itmo.coursework.controllers;

import databases.itmo.coursework.model.*;
import databases.itmo.coursework.security.UserPrincipal;
import databases.itmo.coursework.servises.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(path = "/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping(path = "/executors/{competence}")
    public String executorsByCompetence(@PathVariable(name = "competence") String competenceName,
                                        @RequestParam(required = false) Integer orderRequestId,
                                        @RequestParam(name = "message", required = false) String message,
                                        Model model){
        if(orderRequestId != null){
            model.addAttribute("orderRequestId", orderRequestId);
        }
        if(message != null){
            model.addAttribute("message", message);
        }
        model.addAttribute("competence", competenceName);
        List<Executor> executors = customerService.getExecutorsByCompetence(competenceName);
        model.addAttribute("executors", executors);
        return "customer/executors";
    }

    @GetMapping(path = "/newOrder")
    public String newOrderPage(@ModelAttribute("orderRequest") OrderRequest orderRequest,
                               Model model){

        return "customer/newOrder";
    }

    @GetMapping(path = "/orderRequests")
    public String newPerson(@ModelAttribute("chooseOrderRequest") OrderRequest orderRequest,
                            @ModelAttribute("orderRequestIdExecutorId") OrderRequestExecutorIdDto requestIdExecutorIdDto,
                            Model model) {
        Integer customerId = (Integer)model.getAttribute("customerId");
        model.addAttribute("orderRequests", customerService.getOpenedOrderRequests(customerId));
        return "customer/orderRequests";
    }


    @PostMapping
    public ModelAndView createOrder(@Valid @ModelAttribute("orderRequest") OrderRequest orderRequest,
                                    BindingResult result, Authentication auth, ModelMap model) {
        if (result.hasErrors()) {
            return new ModelAndView("customer/newOrder", model);
        }
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Integer customerId = userPrincipal.getUserSpecId();
        Integer orderRequestId = customerService.createNewOrderRequest(customerId, orderRequest);
        orderRequest.setId(orderRequestId);
        if(orderRequest.getAccess().equals(OrderVisibility.public_)) {
            return new ModelAndView("redirect:/customer/orderRequests");
        }
        else {
            ModelMap queryParam = new ModelMap("orderRequestId", orderRequestId);
            return new ModelAndView("redirect:/customer/executors/" + orderRequest.getCompetence(), queryParam);
        }
    }

    @PostMapping(path = "/addExecutor")
    public ModelAndView addExecutor(@RequestParam("orderRequestId") Integer orderRequestId,
                                    @RequestParam("executorId") Integer executorId,
                                    @RequestParam("competence") String competence,
                                    Authentication auth,
                                    ModelMap redirectParams) {
        Integer customerId = ((UserPrincipal) auth.getPrincipal()).getUserSpecId();
        OrderRequestExecutorIdDto orderRequestExecutorId = new OrderRequestExecutorIdDto(orderRequestId, executorId);
        int placesRemain = customerService.addExecutorToOrderRequest(orderRequestExecutorId, customerId);
        if (placesRemain > 0) {
            redirectParams.addAttribute("message", String.format("Исполнитель получил ваше приглашение, отследить его согласие вы можете в разделе" +
                    " Мои заяки. Вы можете выбрать еще %d сполнителя для выполнения вашего заказа.", placesRemain));
            redirectParams.addAttribute("orderRequestId", orderRequestId);
            return new ModelAndView("redirect:/customer/executors/" + competence, redirectParams);
        }
        return new ModelAndView("redirect:/customer/orderRequests");
    }

    @GetMapping("/orders")
    public String getMyOrders(@ModelAttribute("feedback") FeedbackDTO feedback,
                              Model model){
        Integer customerId = (Integer)model.getAttribute("customerId");
        model.addAttribute("orders", customerService.getCustomerOrdersWithNoFeedback(customerId));
        return "/customer/orders";
    }

    @PostMapping("orders/sendFeedback")
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

    @PostMapping("/orderRequests/decline")
    public String declineExecutor(@RequestParam Integer orderRequestId,
                                  @RequestParam Integer executorId,
                                  Authentication auth,
                                  Model model){
        Integer customerId = ((UserPrincipal) auth.getPrincipal()).getUserSpecId();
        customerService.declineExecutor(new OrderRequestExecutorIdDto(orderRequestId, executorId), customerId);
        model.addAttribute("orderRequests", customerService.getOpenedOrderRequests(executorId));
        return "customer/orderRequests";
    }

    @PostMapping("/orderRequests/accept")
    public String acceptExecutorRequest(@ModelAttribute("orderRequestIdExecutorId") OrderRequestExecutorIdDto requestIdExecutorIdDto,
                                        Model model){
        Integer customerId = (Integer) model.getAttribute("customerId");
        customerService.acceptExecutor(requestIdExecutorIdDto,
                customerId);
        model.addAttribute("orderRequests", customerService.getOpenedOrderRequests(customerId));
        return "customer/orderRequests";
    }

    @GetMapping("/orderRequests/choose")
    public ModelAndView choosePrivateExecutors(@ModelAttribute("chooseOrderRequest") OrderRequest orderRequest,
                                         Model model, ModelMap redirectParams){
        if(customerService.privateExecutorsPlacesRemain(orderRequest.getId(),
                (Integer)model.getAttribute("customerId")) > 0){
            redirectParams.addAttribute("orderRequestId", orderRequest.getId());
            return new ModelAndView("redirect:/customer/executors/" + orderRequest.getCompetence(), redirectParams);
        }
        model.addAttribute("error", "Вы пригласили максимально допусимое число исполнителей." +
                "Если хотите заменить кого-то, сперва отзовите запрос");
        return new ModelAndView("customer/orderRequests");
    }

    @ModelAttribute
    public void addAttributesForMenu(Model model, Authentication auth){
        model.addAttribute("competences", customerService.getAllCompetences());
        UserPrincipal user = (UserPrincipal)auth.getPrincipal();
        model.addAttribute("userName", user.getPersonEntity().getFullName());
        model.addAttribute("customerId", user.getUserSpecId());
    }




//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//    }
}
