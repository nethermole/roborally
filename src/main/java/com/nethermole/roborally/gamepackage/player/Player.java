package com.nethermole.roborally.gamepackage.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.Direction;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.board.element.Beacon;
import com.nethermole.roborally.gamepackage.board.element.Checkpoint;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.bot.NPCPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

public abstract class Player {

    //todo: move to gameconfig
    public static final int STARTING_HEALTH = 9;

    @Getter
    private String id;

    @Getter
    @Setter
    private String displayName;

    @Getter
    @Setter
    @JsonIgnore
    private int health;

    @Setter
    @Getter
    @JsonIgnore
    private Direction direction;

    @Setter
    @Getter
    @JsonIgnore
    private Position position;

    @Setter
    @Getter
    @JsonIgnore
    private Beacon beacon;

    @Getter
    @JsonIgnore
    private int mostRecentCheckpointTouched;

    @Setter
    @Getter
    private RGB color;

    public int getNextCheckpointIndex() {
        return mostRecentCheckpointTouched + 1;
    }

    private static Logger log = LogManager.getLogger(Player.class);

    public Player(String id) {
        this.id = id;
        this.color = new RGB((new Random()).nextInt(255), (new Random()).nextInt(255), (new Random()).nextInt(255));
        this.direction = Direction.UP;
        health = STARTING_HEALTH;
        mostRecentCheckpointTouched = 0;
    }

    public void touchCheckpoint(Checkpoint checkpoint) {
        if (checkpoint.getBase1index() - 1 == mostRecentCheckpointTouched) {
            mostRecentCheckpointTouched = checkpoint.getBase1index();
            log.info(getId() + " touched " + checkpoint.getElementEnum().name());
        }
    }

    public PlayerSnapshot snapshot() {
        return new PlayerSnapshot(this);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class RGB{
        int red;
        int green;
        int blue;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static Player instance() {
        return new NPCPlayer(""+Integer.MAX_VALUE) {
            @Override
            public PlayerSnapshot snapshot() {
                return null;
            }

            @Override
            public List<MovementCard> chooseCards(List<MovementCard> movementCards, Board board) {
                return null;
            }
        };
    }
}
