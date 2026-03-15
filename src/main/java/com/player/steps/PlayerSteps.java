package com.player.steps;

import com.player.api.PlayerApi;
import com.player.models.PlayerDto;
import com.player.models.StatusCode;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerSteps {

    private static final Logger log = LoggerFactory.getLogger(PlayerSteps.class);

    private final PlayerApi playerApi;

    public PlayerSteps(PlayerApi playerApi) {
        this.playerApi = playerApi;
    }

    @Step("Create player with editor '{editor}'")
    public PlayerDto createPlayer(String editor, PlayerDto request) {
        PlayerDto created = playerApi.createPlayer(editor, request)
                .then()
                .statusCode(StatusCode.OK.getCode())
                .extract()
                .as(PlayerDto.class);

        log.info("Player created with ID: {}", created.getId());
        return created;
    }

    @Step("Attempt to create player with editor '{editor}'")
    public Response createExpectingAnyStatus(String editor, PlayerDto request) {
        return playerApi.createPlayer(editor, request);
    }

    @Step("Extract player from response if created successfully")
    public PlayerDto extractIfCreated(Response response) {
        if (response.statusCode() == StatusCode.OK.getCode()) {
            try {
                return response.as(PlayerDto.class);
            } catch (Exception e) {
                log.warn("Failed to extract player from response: {}", e.getMessage());
            }
        }
        return null;
    }

    @Step("Get player by ID={playerId}")
    public PlayerDto getPlayerById(Long playerId) {
        return playerApi.getPlayer(playerId)
                .then()
                .statusCode(StatusCode.OK.getCode())
                .extract()
                .as(PlayerDto.class);
    }

    @Step("Check if player ID={playerId} exists")
    public boolean playerExists(Long playerId) {
        Response response = playerApi.getPlayer(playerId);
        String body = response.getBody().asString();
        return body != null && !body.isEmpty();
    }

    @Step("Get all players")
    public List<PlayerDto> getAllPlayers() {
        return playerApi.getAllPlayers()
                .then()
                .statusCode(StatusCode.OK.getCode())
                .extract()
                .jsonPath()
                .getList("players", PlayerDto.class);
    }

    @Step("Get all player IDs")
    public List<Long> getAllPlayerIds() {
        return getAllPlayers().stream()
                .map(PlayerDto::getId)
                .collect(Collectors.toList());
    }

    @Step("Update player ID={playerId} with editor '{editor}'")
    public PlayerDto updatePlayer(String editor, Long playerId, PlayerDto request) {
        PlayerDto updated = playerApi.updatePlayer(editor, playerId, request)
                .then()
                .statusCode(StatusCode.OK.getCode())
                .extract()
                .as(PlayerDto.class);

        log.info("Player ID={} updated", playerId);
        return updated;
    }

    @Step("Attempt to update player ID={playerId}")
    public Response updateExpectingAnyStatus(String editor, Long playerId, PlayerDto request) {
        return playerApi.updatePlayer(editor, playerId, request);
    }

    @Step("Delete player ID={playerId} with editor '{editor}'")
    public void deletePlayer(String editor, Long playerId) {
        playerApi.deletePlayer(editor, playerId)
                .then()
                .statusCode(StatusCode.NO_CONTENT.getCode());

        log.info("Player ID={} deleted", playerId);
    }

    @Step("Attempt to delete player ID={playerId}")
    public Response deleteExpectingAnyStatus(String editor, Long playerId) {
        return playerApi.deletePlayer(editor, playerId);
    }

    @Step("Safely delete player ID={playerId}")
    public void deleteSafely(String editor, Long playerId) {
        if (playerId != null) {
            try {
                playerApi.deletePlayer(editor, playerId);
                log.info("Player ID={} cleaned up", playerId);
            } catch (Exception e) {
                log.warn("Failed to cleanup player ID={}: {}", playerId, e.getMessage());
            }
        }
    }
}