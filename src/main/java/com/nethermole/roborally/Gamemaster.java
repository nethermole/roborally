package com.nethermole.roborally;

import com.nethermole.roborally.exceptions.GameNotStartedException;
import com.nethermole.roborally.game.*;
import com.nethermole.roborally.game.board.Board;
import com.nethermole.roborally.game.board.Coordinate;
import com.nethermole.roborally.game.board.element.Element;
import com.nethermole.roborally.game.deck.movement.MovementCard;
import com.nethermole.roborally.game.player.Player;
import com.nethermole.roborally.game.turn.Event;
import com.nethermole.roborally.logs.GameEventLogger;
import com.nethermole.roborally.view.AbstractView;
import com.nethermole.roborally.view.POCView;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class Gamemaster {

    @Getter
    private Game game;

    @Getter
    private List<Player> players;
    private List<AbstractView> viewers;
    private GameEventLogger gameEventLogger;

    public void startGame(HashMap<Element, Coordinate> elements, List<Player> players){
        this.players = players;
        this.gameEventLogger = new GameEventLogger();
        game = new Game(elements, players, 8,8, gameEventLogger);
        POCView pocView = POCView.startPOCView(this);
        viewers = new ArrayList<>();
        viewers.add(pocView);
    }

    public void stopGame(){
        for(AbstractView view: viewers){
            view.stopViewing();
        }
        game = null;
    }

    public Board getBoard() throws GameNotStartedException {
        if(game == null){
            throw new GameNotStartedException();
        }

        return game.getBoard();
    }

    public List<MovementCard> getHand(int playerId){
        if(game == null){
            return null;
        }
        Player player = players.get(playerId);
        return game.getHand(player);
    }

    public void submitHand(int playerId, List<MovementCard> movementCardList){
        if(game == null){
            throw new UnsupportedOperationException("game not started yet. cant submit hand");
        }
        Player player = players.get(playerId);
        game.submitPlayerHand(player, movementCardList);
        if(game.isReadyToProcessTurn()){
            startProcessingTurn();
        }
    }

    public void startProcessingTurn(){
        game.processTurn();
    }
}
