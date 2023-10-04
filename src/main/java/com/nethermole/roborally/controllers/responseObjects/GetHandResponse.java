package com.nethermole.roborally.controllers.responseObjects;

import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetHandResponse {
    List<MovementCard> movementCards;
}
