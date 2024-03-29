package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.PlayerState;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Data
public class RulesFollowedVerifier {

    public boolean isValidHand(String playerId, List<MovementCard> movementCardList, Game game) {
        return  isPlayerReadyToSubmitHand(playerId, game) &&
                isHandCorrectSize(movementCardList, game) &&
                doesHandContainNoDuplicateCards(movementCardList) &&
                playerWasDealtEveryCard(playerId, movementCardList, game);
    }

    public boolean isPlayerReadyToSubmitHand(String playerId, Game game){
        PlayerState playerState = game.getPlayerStatusManager().getPlayerState(playerId);
        if(playerState == PlayerState.CHOOSING_MOVEMENT_CARDS){
            return true;
        }
        return false;
    }

    public boolean doesHandContainNoDuplicateCards(List<MovementCard> movementCardList) {
        if(movementCardList.size() != (new HashSet(movementCardList)).size()){
            return false;
        }

        return true;
    }

    public boolean isHandCorrectSize(List<MovementCard> movementCardList, Game game){
        //todo: worry about smaller hands for when players have had too much damage dealt
        if(movementCardList.size() != game.getGameConfig().getMaxHandSize()) {
            return false;
        }

        return true;
    }

    public boolean playerWasDealtEveryCard(String playerId, List<MovementCard> movementCardList, Game game){
        Map<MovementCard, String> cardsDealt = game.getCardsDealt();
        return movementCardList.stream().noneMatch(
                card -> cardsDealt.get(card) != playerId
        );
    }

}
