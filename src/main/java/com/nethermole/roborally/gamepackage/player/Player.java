package com.nethermole.roborally.gamepackage.player;

import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.board.Direction;
import lombok.Data;

@Data
public abstract class Player {

    public static final int STARTING_HEALTH = 9;

    public int id;
    public int health;
    public String name;
    private Direction facing;
    private Position position;
    private Position respawnPosition;

    public Player() {
        health = STARTING_HEALTH;
    }

    public String getName() {
        return "Fred";
    }

    public boolean hasPosition() {
        return position != null;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
