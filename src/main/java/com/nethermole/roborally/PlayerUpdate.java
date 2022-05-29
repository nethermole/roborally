package com.nethermole.roborally;

import lombok.Data;

@Data
public class PlayerUpdate {

    String value;

    public PlayerUpdate(String value) {
        this.value = value;
    }
}
