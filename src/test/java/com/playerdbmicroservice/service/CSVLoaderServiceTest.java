/*
package com.playerdbmicroservice.service;

import com.opencsv.CSVReader;
import com.playerdbmicroservice.entity.Player;
import com.playerdbmicroservice.repository.PlayerRepository;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.mockito.Mockito.*;

public class CSVLoaderServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(CSVLoaderServiceTest.class);

    @Test
    public void test_reload_csv_success() throws IOException, com.opencsv.exceptions.CsvException {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        KafkaProducerService kafkaProducerService = mock(KafkaProducerService.class);

        CSVLoaderService csvLoaderService = new CSVLoaderService();
        ReflectionTestUtils.setField(csvLoaderService, "playerRepository", playerRepository);
        ReflectionTestUtils.setField(csvLoaderService, "kafkaProducerService", kafkaProducerService);

        CSVReader csvReader = mock(CSVReader.class);
        when(csvReader.readAll()).thenReturn(List.of(
                new String[]{"playerID", "birthYear", "birthMonth", "birthDay", "birthCountry", "birthState", "birthCity", "deathYear", "deathMonth", "deathDay", "deathCountry", "deathState", "deathCity", "nameFirst", "nameLast", "nameGiven", "weight", "height", "bats", "throwsHand", "debut", "finalGame", "retroID", "bbrefID"},
                new String[]{"1", "1980", "5", "20", "USA", "CA", "Los Angeles", "", "", "", "", "", "", "John", "Doe", "", "180", "75", "R", "R", "", "", "", ""}
        ));

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        when(csvLoaderService.getClass().getResourceAsStream("/Player.csv")).thenReturn(inputStream);

        csvLoaderService.reloadCSV();

        verify(playerRepository, times(1)).save(any(Player.class));
        verify(kafkaProducerService, times(1)).sendMessage(anyString());
    }

    @Test
    public void test_handle_invalid_player_id() throws IOException, com.opencsv.exceptions.CsvException {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        KafkaProducerService kafkaProducerService = mock(KafkaProducerService.class);

        CSVLoaderService csvLoaderService = new CSVLoaderService();
        ReflectionTestUtils.setField(csvLoaderService, "playerRepository", playerRepository);
        ReflectionTestUtils.setField(csvLoaderService, "kafkaProducerService", kafkaProducerService);

        CSVReader csvReader = mock(CSVReader.class);
        when(csvReader.readAll()).thenReturn(List.of(
                new String[]{"playerID", "birthYear", "birthMonth", "birthDay", "birthCountry", "birthState", "birthCity", "deathYear", "deathMonth", "deathDay", "deathCountry", "deathState", "deathCity", "nameFirst", "nameLast", "nameGiven", "weight", "height", "bats", "throwsHand", "debut", "finalGame", "retroID", "bbrefID"},
                new String[]{" ", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}
        ));

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        when(csvLoaderService.getClass().getResourceAsStream("/Player.csv")).thenReturn(inputStream);

        csvLoaderService.reloadCSV();

        verify(kafkaProducerService, times(1)).sendMessage(eq("invalid-players"), anyString());
    }



}*/
