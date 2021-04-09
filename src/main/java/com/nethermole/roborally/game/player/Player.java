package com.nethermole.roborally.game.player;

import com.nethermole.roborally.game.board.Coordinate;
import com.nethermole.roborally.game.board.Direction;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public abstract class Player {

    public int id;
    public int health;
    public String name;
    private Direction facing;
    private Coordinate position;

    public Player(){
        health = 9;
    }

    public String getName(){
        return "Fred";
    }

    public abstract void giveCards();

    public abstract void getCards();

    public boolean hasPosition(){
        return position != null;
    }

    @Override
    public int hashCode(){
        return id;
    }

}
