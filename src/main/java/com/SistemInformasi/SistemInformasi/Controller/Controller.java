package com.SistemInformasi.SistemInformasi.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping(value = "/")
    public String index() {
        
        return "index";
    }  
}
