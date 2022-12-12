package com.nethermole.roborally.gamepackage.deck.movement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovementCard {
    private Movement movement;
    private int priority;

    @Override
    public String toString(){
        return movement.string + "-" + priority;
    }
}
