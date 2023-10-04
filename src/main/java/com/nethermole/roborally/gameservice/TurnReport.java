package com.nethermole.roborally.gameservice;

import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.play.Play;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TurnReport {
    List<MovementCard> cards = new ArrayList<>();
    Map<MovementCard, String> playersByCard = new HashMap<>();
    List<Play> plays = new ArrayList<>();
}
