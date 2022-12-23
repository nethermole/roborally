package com.nethermole.roborally.gamepackage.player;

import com.nethermole.roborally.gamepackage.board.Direction;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.board.element.Beacon;
import com.nethermole.roborally.gamepackage.board.element.Checkpoint;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.bot.NPCPlayer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public abstract class Player {

    public static final int STARTING_HEALTH = 9;

    @Getter
    private int id;

    @Getter
    @Setter
    private int health;

    @Getter
    @Setter
    private String name;

    private Direction facing;

    @Getter
    private Position position;

    private Beacon beacon;

    private int mostRecentCheckpointTouched;

    public Player(int id) {
        this.id = id;
        health = STARTING_HEALTH;
        mostRecentCheckpointTouched = 0;
    }

    public void touchCheckpoint(Checkpoint checkpoint){
        if(checkpoint.getIndex() - 1 == mostRecentCheckpointTouched){
            mostRecentCheckpointTouched = checkpoint.getIndex();
        }
    }

    public PlayerSnapshot snapshot(){
        return new PlayerSnapshot(this);
    }

    @Override
    public int hashCode() {
        return id;
    }

    public static Player instance(){
        return new NPCPlayer(Integer.MAX_VALUE) {
            @Override
            public PlayerSnapshot snapshot() {
                return null;
            }

            @Override
            public List<MovementCard> chooseCards(List<MovementCard> movementCards) {
                return null;
            }
        };
    }





}
