package com.nethermole.roborally.game.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

     Coordinate coordinate;

     @Test
     public void moveForward1(){
         coordinate = new Coordinate(5,5);

         Coordinate up = coordinate.moveForward1(Direction.UP);
         Coordinate right = coordinate.moveForward1(Direction.RIGHT);
         Coordinate down = coordinate.moveForward1(Direction.DOWN);
         Coordinate left = coordinate.moveForward1(Direction.LEFT);

         assertThat(up.getX()).isEqualTo(5);
         assertThat(up.getY()).isEqualTo(4);

         assertThat(right.getX()).isEqualTo(6);
         assertThat(right.getY()).isEqualTo(5);

         assertThat(down.getX()).isEqualTo(5);
         assertThat(down.getY()).isEqualTo(6);

         assertThat(left.getX()).isEqualTo(4);
         assertThat(left.getY()).isEqualTo(5);
     }

    @Test
    public void moveBackward1(){
        coordinate = new Coordinate(5,5);

        Coordinate up = coordinate.moveBackward1(Direction.UP);
        Coordinate right = coordinate.moveBackward1(Direction.RIGHT);
        Coordinate down = coordinate.moveBackward1(Direction.DOWN);
        Coordinate left = coordinate.moveBackward1(Direction.LEFT);

        assertThat(up.getX()).isEqualTo(5);
        assertThat(up.getY()).isEqualTo(6);

        assertThat(right.getX()).isEqualTo(4);
        assertThat(right.getY()).isEqualTo(5);

        assertThat(down.getX()).isEqualTo(5);
        assertThat(down.getY()).isEqualTo(4);

        assertThat(left.getX()).isEqualTo(6);
        assertThat(left.getY()).isEqualTo(5);
    }

}