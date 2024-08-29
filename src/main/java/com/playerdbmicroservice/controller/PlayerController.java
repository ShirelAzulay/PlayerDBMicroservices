package com.playerdbmicroservice.controller;

import com.playerdbmicroservice.entity.Player;
import com.playerdbmicroservice.service.CSVLoaderService;
import com.playerdbmicroservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private CSVLoaderService csvLoaderService;

    @GetMapping
    public List<Player> getAllPlayers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return playerService.getAllPlayers(page, size);
    }

    @GetMapping("/{playerID}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String playerID) {
        Optional<Player> player = playerService.getPlayerById(playerID);
        return player.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/reload")
    public ResponseEntity<ReloadResponse> reloadCSV() {
        csvLoaderService.reloadCSV();
        ReloadResponse response = new ReloadResponse("success", "CSV reloaded successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}