package com.coderscampus.assignment13.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    public String handleNotFound (NoHandlerFoundException ex, Model model) {
        model.addAttribute ("error","Page not Found");
        model.addAttribute ("message", ex.getMessage());
        return "error/404";
    }
    public String handleGenericException (Exception ex, Model model) {
        model.addAttribute ("error","An Error Occurred");
        model.addAttribute ("message", ex.getMessage());
        return "error/generic";
    }





}
