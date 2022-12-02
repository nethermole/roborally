package com.nethermole.roborally.gamepackage.player;

import com.nethermole.roborally.gamepackage.board.Direction;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.board.element.Beacon;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.bot.NPCPlayer;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public abstract class Player {

    public static final int STARTING_HEALTH = 9;

    public int id;
    public int health;
    public String name;
    private Direction facing;

    @Getter
    private Position position;

    private Beacon beacon;

    public Player(int id) {
        this.id = id;
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

    public static Player instance(){
        return new NPCPlayer(Integer.MAX_VALUE) {
            @Override
            public List<MovementCard> chooseCards(List<MovementCard> movementCards) {
                return null;
            }
        };
    }

}
