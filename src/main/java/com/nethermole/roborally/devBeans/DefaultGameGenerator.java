package com.nethermole.roborally.devBeans;

import com.nethermole.roborally.controllers.GameController;
import com.nethermole.roborally.gamepackage.GameConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class DefaultGameGenerator {

    @Autowired
    GameController gameController;

    @PostConstruct
    public void createDefaultBotGame() {
        gameController.createGameWithId("admin", GameConfig.standardBot4Player);
    }
}
