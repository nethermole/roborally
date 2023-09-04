package com.nethermole.roborally.controllers.requestObjects;

import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class APIrequestPlayerSubmitHand {
    List<MovementCard> movementCards;
}
