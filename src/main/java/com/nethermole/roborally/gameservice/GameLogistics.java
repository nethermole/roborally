package com.nethermole.roborally.gameservice;

import com.nethermole.roborally.StartInfo;
import com.nethermole.roborally.exceptions.GameNotStartedException;
import com.nethermole.roborally.gamepackage.Game;
import com.nethermole.roborally.gamepackage.ViewStep;
import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.BoardFactory;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.view.AbstractView;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class GameLogistics {

    @Getter
    private Game game;

    @Getter
    private Map<Integer, Player> players;
    private List<AbstractView> viewers;

    @Autowired
    GameLog gameLog;

    public boolean isGameAlreadyStarted() {
        return (game != null);
    }

    public boolean isGameAboutToStart(){
        return isGameAlreadyStarted() && game.getCurrentTurn() == 0;
    }

    //todo extract clientUpdate logic
    public void startGame(Map<Integer, Player> players) {
        this.players = players;
        Random random = new Random();

        BoardFactory boardFactory = new BoardFactory();
        Board board = boardFactory.board_exchange();

        Position startPosition = new Position(random.nextInt(board.getSquares().length), random.nextInt(board.getSquares().length));
        Position endPosition = new Position(random.nextInt(board.getSquares().length), random.nextInt(board.getSquares().length));
        while(startPosition.equals(endPosition)){
            endPosition = new Position(random.nextInt(board.getSquares().length), random.nextInt(board.getSquares().length));
        }
        List<Position> checkpointPositions = new ArrayList<>();
        checkpointPositions.add(startPosition);
        checkpointPositions.add(endPosition);

        game = new Game(players, gameLog);
        ArrayList<Board> boards = new ArrayList<>();
        boards.add(board);
        game.setBoards(boards, checkpointPositions);
        game.distributeCards();
        game.npcPlayersSelectCards();

        //ProofOfConceptView pocView = ProofOfConceptView.startPOCView(this);
        viewers = new ArrayList<>();
        //viewers.add(pocView);
    }

    public void stopGame() {
        for (AbstractView view : viewers) {
            view.stopViewing();
        }
        game = null;
    }

    public Board getBoard() throws GameNotStartedException {
        if (game == null) {
            throw new GameNotStartedException();
        }

        return game.getBoard();
    }

    public List<ViewStep> getViewstepsByTurn(int turn) {
        if (turn >= game.getCurrentTurn()) {
            return new ArrayList<>();
        }
        return gameLog.getViewstepsByTurn(turn);
    }

    public List<MovementCard> getHand(int playerId) {
        if (game == null) {
            return null;
        }
        Player player = players.get(playerId);
        return game.getHand(player);
    }

    public void submitHand(int playerId, List<MovementCard> movementCardList) {
        if (game == null) {
            throw new UnsupportedOperationException("game not started yet. cant submit hand");
        }

        Player player = players.get(playerId);
        game.submitPlayerHand(player, movementCardList);

        if (game.isReadyToProcessTurn()) {
            game.processTurn();
        }
    }

    public StartInfo getStartInfo(){
        return new StartInfo(players.size(), game.getStartPosition());
    }
}
