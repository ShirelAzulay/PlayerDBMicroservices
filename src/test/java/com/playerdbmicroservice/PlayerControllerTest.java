package com.playerdbmicroservice;

import com.playerdbmicroservice.controller.PlayerController;
import com.playerdbmicroservice.controller.ErrorResponse;
import com.playerdbmicroservice.entity.Player;
import com.playerdbmicroservice.service.CSVLoaderService;
import com.playerdbmicroservice.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PlayerDbMicroserviceApplication.class)
public class PlayerControllerTest {

    @Test
    public void test_get_all_players_with_default_pagination() {
        PlayerService playerService = mock(PlayerService.class);
        CSVLoaderService csvLoaderService = mock(CSVLoaderService.class);

        PlayerController playerController = new PlayerController();
        ReflectionTestUtils.setField(playerController, "playerService", playerService);
        ReflectionTestUtils.setField(playerController, "csvLoaderService", csvLoaderService);

        List<Player> players = Arrays.asList(new Player(), new Player());
        PageImpl<Player> page = new PageImpl<>(players);

        when(playerService.getAllPlayers(0, 10)).thenReturn(page.getContent());

        List<Player> result = playerController.getAllPlayers(0, 10);

        assertEquals(2, result.size());

        verify(playerService, times(1)).getAllPlayers(0, 10);
    }

    @Test
    public void test_get_player_by_non_existent_id() {
        PlayerService playerService = mock(PlayerService.class);
        CSVLoaderService csvLoaderService = mock(CSVLoaderService.class);

        PlayerController playerController = new PlayerController();
        ReflectionTestUtils.setField(playerController, "playerService", playerService);
        ReflectionTestUtils.setField(playerController, "csvLoaderService", csvLoaderService);

        String nonExistentId = "nonExistentId";

        when(playerService.getPlayerById(nonExistentId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = playerController.getPlayerById(nonExistentId);

        // Ensure the response is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Ensure the response has a body
        assertTrue(response.hasBody());

        // Ensure the response body is ErrorResponse
        assertTrue(response.getBody() instanceof ErrorResponse);

        // Check the error message content
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Player with ID " + nonExistentId + " not found", errorResponse.getMessage());

        verify(playerService, times(1)).getPlayerById(nonExistentId);
    }
}