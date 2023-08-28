package com.nethermole.roborally.gameservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GameReport {

    String gameUUID;
    List<String> playerIDs;
    Map<String, String> playerNames;
    List<TurnReport> turns;

    public GameReport() {
        List<TurnReport> turns;
    }

    public class TurnReport {
        List<MovementCard> cards;
        Map<MovementCard, String> playersByCard;
        List<Play> plays;
    }

    public abstract class Play {
        String type;
    }

    public class CardPlay extends Play {
        String cardName;
        int priority;

        List<BoardAction> boardActions;
    }

    public abstract class BoardAction {
        String type;
    }

    public class MoveRobot extends BoardAction {
        String playerId;
        Position startPosition;
        Position endPosition;

        PushRobot push;
    }

    public class PushRobot {
        String playerId;
        Position startPosition;
        Position endPosition;
    }

    public void dumpToFile() {
        try {
            (new ObjectMapper()).writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
