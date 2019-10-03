package ee.taltech.iti0203.webstore.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * see: https://stackoverflow.com/a/54363378
 * */
@Controller
public class RouteToAngular implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return "/";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}