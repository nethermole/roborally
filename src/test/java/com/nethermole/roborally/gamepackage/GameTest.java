package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.BoardFactory;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.deck.movement.Movement;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.PlayerState;
import com.nethermole.roborally.gameservice.GameLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

    Game game;
    HumanPlayer player0;
    HumanPlayer player1;

    Map<Integer, Player> playerList;
    List<Position> checkpointList;

    @BeforeEach
    public void setup() {
        player0 = new HumanPlayer(0);
        player1 = new HumanPlayer(1);
        playerList = new HashMap<>();
        playerList.put(0, player0);
        playerList.put(1, player1);
        checkpointList = new ArrayList<>();
        checkpointList.add(new Position(0,0));
        checkpointList.add(new Position(1,1));
        game = new Game(playerList, new GameLog());

        //gets the game into general testable state
        game.distributeCards();

        BoardFactory boardFactory = new BoardFactory();
        List<Board> boards = new ArrayList<>();
        boards.add(boardFactory.board_empty());
        game.setBoards(boards, checkpointList);
    }

    @Test
    public void constructor() {
        assertThat(game.getBoard()).isNotNull();
        assertThat(game.getPlayersHands().entrySet()).isNotEmpty();
    }

    @Test
    public void distributeCards() {
        List<MovementCard> playerHand = game.getHand(player0);
        assertThat(playerHand.size()).isEqualTo(Player.STARTING_HEALTH);
    }

    @Test
    public void getHand_getsCardsForPlayer() {
        List<MovementCard> playerHand = game.getHand(player0);
        assertThat(playerHand.size()).isEqualTo(Player.STARTING_HEALTH);
        assertThat(game.getPlayerStates().get(player0)).isEqualTo(PlayerState.CHOOSING_MOVEMENT_CARDS);
    }

    @Test
    public void getHand_setsPlayerState() {
        game.getHand(player0);
        assertThat(game.getPlayerStates().get(player0)).isEqualTo(PlayerState.CHOOSING_MOVEMENT_CARDS);
    }

    @Test
    public void submitPlayerHand_resetsPlayerHand() {
        List<MovementCard> movementCardList = new ArrayList<>();
        movementCardList.add(new MovementCard(Movement.MOVE1, 100));

        game.submitPlayerHand(player0, movementCardList);

        assertThat(game.getPlayersHands().get(player0)).isEmpty();
    }

    @Test
    public void submitPlayerHand_setsSubmittedHand() {
        List<MovementCard> movementCardList = new ArrayList<>();
        MovementCard movementCard = new MovementCard(Movement.MOVE1, 100);
        movementCardList.add(movementCard);

        game.submitPlayerHand(player0, movementCardList);

        assertThat(game.getPlayerSubmittedHands().get(player0).get(0)).isEqualTo(movementCard);
    }

    @Test
    public void isReadyToProcessTurn_noPlayersHaveSubmittedCards_returnsFalse() {
        assertThat(game.isReadyToProcessTurn()).isFalse();
    }

    @Test
    public void isReadyToProcessTurn_notAllPlayersHaveSubmittedCards_returnsFalse() {
        assertThat(playerList.size()).isGreaterThan(1);

        game.submitPlayerHand(player1, new ArrayList<>());
        assertThat(game.isReadyToProcessTurn()).isFalse();
    }

    @Test
    public void isReadyToProcessTurn_allPlayersHaveSubmittedCards_returnsTrue() {
        assertThat(playerList.size()).isEqualTo(2);

        game.submitPlayerHand(player0, new ArrayList<>());
        game.submitPlayerHand(player1, new ArrayList<>());
        assertThat(game.isReadyToProcessTurn()).isTrue();
    }

    @Test
    public void checkForWinner_checkPointPreset_returnsTrue(){
        game.getPlayer(0).setPosition(new Position(1, 1));
        game.checkForWinner();
        assertThat(game.getWinningPlayer()).isEqualTo(game.getPlayer(0));
    }

    @Test
    public void checkForWinner_checkpointNotPreset_returnsFalse(){
        game.getPlayer(0).setPosition(new Position(0, 0));
        game.checkForWinner();
        assertThat(game.getWinningPlayer()).isNull();
    }

    @Test
    public void positionInSquares_tooLowX_returnsFalse(){
        Position out = new Position(-1, 0);
        assertThat(game.isPositionInSquares(out)).isFalse();
    }

    @Test
    public void positionInSquares_tooLowY_returnsFalse(){
        Position out = new Position(0, -1);
        assertThat(game.isPositionInSquares(out)).isFalse();
    }

    @Test
    public void positionInSquares_inBounds_returnsTrue(){
        Position _00 = new Position(0, 0);
        Position _55 = new Position(5, 5);
        assertThat(game.isPositionInSquares(_00)).isTrue();
        assertThat(game.isPositionInSquares(_55)).isTrue();
    }

    @Test
    public void positionInSquares_tooBigX_returnsFalse(){
        Position out = new Position(20, 0);
        assertThat(game.isPositionInSquares(out)).isFalse();
    }

    @Test
    public void positionInSquares_tooBigY_returnsFalse(){
        Position out = new Position(0, 20);
        assertThat(game.isPositionInSquares(out)).isFalse();
    }

}