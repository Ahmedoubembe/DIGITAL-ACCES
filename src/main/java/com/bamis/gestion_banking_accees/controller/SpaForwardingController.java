package com.bamis.gestion_banking_accees.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaForwardingController {

    //    @RequestMapping(value = "/{path:^(?!api|static|.*\\..*).*}/**")
    @RequestMapping("/TEST")
    public String forwar01() {
        // Always forward to index.html for Angular routes
        return "test";
    }
    @RequestMapping("/services/**")
    public String forward1() {
        // Always forward to index.html for Angular routes
        return "forward:/index.html";
    }
    @RequestMapping("/")
    public String forward() {
        // Always forward to index.html for Angular routes
        return "forward:/index.html";
    }
}

