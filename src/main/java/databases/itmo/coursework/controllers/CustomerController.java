package databases.itmo.coursework.controllers;

import databases.itmo.coursework.model.AddExecutorRequest;
import databases.itmo.coursework.model.Executor;
import databases.itmo.coursework.model.OrderRequest;
import databases.itmo.coursework.model.OrderVisibility;
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
    public String newPerson(Model model, Authentication auth) {
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Integer customerId = userPrincipal.getUserSpecId();
        List<OrderRequest> orderRequests = customerService.getOrderRequests(customerId);
        model.addAttribute("orderRequests", orderRequests);
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
                                    ModelMap redirectParams) {
        AddExecutorRequest addExecutorRequest = new AddExecutorRequest(orderRequestId, executorId);
        int placesRemain = customerService.addExecutorToOrderRequest(addExecutorRequest);
        if (placesRemain > 0) {
            redirectParams.addAttribute("message", String.format("Исполнитель получил ваше приглашение, отследить его согласие вы можете в разделе" +
                    " Мои заяки. Вы можете выбрать еще %d сполнителя для выполнения вашего заказа.", placesRemain));
            redirectParams.addAttribute("orderRequestId", orderRequestId);
            return new ModelAndView("redirect:/customer/executors/" + competence, redirectParams);
        }
        return new ModelAndView("redirect:/customer/orderRequests");
    }

    @ModelAttribute("competences")
    public List<String> competences(){
        return customerService.getAllCompetences();
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
