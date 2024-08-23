/*
package com.playerdbmicroservice.controller;

import com.playerdbmicroservice.entity.Player;
import com.playerdbmicroservice.repository.PlayerRepository;
import com.playerdbmicroservice.service.CSVLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController

@RequestMapping("/api/players")
public class PlayerController_back {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CSVLoaderService csvLoaderService;

    @GetMapping
    public List<Player> getAllPlayers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return playerRepository.findAll(PageRequest.of(page, size)).getContent();

    }


    @GetMapping("/{playerID}")
    public Optional<Player> getPlayerById(@PathVariable String playerID) {
        return playerRepository.findById(playerID);
    }



    @PostMapping("/reload")
    public void reloadCSV() {
        csvLoaderService.reloadCSV();
    }

}*/
