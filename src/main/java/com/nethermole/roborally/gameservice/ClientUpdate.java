package com.nethermole.roborally.gameservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdate {
    boolean anyActionRequired;
    String descriptionOfAction;
}
