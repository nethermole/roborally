package com.nethermole.roborally.game;

import com.nethermole.roborally.game.Element;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Tile {

    @Getter
    private List<Element> elements;

    public Tile(){
        elements = new ArrayList<>();
    }

    public void addElement(Element element){
        elements.add(element);
    }

}
