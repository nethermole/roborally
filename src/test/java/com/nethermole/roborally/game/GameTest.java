package com.nethermole.roborally.game;

import com.nethermole.roborally.game.board.element.ElementEnum;
import com.nethermole.roborally.game.deck.movement.Movement;
import com.nethermole.roborally.game.deck.movement.MovementCard;
import com.nethermole.roborally.game.player.HumanPlayer;
import com.nethermole.roborally.game.player.Player;
import com.nethermole.roborally.game.player.PlayerState;
import com.nethermole.roborally.logs.GameEventLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

    Game game;
    HumanPlayer player0;
    HumanPlayer player1;

    List<Player> playerList;

    @BeforeEach
    public void setup() {
        player0 = new HumanPlayer(0);
        player1 = new HumanPlayer(1);
        playerList = new ArrayList<>();
        playerList.add(player0);
        playerList.add(player1);
        game = new Game(playerList, 8, 8, new GameEventLogger());

        //gets the game into general testable state
        game.distributeCards();
        game.constructBoard("Empty");
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
    public void constructBoard_constructsBoard() {
        game.constructBoard("Empty");

        assertThat(game.getBoard().getSquares().length).isEqualTo(8);
        assertThat(game.getBoard().getSquares()[0].length).isEqualTo(8);
        assertThat(game.getBoard().getSquares()[0][0].hasElement(ElementEnum.PIT)).isTrue();

    }

}