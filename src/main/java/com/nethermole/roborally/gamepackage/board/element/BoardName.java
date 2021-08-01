package com.nethermole.roborally.gamepackage.board.element;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardName extends Element {

    String name;

    @Override
    public ElementEnum getElementEnum() {
        return ElementEnum.BOARD_NAME;
    }
}
