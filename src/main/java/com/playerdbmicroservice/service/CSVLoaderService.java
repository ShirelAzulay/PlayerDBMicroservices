package com.playerdbmicroservice.service;

import com.playerdbmicroservice.entity.Player;
import com.playerdbmicroservice.repository.PlayerRepository;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CSVLoaderService {

    private static final Logger logger = LoggerFactory.getLogger(CSVLoaderService.class);

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private static final String INVALID_PLAYER_TOPIC = "invalid-players";

    /**
     * Reloads the player data from the CSV file into the database.
     */
    @Transactional
    public void reloadCSV() {
        try (CSVReader reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/Player.csv")))) {
            logger.info("Started reloading player data from CSV");

            List<String[]> records = reader.readAll();
            Set<String> csvPlayerIds = getCsvPlayerIds(records);

            deleteNonExistingPlayers(csvPlayerIds);

            records.stream().skip(1).forEach(record -> processRecord(record));

            logger.info("Finished reloading player data from CSV");
        } catch (Exception e) {
            logger.error("Error reading CSV file", e);
        }
    }

    /**
     * Processes a single CSV record: validates it, updates or creates a player, and logs errors if any.
     *
     * @param record the CSV record as a String array
     */
    private void processRecord(String[] record) {
        try {
            String playerID = record[0];
            if(isInvalidPlayerID(playerID)) {
                sendInvalidPlayer(record);
                return;  // Skip the invalid record
            }

            Player player = getPlayerOrCreateNew(playerID);
            populatePlayerFields(player, record);

            playerRepository.save(player);
            logger.debug("Loaded/Updated player: {}", player);

            kafkaProducerService.sendMessage("Player loaded/updated: " + player.toString());
        } catch (Exception e) {
            logger.error("Error parsing record: {}", (Object) record, e);
            sendInvalidPlayer(record);
        }
    }

    /**
     * Returns true if the playerID is invalid (null or empty), false otherwise.
     *
     * @param playerID the ID of the player.
     * @return boolean indicating if the playerID is invalid.
     */
    private boolean isInvalidPlayerID(String playerID) {
        if (playerID == null || playerID.trim().isEmpty()) {
            logger.error("Invalid playerID: {}", playerID);
            return true;
        }
        return false;
    }

    /**
     * Retrieves an existing player by ID or creates a new instance if not found.
     *
     * @param playerID the ID of the player to retrieve or create
     * @return the Player object retrieved or created
     */
    private Player getPlayerOrCreateNew(String playerID) {
        Optional<Player> optionalPlayer = playerRepository.findById(playerID);
        Player player = optionalPlayer.orElseGet(Player::new);
        player.setPlayerID(playerID); // Set the playerID for the new player
        return player;
    }

    /**
     * Populates the fields of a Player object from a CSV record.
     * @param player the Player object to populate
     * @param record the CSV record
     */
    private void populatePlayerFields(Player player, String[] record) {
        player.setBirthYear(validateInteger(record[1], "BirthYear"));
        player.setBirthMonth(validateInteger(record[2], "BirthMonth"));
        player.setBirthDay(validateInteger(record[3], "BirthDay"));
        player.setBirthCountry(record[4]);
        player.setBirthState(record[5]);
        player.setBirthCity(record[6]);
        player.setDeathYear(validateInteger(record[7], "DeathYear"));
        player.setDeathMonth(validateInteger(record[8], "DeathMonth"));
        player.setDeathDay(validateInteger(record[9], "DeathDay"));
        player.setDeathCountry(record[10]);
        player.setDeathState(record[11]);
        player.setDeathCity(record[12]);
        player.setNameFirst(record[13]);
        player.setNameLast(record[14]);
        player.setNameGiven(record[15]);
        player.setWeight(validateInteger(record[16], "Weight"));
        player.setHeight(validateInteger(record[17], "Height"));
        player.setBats(record[18]);
        player.setThrowsHand(record[19]);
        player.setDebut(record[20]);
        player.setFinalGame(record[21]);
        player.setRetroID(record[22]);
        player.setBbrefID(record[23]);
    }

    /**
     * Validates and converts a String to an Integer, logging an error if the conversion fails.
     *
     * @param value the String value to validate and convert
     * @param fieldName the name of the field for logging purposes
     * @return the Integer value or null if validation/conversion fails
     */
    private Integer validateInteger(String value, String fieldName) {
        try {
            return value.isEmpty() ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Invalid value for {}: {}", fieldName, value);
            return null;
        }
    }

    /**
     * Sends an invalid player record to a Kafka topic for potential later correction.
     *
     * @param record the invalid player record as a String array
     */
    private void sendInvalidPlayer(String[] record) {
        try {
            Map<String, String> invalidRecord = mapCsvRecordToMap(record);

            kafkaProducerService.sendMessage(INVALID_PLAYER_TOPIC, invalidRecord.toString());
            logger.info("Sent invalid player record to Kafka: {}", invalidRecord);
        } catch (Exception e) {
            logger.error("Error sending invalid player record to Kafka: {}", (Object) record, e);
        }
    }

    /**
     * Maps the CSV record to a Map for easier processing and logging.
     *
     * @param record the CSV record as a String array
     * @return a Map representing the CSV record
     */
    private Map<String, String> mapCsvRecordToMap(String[] record) {
        Map<String, String> invalidRecord = new HashMap<>();
        invalidRecord.put("playerID", record[0]);
        invalidRecord.put("birthYear", record[1]);
        invalidRecord.put("birthMonth", record[2]);
        invalidRecord.put("birthDay", record[3]);
        invalidRecord.put("birthCountry", record[4]);
        invalidRecord.put("birthState", record[5]);
        invalidRecord.put("birthCity", record[6]);
        invalidRecord.put("deathYear", record[7]);
        invalidRecord.put("deathMonth", record[8]);
        invalidRecord.put("deathDay", record[9]);
        invalidRecord.put("deathCountry", record[10]);
        invalidRecord.put("deathState", record[11]);
        invalidRecord.put("deathCity", record[12]);
        invalidRecord.put("nameFirst", record[13]);
        invalidRecord.put("nameLast", record[14]);
        invalidRecord.put("nameGiven", record[15]);
        invalidRecord.put("weight", record[16]);
        invalidRecord.put("height", record[17]);
        invalidRecord.put("bats", record[18]);
        invalidRecord.put("throwsHand", record[19]);
        invalidRecord.put("debut", record[20]);
        invalidRecord.put("finalGame", record[21]);
        invalidRecord.put("retroID", record[22]);
        invalidRecord.put("bbrefID", record[23]);
        return invalidRecord;
    }

    /**
     * Extracts player IDs from the CSV records.
     *
     * @param records the list of CSV records
     * @return a set of player IDs
     */
    private Set<String> getCsvPlayerIds(List<String[]> records) {
        return records.stream()
                .skip(1) // Skip header row
                .map(record -> record[0]) // Extract playerID
                .collect(Collectors.toSet());
    }

    /**
     * Deletes players from the database that are not present in the CSV file anymore.
     *
     * @param csvPlayerIds the set of player IDs present in the CSV file
     */
    private void deleteNonExistingPlayers(Set<String> csvPlayerIds) {
        List<Player> playersToDelete = playerRepository.findAll().stream()
                .filter(player -> !csvPlayerIds.contains(player.getPlayerID()))
                .collect(Collectors.toList());
        playerRepository.deleteAll(playersToDelete);
    }
}