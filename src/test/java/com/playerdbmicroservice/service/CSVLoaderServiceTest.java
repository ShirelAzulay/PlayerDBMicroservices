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
        when(csvReader.readAll()).thenReturn(getMockRecords());

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        when(csvReader.readAll()).thenReturn(getMockRecords());

        csvLoaderService.reloadCSV();

        verify(playerRepository, times(getMockRecords().size())).save(any(Player.class));
        verify(kafkaProducerService, times(getMockRecords().size())).sendMessage(anyString());
    }

    @Test
    public void test_handle_invalid_player_id() throws IOException, com.opencsv.exceptions.CsvException {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        KafkaProducerService kafkaProducerService = mock(KafkaProducerService.class);

        CSVLoaderService csvLoaderService = new CSVLoaderService();
        ReflectionTestUtils.setField(csvLoaderService, "playerRepository", playerRepository);
        ReflectionTestUtils.setField(csvLoaderService, "kafkaProducerService", kafkaProducerService);

        CSVReader csvReader = mock(CSVReader.class);
        when(csvReader.readAll()).thenReturn(Arrays.asList(
                new String[]{"playerID", "birthYear", "birthMonth", "birthDay", "birthCountry", "birthState", "birthCity", "deathYear", "deathMonth", "deathDay", "deathCountry", "deathState", "deathCity", "nameFirst", "nameLast", "nameGiven", "weight", "height", "bats", "throwsHand", "debut", "finalGame", "retroID", "bbrefID"},
                new String[]{" ", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}
        ));

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        when(csvReader.readAll()).thenReturn(Arrays.asList(
                new String[]{"playerID", "birthYear", "birthMonth", "birthDay", "birthCountry", "birthState", "birthCity", "deathYear", "deathMonth", "deathDay", "deathCountry", "deathState", "deathCity", "nameFirst", "nameLast", "nameGiven", "weight", "height", "bats", "throwsHand", "debut", "finalGame", "retroID", "bbrefID"},
                new String[]{" ", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}
        ));

        csvLoaderService.reloadCSV();

        verify(kafkaProducerService, times(1)).sendMessage(eq("invalid-players"), anyString());
    }

    @Test
    public void test_process_record_updates_or_creates_players() throws IOException, com.opencsv.exceptions.CsvException {
        CSVReader csvReader = mock(CSVReader.class);
        when(csvReader.readAll()).thenReturn(getMockRecords());

        PlayerRepository playerRepository = mock(PlayerRepository.class);
        when(playerRepository.findById(anyString())).thenReturn(Optional.empty());

        KafkaProducerService kafkaProducerService = mock(KafkaProducerService.class);

        CSVLoaderService csvLoaderService = new CSVLoaderService();
        ReflectionTestUtils.setField(csvLoaderService, "playerRepository", playerRepository);
        ReflectionTestUtils.setField(csvLoaderService, "kafkaProducerService", kafkaProducerService);

        ReflectionTestUtils.invokeMethod(csvLoaderService, "processRecord", new Object[]{new String[]{"1", "1990", "10", "5", "USA", "NY", "NYC", "2020", "10", "15", "USA", "CA", "LA", "John", "Doe", "John Doe", "180", "75", "R", "R", "2000-01-01", "2020-12-31", "JD001", "BB001"}});

        verify(playerRepository, times(1)).save(any(Player.class));
        verify(kafkaProducerService, times(1)).sendMessage(anyString());
    }


    private List<String[]> getMockRecords() {
        List<String[]> records = new ArrayList<>();
        for (int i = 0; i < 19369; i++) {
            records.add(new String[]{"1", "1990", "10", "5", "USA", "NY", "NYC", "2020", "10", "15", "USA", "CA", "LA", "John", "Doe", "John Doe", "180", "75", "R", "R", "2000-01-01", "2020-12-31", "JD001", "BB001"});
        }
        return records;
    }
}