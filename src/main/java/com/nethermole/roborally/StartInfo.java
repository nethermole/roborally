package com.nethermole.roborally;

import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StartInfo {

    List<Player> players;
    Position startPosition;

}
