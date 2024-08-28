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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.nio.file.Paths;


@Service

public class CSVLoaderService {


    private static final Logger logger = LoggerFactory.getLogger(CSVLoaderService.class);

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Transactional
    public void reloadCSV() {
        try (CSVReader reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/Player.csv")))) {
       // try (CSVReader reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/Player_small.csv")))) {

            logger.info("Started reloading player data from CSV");
            List<String[]> records = reader.readAll();
            Set<String> csvPlayerIds = records.stream()
                                              .skip(1)
                                              .map(record -> record[0])
                                              .collect(Collectors.toSet());

            // Delete players not in CSV anymore
            List<Player> playersToDelete = playerRepository.findAll().stream()
                    .filter(player -> !csvPlayerIds.contains(player.getPlayerID()))
                    .collect(Collectors.toList());

            playerRepository.deleteAll(playersToDelete);

            records.stream().skip(1).forEach(record -> {

                try {
                    String playerID = record[0];

                    Optional<Player> optionalPlayer = playerRepository.findById(playerID);

                    Player player = optionalPlayer.orElseGet(Player::new);

                    player.setPlayerID(record[0]);

                    player.setBirthYear(record[1].isEmpty() ? null : Integer.parseInt(record[1]));

                    player.setBirthMonth(record[2].isEmpty() ? null : Integer.parseInt(record[2]));

                    player.setBirthDay(record[3].isEmpty() ? null : Integer.parseInt(record[3]));

                    player.setBirthCountry(record[4]);

                    // Set other fields...

                    playerRepository.save(player);

                    logger.debug("Loaded/Updated player: {}", player);

                    // Send Kafka message

                    //meanwhile disabled it => kafkaProducerService.sendMessage("Player loaded/updated: " + player);
                    kafkaProducerService.sendMessage("Player loaded/updated: " + player);

                } catch (Exception e) {
                    logger.error("Error parsing record: {}", (Object) record, e);
                }
            });


            logger.info("Finished reloading player data from CSV");
        } catch (Exception e) {

            logger.error("Error reading CSV file", e);

        }

    }

}