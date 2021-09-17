package com.nethermole.roborally;

import com.nethermole.roborally.gamepackage.board.Position;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StartInfo {

    int playerCount;
    Position startPosition;

}
