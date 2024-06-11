package com.nethermole.roborally.gamepackage.board;

import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MovementCalculatorTest {
    Board board;

    @BeforeEach
    public void setup() {
        board = new Board(8, 8);
    }

    @Test
    public void move1_noShove_moves1() {
        Player player = new HumanPlayer("0");
        player.setPosition(new Position(5, 5));
        player.setDirection(Direction.UP);

        board.moveForward(player, 1);
        Position position = player.getPosition();
        assertThat(position.getX()).isEqualTo(5);
        assertThat(position.getY()).isEqualTo(6);
    }

//    @Test
//    public void move1_shovesWallOnCurrentTile_doesNotMove() {
//
//    }

//    @Test
//    public void move1_shovesWallOnAdjacentTile_doesNotMove() {
//    }
//
//    @Test
//    public void move1_shoves1Robot_movesAndShovesRobot(){
//        Player player0 = new HumanPlayer(0);
//        player0.setPosition(new Position(5, 5));
//        player0.setFacing(Direction.UP);
//
//        Player player1 = new HumanPlayer(1);
//        player1.setPosition(new Position(5,6));
//
//        board.moveForward(player0, 1);
//
//        assertThat(player0.getPosition().getX()).isEqualTo(5);
//        assertThat(player0.getPosition().getY()).isEqualTo(6);
//
//        assertThat(player1.getPosition().getX()).isEqualTo(5);
//        assertThat(player1.getPosition().getY()).isEqualTo(7);
//    }

//    @Test
//    public void move1_shovesRobotIntoWall_doesNotMove(){
//    }
//
//    @Test
//    public void move1_shovesRobotIntoRobot_doesNotMove(){
//        Player player0 = new HumanPlayer(0);
//        player0.setPosition(new Position(5, 5));
//        player0.setFacing(Direction.UP);
//
//        Player player1 = new HumanPlayer(1);
//        player1.setPosition(new Position(5,6));
//
//        Player player2 = new HumanPlayer(1);
//        player2.setPosition(new Position(5,7));
//
//        board.moveForward(player0, 1);
//
//        assertThat(player0.getPosition().getX()).isEqualTo(5);
//        assertThat(player0.getPosition().getY()).isEqualTo(5);
//
//        assertThat(player1.getPosition().getX()).isEqualTo(5);
//        assertThat(player1.getPosition().getY()).isEqualTo(6);
//
//        assertThat(player2.getPosition().getX()).isEqualTo(5);
//        assertThat(player2.getPosition().getY()).isEqualTo(7);
//    }

//    @Test
//    public void move1_robotEndsOnPit_killsRobot(){
//    }
//
//    @Test
//    public void move1_robotPassesOverPit_killsRobot(){
//    }
//
//    @Test
//    public void robotGetShovedOnPit_killsRobot(){
//    }
//
//    @Test
//    public void robotGetShovedOverPit_killsRobot(){
//    }
//
//    @Test
//    public void robotGetsShovedOverEdge_killsRobot(){
//    }
//
//    @Test
//    public void robotMovesOverEdge_killsRobot(){
//    }


}