package com.nethermole.roborally.game.player;

import com.nethermole.roborally.game.board.Coordinate;
import com.nethermole.roborally.game.board.Direction;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public abstract class Player {

    public static final int STARTING_HEALTH = 9;

    public int id;
    public int health;
    public String name;
    private Direction facing;
    private Coordinate position;

    public Player(){
        health = STARTING_HEALTH;
    }

    public String getName(){
        return "Fred";
    }

    public boolean hasPosition(){
        return position != null;
    }

    @Override
    public int hashCode(){
        return id;
    }

}
