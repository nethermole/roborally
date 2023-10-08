package com.nethermole.roborally.controllers;

import com.nethermole.roborally.controllers.requestObjects.PlayerSubmitHandRequest;
import com.nethermole.roborally.controllers.responseObjects.GetHandResponse;
import com.nethermole.roborally.controllers.responseObjects.PlayerJoinResponse;
import com.nethermole.roborally.exceptions.InvalidPlayerStateException;
import com.nethermole.roborally.exceptions.ThisShouldntHappenException;
import com.nethermole.roborally.gamepackage.GameLogistics;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gameservice.GamePoolService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayerController {

    @Autowired
    GamePoolService gamePoolService;

    private static Logger log = LogManager.getLogger(PlayerController.class);


    @PostMapping("/game/{gameId}/join")
    public PlayerJoinResponse joinGame(@PathVariable("gameId") String gameId){
        String connectedPlayerId = gamePoolService.joinHumanPlayer(gameId);
        log.info("Human player joined game " + gameId + " as player " + connectedPlayerId);
        return new PlayerJoinResponse(connectedPlayerId);
    }

    @PostMapping("/game/{gameId}/player/{playerId}/submitHand")
    public String submitHand(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId, @RequestBody PlayerSubmitHandRequest playerSubmitHandRequest) {
        try {
            GameLogistics gameLogistics = gamePoolService.getGameLogistics("" + gameId);
            gameLogistics.submitHand(playerId, playerSubmitHandRequest.getMovementCards());
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }

    @GetMapping("/game/{gameId}/player/{playerId}/getHand")
    public GetHandResponse getHand(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId) throws InvalidPlayerStateException, ThisShouldntHappenException {
        GameLogistics gameLogistics = gamePoolService.getGameLogistics("" + gameId);
        List<MovementCard> movementCards = gameLogistics.getHand(playerId);
        return new GetHandResponse(movementCards);
    }
}
