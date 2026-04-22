package com.voluntrack.volunteerplatform.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler({
            DuplicateRegistrationException.class,
            EventRegistrationException.class,
            IllegalArgumentException.class
    })
    public String handleBadRequestExceptions(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/500";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Something went wrong. Please try again later.");
        return "error/500";
    }
}