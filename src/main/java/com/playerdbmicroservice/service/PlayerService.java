package com.playerdbmicroservice.service;

import com.playerdbmicroservice.entity.Player;
import com.playerdbmicroservice.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getAllPlayers(int page, int size) {
        return playerRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public Optional<Player> getPlayerById(String playerID) {
        return playerRepository.findById(playerID);
    }
}