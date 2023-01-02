package com.nethermole.roborally.gamepackage.board;

import com.nethermole.roborally.gamepackage.board.element.Element;
import com.nethermole.roborally.gamepackage.board.element.ElementEnum;
import com.nethermole.roborally.gamepackage.board.element.Pit;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class Tile {

    private static Tile outOfBounds;

    @Getter
    private Set<Element> elements;

    public Tile() {
        elements = new HashSet<>();
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public boolean hasElement(ElementEnum elementEnum) {
        for (Element element : elements) {
            if (element.getElementEnum() == elementEnum) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public static Tile getOutOfBounds(){
        if (outOfBounds == null) {
            Tile _outOfBounds = new Tile();
            _outOfBounds.addElement(new Pit());

            outOfBounds = _outOfBounds;
        }
        return outOfBounds;
    }
}
