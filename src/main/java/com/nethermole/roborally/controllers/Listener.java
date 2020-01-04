package com.nethermole.roborally.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class Listener {

    @GetMapping("/wew")
    public void getNumber(int wew){
        System.out.println(wew);
    }

}
