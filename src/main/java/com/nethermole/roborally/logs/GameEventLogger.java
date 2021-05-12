package com.nethermole.roborally.logs;

import com.nethermole.roborally.game.turn.Event;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameEventLogger {

    private List<Event> eventList;

    public GameEventLogger() {
        eventList = new ArrayList<>();
    }

    public void log(Event event) {
        eventList.add(event);
        System.out.println(event.toString());
    }

}
