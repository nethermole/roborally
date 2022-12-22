package com.nethermole.roborally.gamepackage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class GameTracker {
    Map<Long, GameLogistics> gamesById;

    private static Logger log;

    @PostConstruct
    public void init() {
        log = LogManager.getLogger(GameTracker.class);

        gamesById = new HashMap<>();
    }
}
