package com.nethermole.roborally.game;

import com.nethermole.roborally.game.Coordinate;
import com.nethermole.roborally.game.Direction;
import lombok.Data;

@Data
public class Robot {

    public Direction facing;
    public Coordinate location;

}
