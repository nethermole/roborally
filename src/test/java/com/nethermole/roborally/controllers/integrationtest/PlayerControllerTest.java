package com.nethermole.roborally.controllers.integrationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethermole.roborally.controllers.requestObjects.APIrequestPlayerSubmitHand;
import com.nethermole.roborally.controllers.responseObjects.APIresponsePlayerGetHand;
import com.nethermole.roborally.gamepackage.GameConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerControllerTest {

    @LocalServerPort
    private int port;

    private HttpHeaders httpHeaders;
    private TestRestTemplate restTemplate;
    private String baseUrl;

    @BeforeEach
    public void setup(){
        httpHeaders = new HttpHeaders();
        restTemplate = new TestRestTemplate();

        baseUrl = "http://localhost:" + port;

        httpHeaders.add("Content-Type", "application/json");
    }


    //Base Game Loop
    //EXPECTATIONS VIOLATED ON THIS TEST. GET THAT LOBBY WORKING
    @Test
    public void singlePlayer_canStartGame_canJoinGame_canStartGame_canGetHand_canSubmitHand() throws Exception {
        //Create game with 1 human player slot
        String gameId = postWithBody_getStringResponse(baseUrl + "/game/create", GameConfig.standardHuman4Player);
        System.out.println("gameId="+gameId);


        //Join game
        String connectedPlayerId = postWithBody_getStringResponse(baseUrl + "/game/"+gameId+"/join", GameConfig.standardHuman4Player);
        System.out.println("connectedPlayerId="+connectedPlayerId);


        //Start game
        System.out.println("startingGame:"+gameId);
        postWithBody_getStringResponse(baseUrl + "/game/"+gameId+"/start", "");
        System.out.println("startedGame:"+gameId);


        //Get hand
        String playerGetHandUrl = baseUrl + "/game/"+gameId+"/player/"+connectedPlayerId+"/getHand";
        APIresponsePlayerGetHand hand = restTemplate.getForObject(
                playerGetHandUrl,
                APIresponsePlayerGetHand.class
        );
        System.out.println("got hand: " + hand);


        //Submit hand
        String playerSubmitHandUrl = baseUrl + "/game/"+gameId+"/player/"+connectedPlayerId+"/submitHand";
        APIrequestPlayerSubmitHand submitHand = new APIrequestPlayerSubmitHand(hand.getMovementCards().subList(0,5));
        System.out.println("submitting hand: " + submitHand);
        ResponseEntity<String> response = restTemplate.postForEntity(
                playerSubmitHandUrl,
                submitHand,
                String.class);
        System.out.println("submit hand response: " + response.getBody());


        //todo: implement some polling or something. meanwhile, wait 3 seconds for server to process turn
        Thread.sleep(3000);

        //Get second hand, make sure it's different
        APIresponsePlayerGetHand hand2 = restTemplate.getForObject(
                playerGetHandUrl,
                APIresponsePlayerGetHand.class
        );
        System.out.println("got hand2: " + hand2);
        assertThat(hand2).isNotEqualTo(hand);
    }


    //todo: use dependency objectmapper. some complication around bean scoping to non-test
    @Test
    public void startGameIntegrationTest_canStartMultipleGames() throws Exception {
        String url = baseUrl + "/playerStart";

        String gameId = postWithBody_getStringResponse(url, GameConfig.standardHuman4Player);
        assertThat(gameId).isNotEmpty();
    }

    public String postWithBody_getStringResponse(String url, Object o) throws Exception {
        try {
            HttpEntity<String> request = new HttpEntity<>(
                    (new ObjectMapper()).writeValueAsString(o),
                    httpHeaders
            );
            String output = restTemplate.postForEntity(
                    url,
                    request,
                    String.class
            ).getBody();
            return output;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

}