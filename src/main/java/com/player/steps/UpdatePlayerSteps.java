package com.player.steps;

import com.player.api.UpdatePlayerApi;
import com.player.models.PlayerDto;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UpdatePlayerSteps {

    private static final Logger log = LoggerFactory.getLogger(UpdatePlayerSteps.class);

    private final UpdatePlayerApi updatePlayerApi;

    public UpdatePlayerSteps(UpdatePlayerApi updatePlayerApi) {
        this.updatePlayerApi = updatePlayerApi;
    }

    @Step("Update player ID={playerId} with editor '{editor}'")
    public PlayerDto update(String editor, Long playerId, PlayerDto request) {
        PlayerDto updated = updatePlayerApi.updatePlayer(editor, playerId, request)
                .then()
                .statusCode(200)
                .extract()
                .as(PlayerDto.class);

        log.info("Player ID={} updated", playerId);
        return updated;
    }

    @Step("Attempt to update player ID={playerId} (expect possible error)")
    public Response updateExpectingAnyStatus(String editor, Long playerId, PlayerDto request) {
        return updatePlayerApi.updatePlayer(editor, playerId, request);
    }
}