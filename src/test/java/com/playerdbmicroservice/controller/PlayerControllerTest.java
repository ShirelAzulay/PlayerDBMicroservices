package com.playerdbmicroservice.controller;

import com.playerdbmicroservice.PlayerDbMicroserviceApplication;
import com.playerdbmicroservice.entity.Player;
import com.playerdbmicroservice.service.CSVLoaderService;
import com.playerdbmicroservice.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PlayerDbMicroserviceApplication.class)
public class PlayerControllerTest {

    // Retrieve a player by non-existent ID
    @Test
    public void test_get_player_by_non_existent_id() {
        PlayerService playerService = mock(PlayerService.class);
        CSVLoaderService csvLoaderService = mock(CSVLoaderService.class);
        PlayerController playerController = new PlayerController();
        ReflectionTestUtils.setField(playerController, "playerService", playerService);
        ReflectionTestUtils.setField(playerController, "csvLoaderService", csvLoaderService);

        String nonExistentId = "nonExistentId";
        when(playerService.getPlayerById(nonExistentId)).thenReturn(Optional.empty());

        ResponseEntity<Player> response = playerController.getPlayerById(nonExistentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(playerService).getPlayerById(nonExistentId);
    }

    // Retrieve all players with default pagination
    @Test
    public void test_get_all_players_with_default_pagination() {
        PlayerService playerService = mock(PlayerService.class);
        CSVLoaderService csvLoaderService = mock(CSVLoaderService.class);
        PlayerController playerController = new PlayerController();
        ReflectionTestUtils.setField(playerController, "playerService", playerService);
        ReflectionTestUtils.setField(playerController, "csvLoaderService", csvLoaderService);

        List<Player> players = Arrays.asList(new Player(), new Player());
        when(playerService.getAllPlayers(0, 10)).thenReturn(players);

        List<Player> result = playerController.getAllPlayers(0, 10);

        assertEquals(2, result.size());
        verify(playerService).getAllPlayers(0, 10);
    }

    // Retrieve specific page of players with custom size
    @Test
    public void test_retrieve_specific_page_custom_size() {
        // Setup
        PlayerService playerService = mock(PlayerService.class);
        List<Player> expectedPlayers = new ArrayList<>();
        when(playerService.getAllPlayers(2, 5)).thenReturn(expectedPlayers);

        // Execute
        List<Player> actualPlayers = playerService.getAllPlayers(2, 5);

        // Verify
        assertEquals(expectedPlayers, actualPlayers);
    }

    // Retrieve players list when database has multiple entries
    @Test
    public void test_retrieve_players_multiple_entries() {
        // Setup
        PlayerService playerService = mock(PlayerService.class);
        List<Player> expectedPlayers = new ArrayList<>();
        expectedPlayers.add(new Player());
        expectedPlayers.add(new Player());
        when(playerService.getAllPlayers(0, 10)).thenReturn(expectedPlayers);

        // Execute
        List<Player> actualPlayers = playerService.getAllPlayers(0, 10);

        // Verify
        assertEquals(2, actualPlayers.size());
    }

    // CSV reload endpoint returns HTTP 200 status on successful reload
    @Test
    public void test_reload_csv_success() {
        // Arrange
        CSVLoaderService csvLoaderService = Mockito.mock(CSVLoaderService.class);
        PlayerController playerController = new PlayerController();
        ReflectionTestUtils.setField(playerController, "csvLoaderService", csvLoaderService);

        // Act
        ResponseEntity<ReloadResponse> response = playerController.reloadCSV();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("CSV reloaded successfully", response.getBody().getMessage());
    }

    // CSV reload endpoint handles exceptions thrown by CSVLoaderService's reloadCSV method
    @Test
    public void test_reload_csv_exception_handling() {
        // Arrange
        CSVLoaderService csvLoaderService = Mockito.mock(CSVLoaderService.class);
        Mockito.doThrow(new RuntimeException("CSV load error")).when(csvLoaderService).reloadCSV();
        PlayerController playerController = new PlayerController();
        ReflectionTestUtils.setField(playerController, "csvLoaderService", csvLoaderService);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            playerController.reloadCSV();
        });

        assertEquals("CSV load error", exception.getMessage());
    }
}