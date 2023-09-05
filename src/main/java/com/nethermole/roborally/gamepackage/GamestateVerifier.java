package com.nethermole.roborally.gamepackage;

public class GamestateVerifier {

    public boolean isGameReadyToProcessTurn(Game game) {
        return game.getWinningPlayer() == null &&
                game.getPlayerStatusManager().allPlayersHaveSubmittedHands();
    }

}
