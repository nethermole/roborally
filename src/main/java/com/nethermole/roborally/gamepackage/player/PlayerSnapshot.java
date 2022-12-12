package com.nethermole.roborally.gamepackage.player;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PlayerSnapshot extends Player {

    public PlayerSnapshot(Player reference) {
        super(reference.getId());
        this.setHealth(reference.getHealth());
        this.setBeacon(reference.getBeacon());
        this.setFacing(reference.getFacing());
        this.setPosition(reference.getPosition());
        this.setName(reference.getName());
    }

    @Override
    public PlayerSnapshot snapshot() {
        throw new NotImplementedException();
    }
}
