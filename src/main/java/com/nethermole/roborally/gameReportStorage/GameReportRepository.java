package com.nethermole.roborally.gameReportStorage;

import com.nethermole.roborally.gameservice.GameReport;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class GameReportRepository {
    private Map<String, GameReport> gameReports;

    @PostConstruct
    public void setup(){
        gameReports = new HashMap<>();
    }

    public void saveGameReport(GameReport gameReport){
        String gameId = gameReport.getGameUUID();
        gameReports.put(gameId, gameReport);
    }

    public GameReport getGameReport(String gameId){
        return gameReports.get(gameId);
    }
}
