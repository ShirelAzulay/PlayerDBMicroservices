package java.com.example.playerdbmicroservice;



import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.ResponseEntity;



import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class PlayerControllerTests {



    @Autowired

    private TestRestTemplate restTemplate;



    @Test

    public void getAllPlayersWithPagination() {

        ResponseEntity<String> response = restTemplate.getForEntity("/api/players?page=0&size=10", String.class);

        assertThat(response.getBody()).contains("playerID");

    }



    @Test

    public void getPlayerById() {

        String playerId = "somePlayerId";

        ResponseEntity<String> response = restTemplate.getForEntity("/api/players/" + playerId, String.class);

        assertThat(response.getBody()).contains("playerID");

    }

}