package com.nethermole.roborally.game.board;

import com.nethermole.roborally.game.board.element.Element;
import com.nethermole.roborally.game.board.element.HorizontalWall;
import com.nethermole.roborally.game.board.element.VerticalWall;
import com.nethermole.roborally.game.player.HumanPlayer;
import com.nethermole.roborally.game.player.NPCPlayer;
import com.nethermole.roborally.game.player.Player;
import com.nethermole.roborally.logs.GameEventLogger;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BoardTest {

    @InjectMocks
    Board board = new Board(8,8, new Coordinate(0,0), new GameEventLogger());

    @Test
    public void board_initializesPlayersAndSquares(){
        assertThat(board.getSquares()).isNotNull();
        assertThat(board.getPlayers()).isNotNull();
    }

    @Test
    public void addElement(){
        VerticalWall verticalWall = new VerticalWall();
        board.addElement(verticalWall, new Coordinate(0,0));
        assertThat(board.getSquares()[0][0].getElements()).contains(verticalWall);
    }

    @Test
    public void addPlayer(){
        Player player = new NPCPlayer();
        board.addPlayer(player);
        assertThat(board.getPlayers()).contains(player);
    }

    @Test
    public void move1(){
        Player player = new HumanPlayer(0);
        player.setPosition(new Coordinate(5,5));
        player.setFacing(Direction.UP);

        board.move1(player);
        Coordinate coordinate = player.getPosition();
        assertThat(coordinate.getX()).isEqualTo(5);
        assertThat(coordinate.getY()).isEqualTo(4);
    }

    @Test
    public void backup(){
        Player player = new HumanPlayer(0);
        player.setPosition(new Coordinate(5,5));
        player.setFacing(Direction.UP);

        board.backup(player);

        Coordinate coordinate = player.getPosition();
        assertThat(coordinate.getX()).isEqualTo(5);
        assertThat(coordinate.getY()).isEqualTo(6);
    }

    @Test
    public void uturn(){
        Player player = new HumanPlayer(0);
        player.setPosition(new Coordinate(5,5));

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
        player.setPosition(new Coordinate(5, 5));

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
        player.setPosition(new Coordinate(5, 5));

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
