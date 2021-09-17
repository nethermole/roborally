package com.nethermole.roborally.gamepackage.board;

import com.nethermole.roborally.gamepackage.board.element.Element;
import com.nethermole.roborally.gamepackage.board.element.ElementEnum;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class Tile {

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
}
