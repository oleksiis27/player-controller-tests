package com.player.steps;

import com.player.api.GetPlayerApi;
import com.player.models.PlayerDto;
import com.player.models.StatusCode;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.stream.Collectors;

public class GetPlayerSteps {

    private final GetPlayerApi getPlayerApi;

    public GetPlayerSteps(GetPlayerApi getPlayerApi) {
        this.getPlayerApi = getPlayerApi;
    }

    @Step("Get player by ID={playerId}")
    public PlayerDto getPlayerById(Long playerId) {
        return getPlayerApi.getPlayer(playerId)
                .then()
                .statusCode(StatusCode.OK.getCode())
                .extract()
                .as(PlayerDto.class);
    }

    @Step("Check if player ID={playerId} exists")
    public boolean playerExists(Long playerId) {
        Response response = getPlayerApi.getPlayer(playerId);
        String body = response.getBody().asString();
        return body != null && !body.isEmpty();
    }

    @Step("Get all players")
    public List<PlayerDto> getAllPlayers() {
        return getPlayerApi.getAllPlayers()
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
}