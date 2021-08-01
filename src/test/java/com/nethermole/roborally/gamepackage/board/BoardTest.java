package com.nethermole.roborally.gamepackage.board;

import com.nethermole.roborally.gamepackage.board.element.Wall;
import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.NPCPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BoardTest {

    @InjectMocks
    Board board = new Board(8, 8);

    @Test
    public void board_initializesPlayersAndSquares() {
        assertThat(board.getSquares()).isNotNull();
        assertThat(board.getPlayers()).isNotNull();
    }

    @Test
    public void addElement() {
        Wall verticalWallLeft = new Wall(Direction.LEFT);
        board.addElement(verticalWallLeft, new Position(0, 0));
        assertThat(board.getSquares()[0][0].getElements()).contains(verticalWallLeft);
    }

    @Test
    public void addPlayer() {
        Player player = new NPCPlayer();
        board.addPlayer(player);
        assertThat(board.getPlayers()).contains(player);
    }

    @Test
    public void move1() {
        Player player = new HumanPlayer(0);
        player.setPosition(new Position(5, 5));
        player.setFacing(Direction.UP);

        board.move1(player);
        Position position = player.getPosition();
        assertThat(position.getX()).isEqualTo(5);
        assertThat(position.getY()).isEqualTo(4);
    }

    @Test
    public void backup() {
        Player player = new HumanPlayer(0);
        player.setPosition(new Position(5, 5));
        player.setFacing(Direction.UP);

        board.backup(player);

        Position position = player.getPosition();
        assertThat(position.getX()).isEqualTo(5);
        assertThat(position.getY()).isEqualTo(6);
    }

    @Test
    public void uturn() {
        Player player = new HumanPlayer(0);
        player.setPosition(new Position(5, 5));

        player.setFacing(Direction.UP);
        board.uturn(player);
        Direction direction = player.getFacing();
        assertThat(direction).isEqualTo(Direction.DOWN);

        player.setFacing(Direction.RIGHT);
        board.uturn(player);
        direction = player.getFacing();
        assertThat(direction).isEqualTo(Direction.LEFT);

        player.setFacing(Direction.DOWN);
        board.uturn(player);
        direction = player.getFacing();
        assertThat(direction).isEqualTo(Direction.UP);

        player.setFacing(Direction.LEFT);
        board.uturn(player);
        direction = player.getFacing();
        assertThat(direction).isEqualTo(Direction.RIGHT);
    }

    @Test
    public void turnLeft() {
        Player player = new HumanPlayer(0);
        player.setPosition(new Position(5, 5));

        player.setFacing(Direction.UP);

        board.turnLeft(player);
        assertThat(player.getFacing()).isEqualTo(Direction.LEFT);

        board.turnLeft(player);
        assertThat(player.getFacing()).isEqualTo(Direction.DOWN);

        board.turnLeft(player);
        assertThat(player.getFacing()).isEqualTo(Direction.RIGHT);

        board.turnLeft(player);
        assertThat(player.getFacing()).isEqualTo(Direction.UP);
    }

    @Test
    public void turnRight() {
        Player player = new HumanPlayer(0);
        player.setPosition(new Position(5, 5));

        player.setFacing(Direction.UP);

        board.turnRight(player);
        assertThat(player.getFacing()).isEqualTo(Direction.RIGHT);

        board.turnRight(player);
        assertThat(player.getFacing()).isEqualTo(Direction.DOWN);

        board.turnRight(player);
        assertThat(player.getFacing()).isEqualTo(Direction.LEFT);

        board.turnRight(player);
        assertThat(player.getFacing()).isEqualTo(Direction.UP);
    }

}
