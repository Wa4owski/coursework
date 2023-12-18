package databases.itmo.coursework.controllers;

import org.hibernate.sql.exec.ExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriUtils;

@Controller
public abstract class BaseController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ExecutionException.class, IllegalArgumentException.class})
    protected String handleValidationExceptions(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", HttpStatus.BAD_REQUEST);
        return "/error";
    }

    @ModelAttribute
    protected abstract void addAttributesForMenu(Model model, Authentication auth);

    protected final String encodePath(String path) {
        return UriUtils.encodePath(path, "UTF-8");
    }
}
