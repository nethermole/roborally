package com.nethermole.roborally.gamepackage.deck.movement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovementCard {
    private Movement movement;
    private int priority;

    //copy constructor
    public MovementCard(MovementCard movementCard){
        movement = movementCard.getMovement();
        priority = movementCard.getPriority();
    }

    @JsonIgnore
    public boolean isTurn(){
        return Movement.isTurn(getMovement());
    }

    @Override
    public String toString(){
        return movement.string + "-" + priority;
    }
}
