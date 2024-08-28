package com.playerdbmicroservice;

import com.playerdbmicroservice.controller.PlayerController;
import com.playerdbmicroservice.entity.Player;
import com.playerdbmicroservice.repository.PlayerRepository;
import com.playerdbmicroservice.service.CSVLoaderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PlayerDbMicroserviceApplication.class)
public class PlayerControllerTest {

    @Test
    public void test_get_all_players_with_default_pagination() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        CSVLoaderService csvLoaderService = mock(CSVLoaderService.class);

        PlayerController playerController = new PlayerController();
        ReflectionTestUtils.setField(playerController, "playerRepository", playerRepository);
        ReflectionTestUtils.setField(playerController, "csvLoaderService", csvLoaderService);

        List<Player> players = Arrays.asList(new Player(), new Player());
        PageImpl<Player> page = new PageImpl<>(players);
        when(playerRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        List<Player> result = playerController.getAllPlayers(0, 10);
        assertEquals(2, result.size());
        verify(playerRepository, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    public void test_get_player_by_non_existent_id() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        CSVLoaderService csvLoaderService = mock(CSVLoaderService.class);

        PlayerController playerController = new PlayerController();
        ReflectionTestUtils.setField(playerController, "playerRepository", playerRepository);
        ReflectionTestUtils.setField(playerController, "csvLoaderService", csvLoaderService);

        String nonExistentId = "nonExistentId";
        when(playerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<Player> result = playerController.getPlayerById(nonExistentId);
        assertFalse(result.isPresent());
        verify(playerRepository, times(1)).findById(nonExistentId);
    }
}