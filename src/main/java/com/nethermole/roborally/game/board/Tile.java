package com.nethermole.roborally.game.board;

import com.nethermole.roborally.game.board.element.Element;
import com.nethermole.roborally.game.board.element.ElementEnum;
import com.nethermole.roborally.game.board.element.VerticalWall;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class Tile {

    @Getter
    private Set<Element> elements;

    public Tile(){
        elements = new HashSet<>();
    }

    public void addElement(Element element){
        elements.add(element);
    }

    public boolean hasElement(ElementEnum elementEnum){
        for(Element element : elements){
            if(element.getElementEnum() == elementEnum){
                return true;
            }
        }
        return false;
    }
}
