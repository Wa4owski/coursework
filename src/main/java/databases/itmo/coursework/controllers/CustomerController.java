package databases.itmo.coursework.controllers;

import databases.itmo.coursework.model.OrderRequest;
import databases.itmo.coursework.model.OrderVisibility;
import databases.itmo.coursework.security.UserPrincipal;
import databases.itmo.coursework.servises.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping(path = "/newOrder")
    public String newOrderPage(@ModelAttribute("orderRequest") OrderRequest orderRequest,
                               Model model){
        List<String> competences = customerService.getAllCompetences();
        model.addAttribute("competences", competences);
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
    public String createOrder(@Valid @ModelAttribute("orderRequest") OrderRequest orderRequest,
                              BindingResult result, Authentication auth, Model model) {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (result.hasErrors()) {
            List<String> competences = customerService.getAllCompetences();
            model.addAttribute("competences", competences);
            return "customer/newOrder";
        }
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Integer customerId = userPrincipal.getUserSpecId();
        customerService.createNewOrderRequest(customerId, orderRequest);
        if(orderRequest.getAccess().equals(OrderVisibility.public_)) {
            return "redirect:/customer/orderRequests";
        }
        else {
            model.addAttribute("choose", true);
            return "customer/executors/" + "{" + orderRequest.getCompetence() + "}";
        }
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
