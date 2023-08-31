package com.nethermole.roborally.controllers.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethermole.roborally.gamepackage.GameConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerControllerTest {

    @LocalServerPort
    private int port;


    //todo: use dependency objectmapper. some complication around bean scoping to non-test
    @Test
    public void startGameIntegrationTest_canStartMultipleGames() throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        TestRestTemplate restTemplate = new TestRestTemplate();

        String url = "http://localhost:" + port + "/playerStart";
        HttpEntity<String> request = new HttpEntity<>(
                (new ObjectMapper()).writeValueAsString((new GameConfig(1, 3))),
                httpHeaders
        );

        String gameId = restTemplate.postForEntity(
                url,
                request,
                String.class
        ).getBody();
        assertThat(gameId).isEqualTo("0");  //todo: for now, counting upwards. should use uuids...

        gameId = restTemplate.postForEntity(
                url,
                request,
                String.class
        ).getBody();
        assertThat(gameId).isEqualTo("1");  //todo: for now, counting upwards. should use uuids...
    }

}