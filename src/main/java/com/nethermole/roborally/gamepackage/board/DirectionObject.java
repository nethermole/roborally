package com.nethermole.roborally.gamepackage.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
this is yucky, but when i need to do copy-constructors, suddenly enums don't support me
todo: make this better instead
 */
@AllArgsConstructor
public class DirectionObject {

    @Setter
    @Getter
    private Direction direction;
}
