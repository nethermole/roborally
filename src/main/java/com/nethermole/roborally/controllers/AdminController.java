package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gameservice.GamePoolService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private static Logger log = LogManager.getLogger(AdminController.class);

    @Autowired
    GamePoolService gamePoolService;

    @GetMapping("/admin/util/getFirstGameUUID")
    public String getFirstGameUUID() {
        return gamePoolService.adminGetFirstGameUUID();
    }

}