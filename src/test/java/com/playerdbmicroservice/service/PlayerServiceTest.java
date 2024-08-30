package com.playerdbmicroservice.service;

import com.playerdbmicroservice.entity.Player;
import com.playerdbmicroservice.repository.PlayerRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerServiceTest {

    // Retrieve all players with valid page and size parameters
    @Test
    public void test_get_all_players_with_valid_page_and_size() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        PlayerService playerService = new PlayerService();
        ReflectionTestUtils.setField(playerService, "playerRepository", playerRepository);

        List<Player> players = Arrays.asList(new Player(), new Player());
        Page<Player> page = new PageImpl<>(players);
        when(playerRepository.findAll(PageRequest.of(0, 2))).thenReturn(page);

        List<Player> result = playerService.getAllPlayers(0, 2);

        Assertions.assertEquals(2, result.size());
        Mockito.verify(playerRepository, Mockito.times(1)).findAll(PageRequest.of(0, 2));
    }

    // Handle negative page and size parameters
    @Test
    public void test_get_all_players_with_negative_page_and_size() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        PlayerService playerService = new PlayerService();
        ReflectionTestUtils.setField(playerService, "playerRepository", playerRepository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            playerService.getAllPlayers(-1, -1);
        });
    }

    // Retrieve a player by a valid playerID
    @Test
    public void test_retrieve_player_by_valid_id() {
        // Arrange
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        PlayerService playerService = new PlayerService();
        ReflectionTestUtils.setField(playerService, "playerRepository", playerRepository);

        String validPlayerID = "123";
        Player mockPlayer = new Player();
        when(playerRepository.findById(validPlayerID)).thenReturn(Optional.of(mockPlayer));

        // Act
        Optional<Player> result = playerService.getPlayerById(validPlayerID);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockPlayer, result.get());
    }

    // Return an empty Optional when playerID does not exist
    @Test
    public void test_return_empty_optional_for_non_existing_id() {
        // Arrange
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        PlayerService playerService = new PlayerService();
        ReflectionTestUtils.setField(playerService, "playerRepository", playerRepository);

        String nonExistingPlayerID = "999";
        when(playerRepository.findById(nonExistingPlayerID)).thenReturn(Optional.empty());

        // Act
        Optional<Player> result = playerService.getPlayerById(nonExistingPlayerID);

        // Assert
        assertTrue(result.isEmpty());
    }

    // Return an empty list when no players are found for given page and size
    @Test
    public void test_return_empty_list_no_players_found() {
        // Mocking the playerRepository
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        when(playerRepository.findAll(any(PageRequest.class))).thenReturn(Page.empty());

        // Setting up the PlayerService with the mocked repository
        PlayerService playerService = new PlayerService();
        ReflectionTestUtils.setField(playerService, "playerRepository", playerRepository);

        // Calling the method under test
        List<Player> result = playerService.getAllPlayers(0, 10);

        // Assertions
        assertTrue(result.isEmpty());
    }

    // Handle excessively large page and size parameters
    @Test
    public void test_handle_excessively_large_page_and_size() {
        // Mocking the playerRepository
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        when(playerRepository.findAll(any(PageRequest.class))).thenReturn(Page.empty());

        // Setting up the PlayerService with the mocked repository
        PlayerService playerService = new PlayerService();
        ReflectionTestUtils.setField(playerService, "playerRepository", playerRepository);

        // Calling the method under test with large page and size
        List<Player> result = playerService.getAllPlayers(Integer.MAX_VALUE, Integer.MAX_VALUE);

        // Assertions
        assertTrue(result.isEmpty());
    }
}