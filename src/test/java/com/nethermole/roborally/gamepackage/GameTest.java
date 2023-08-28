package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.exceptions.InvalidPlayerStateException;
import com.nethermole.roborally.exceptions.InvalidSubmittedHandException;
import com.nethermole.roborally.gamepackage.board.BoardFactory;
import com.nethermole.roborally.gamepackage.board.element.Checkpoint;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.PlayerState;
import com.nethermole.roborally.gamepackage.player.bot.NPCPlayer;
import com.nethermole.roborally.gamepackage.player.bot.RandomBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest {

    @Spy
    Game game;

    HumanPlayer player0;
    HumanPlayer player1;
    NPCPlayer npcPlayer2;

    Map<Integer, Player> playerList;

    @BeforeEach
    public void setup() {
        player0 = new HumanPlayer(0);
        player1 = new HumanPlayer(1);
        npcPlayer2 = new RandomBot(2);

        playerList = new HashMap<>();
        playerList.put(0, player0);
        playerList.put(1, player1);
        playerList.put(2, npcPlayer2);

        GameBuilder gameBuilder = new GameBuilder((new Random()).nextLong());
        gameBuilder.gameLog(null);
        gameBuilder.board((new BoardFactory()).board_empty());
        gameBuilder.players(1,3);
        gameBuilder.generateStartBeacon();
        gameBuilder.generateCheckpoints(1);

        game = gameBuilder.buildGame();
        game.setupForNextTurn();
    }

    @Test
    public void constructor() {
        assertThat(game.getBoard()).isNotNull();
        assertThat(game.getPlayersHands().entrySet()).isNotEmpty();
    }

    @Test
    public void distributeCards() throws InvalidPlayerStateException {
        List<MovementCard> playerHand = game.getHand(0);
        assertThat(playerHand.size()).isEqualTo(Player.STARTING_HEALTH);
    }

    @Test
    public void getHand_getsCardsForPlayer() throws InvalidPlayerStateException {
        List<MovementCard> playerHand = game.getHand(0);
        assertThat(playerHand.size()).isEqualTo(Player.STARTING_HEALTH);
        assertThat(game.getPlayerStatusManager().getPlayerState(0)).isEqualTo(PlayerState.CHOOSING_MOVEMENT_CARDS);
    }

    @Test
    public void getHand_setsPlayerState() throws InvalidPlayerStateException {
        game.getHand(0);
        assertThat(game.getPlayerStatusManager().getPlayerState(0)).isEqualTo(PlayerState.CHOOSING_MOVEMENT_CARDS);
    }

    @Test
    public void submitPlayerHand_resetsPlayerHand() throws InvalidSubmittedHandException, InvalidPlayerStateException {
        List<MovementCard> movementCardList = game.getHand(0).subList(0, 5);
        game.submitPlayerHand(0, movementCardList);

        assertThat(game.getPlayersHands().get(player0.getId())).isEmpty();
    }

    @Test
    public void submitPlayerHand_setsSubmittedHand() throws InvalidSubmittedHandException, InvalidPlayerStateException {
        List<MovementCard> movementCardList = game.getHand(0).subList(0, 5);
        game.submitPlayerHand(0, movementCardList);

        assertThat(game.getPlayerSubmittedHands().get(player0.getId())).isEqualTo(movementCardList);
    }

    @Test
    public void checkForWinner_true() {
        Player winningPlayer = game.getPlayer(0);
        winningPlayer.touchCheckpoint(new Checkpoint(1));
        game.checkForWinner();

        assertThat(game.getWinningPlayer()).isEqualTo(game.getPlayer(0));
    }

    @Test
    public void checkForWinner_false() {
        game.checkForWinner();
        assertThat(game.getWinningPlayer()).isNull();
    }

}