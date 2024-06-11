package com.nethermole.roborally.controllers;

import com.nethermole.roborally.controllers.responseObjects.CreateGameResponse;
import com.nethermole.roborally.gameReportStorage.GameReportRepository;
import com.nethermole.roborally.gamepackage.GameConfig;
import com.nethermole.roborally.gameservice.GamePoolService;
import com.nethermole.roborally.gameservice.GameReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    private static Logger log = LogManager.getLogger(GameController.class);

    @Autowired
    GamePoolService gamePoolService;

    @Autowired
    GameReportRepository gameReportRepository;

    public void createGameWithId(String id, GameConfig gameconfig) {
        gamePoolService.createGameWithId(id, gameconfig);
    }

    //todo: wrap ALL api responses in objects
    @PostMapping("/game/create")
    public CreateGameResponse create(@RequestBody GameConfig gameConfig) {
        String gameId = gamePoolService.createGame(gameConfig);

        return new CreateGameResponse(gameId);
    }

    @PostMapping("/game/{gameId}/start")
    public void start(@PathVariable("gameId") String gameId) {
        gamePoolService.getGameLogistics(gameId).startGame();
    }

    @GetMapping("/game/{gameId}/report")
    public GameReport getGameReport(@PathVariable("gameId") String gameId) {
        GameReport gameReport = gameReportRepository.getGameReport(gameId);
        if(gameReport != null){
            return gameReport;
        } else {
            throw new RuntimeException("make this a 400 or whatever is appropriate");
        }

    }

    @Deprecated
    @PostMapping("/botcivstart")
    public String botCivStart(@RequestParam(required = false) Long seedIn) {
        /*int numplayers = 80;

        long seed = seedIn != null ? seedIn : (new Random()).nextLong();
        log.info("startGame(" + seed + ") called");

        Map<Integer, Player> players = new HashMap<>();
        for(int i = 0; i < 80; i++){
            players.put(i, new MinimalTurnBot(i));
        }
        players.put(numplayers, new TurnRateLimiterBot(numplayers));
        GameLogistics gameLogistics = new GameLogistics(players);
        gamePoolService.addGameLogisticsToPool(gameLogistics);

        BoardFactory boardFactory = new BoardFactory();
        Board board = boardFactory.board_empty();
        for(int x = 0; x < 1600/16; x++){
            for(int y = 0; y < 1200/16; y++){
                if(x == 0 && y == 0){
                    break;
                }
                board.addBoard(boardFactory.board_exchange(), x, y);
            }
        }

        gameLogistics.startGame(seed, board);

        try {
            BasicView basicView = new BasicView(5);
            basicView.setBoard(gameLogistics.getBoard());
            for (Player player : players.values()) {
                basicView.getWindow().getCanvas().addPlayer(player);
            }
            Thread thread = new Thread(basicView);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (gameLogistics.getGamestateVerifier().isGameReadyToProcessTurn(gameLogistics.getGame())) {
            gameLogistics.getGame().processTurn();
            gameLogistics.getGame().setupForNextTurn();
        }

        return gameLogistics.getUuid();*/
        return "";
    }


}