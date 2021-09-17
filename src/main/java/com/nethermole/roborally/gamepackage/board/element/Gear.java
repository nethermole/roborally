package com.nethermole.roborally.gamepackage.board.element;

import com.nethermole.roborally.gamepackage.board.Spin;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Gear extends Element {

    Spin spin;

    @Override
    public ElementEnum getElementEnum() {

        if (spin == Spin.CLOCKWISE) {
            return ElementEnum.GEAR_C;
        } else if (spin == Spin.COUNTERCLOCKWISE) {
            return ElementEnum.GEAR_CC;
        } else {
            return null;
        }
    }

}
