package com.nethermole.roborally.gamepackage.board;

import com.nethermole.roborally.gamepackage.board.element.Beacon;
import com.nethermole.roborally.gamepackage.board.element.Pit;
import com.nethermole.roborally.gamepackage.board.element.Wall;
import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.bot.RandomBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BoardTest {

    Board board;

    @BeforeEach
    public void setup() {
        board = new Board(8, 8);
    }

    @Test
    public void board_initializesPlayersAndSquares() {
        assertThat(board.getTileAtPosition(new Position(1, 1))).isNotNull();
        assertThat(board.getPlayers()).isNotNull();
    }

    @Test
    public void addElement() {
        Wall verticalWallLeft = new Wall(Direction.LEFT);
        board.addElement(verticalWallLeft, new Position(0, 0));
        assertThat(board.getTileAtPosition(new Position(0, 0)).getElements()).contains(verticalWallLeft);
    }

    @Test
    public void addPlayer() {
        Player player = new RandomBot("0");
        board.addPlayer(player);
        assertThat(board.getPlayers()).contains(player);
    }

    @Test
    public void move1() {
        Player player = new HumanPlayer("0");
        player.setPosition(new Position(5, 5));
        player.setDirection(Direction.UP);

        board.moveForward(player, 1);
        Position position = player.getPosition();
        assertThat(position.getX()).isEqualTo(5);
        assertThat(position.getY()).isEqualTo(6);
    }

    @Test
    public void backup() {
        Player player = new HumanPlayer("0");
        player.setPosition(new Position(5, 5));
        player.setDirection(Direction.UP);

        board.backup(player);

        Position position = player.getPosition();
        assertThat(position.getX()).isEqualTo(5);
        assertThat(position.getY()).isEqualTo(4);
    }

    @Test
    public void uturn() {
        Player player = new HumanPlayer("0");
        player.setPosition(new Position(5, 5));

        player.setDirection(Direction.UP);
        board.uturn(player);
        Direction direction = player.getDirection();
        assertThat(direction).isEqualTo(Direction.DOWN);

        player.setDirection(Direction.RIGHT);
        board.uturn(player);
        direction = player.getDirection();
        assertThat(direction).isEqualTo(Direction.LEFT);

        player.setDirection(Direction.DOWN);
        board.uturn(player);
        direction = player.getDirection();
        assertThat(direction).isEqualTo(Direction.UP);

        player.setDirection(Direction.LEFT);
        board.uturn(player);
        direction = player.getDirection();
        assertThat(direction).isEqualTo(Direction.RIGHT);
    }

    @Test
    public void resetPlayer_movesPlayerToBeaconPosition() {
        Player player = new RandomBot("0");
        board.addPlayer(player);
        player.setPosition(new Position(0, 0));

        Beacon respawnBeacon = new Beacon(-1);
        board.addElement(respawnBeacon, new Position(4, 4));
        player.setBeacon(respawnBeacon);

        board.pitKillPlayer(player, new Position(0,0));
        assertThat(player.getPosition()).isEqualTo(new Position(4, 4));
    }

    @Test
    public void turnLeft() {
        Player player = new HumanPlayer("0");
        player.setPosition(new Position(5, 5));

        player.setDirection(Direction.UP);

        board.turnLeft(player);
        assertThat(player.getDirection()).isEqualTo(Direction.LEFT);

        board.turnLeft(player);
        assertThat(player.getDirection()).isEqualTo(Direction.DOWN);

        board.turnLeft(player);
        assertThat(player.getDirection()).isEqualTo(Direction.RIGHT);

        board.turnLeft(player);
        assertThat(player.getDirection()).isEqualTo(Direction.UP);
    }

    @Test
    public void turnRight() {
        Player player = new HumanPlayer("0");
        player.setPosition(new Position(5, 5));

        player.setDirection(Direction.UP);

        board.turnRight(player);
        assertThat(player.getDirection()).isEqualTo(Direction.RIGHT);

        board.turnRight(player);
        assertThat(player.getDirection()).isEqualTo(Direction.DOWN);

        board.turnRight(player);
        assertThat(player.getDirection()).isEqualTo(Direction.LEFT);

        board.turnRight(player);
        assertThat(player.getDirection()).isEqualTo(Direction.UP);
    }

    @Test
    public void isOverPit_notInSquares_returnsTrue() {
        assertThat(board.isPit(new Position(-1, 5))).isTrue();
    }

    @Test
    public void isOverPit_overPit_returnsTrue() {
        board.addElement(new Pit(), new Position(5, 5));
        assertThat(board.isPit(new Position(5, 5))).isTrue();
    }

    @Test
    public void isOverPit_notOverPut_returnFalse() {
        assertThat(board.isPit(new Position(5, 5))).isFalse();
    }

}
