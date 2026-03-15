package com.player.steps;

import com.player.api.DeletePlayerApi;
import com.player.models.StatusCode;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeletePlayerSteps {

    private static final Logger log = LoggerFactory.getLogger(DeletePlayerSteps.class);

    private final DeletePlayerApi deletePlayerApi;

    public DeletePlayerSteps(DeletePlayerApi deletePlayerApi) {
        this.deletePlayerApi = deletePlayerApi;
    }

    @Step("Delete player ID={playerId} with editor '{editor}'")
    public void deletePlayer(String editor, Long playerId) {
        deletePlayerApi.deletePlayer(editor, playerId)
                .then()
                .statusCode(StatusCode.NO_CONTENT.getCode());

        log.info("Player ID={} deleted", playerId);
    }

    @Step("Attempt to delete player ID={playerId} (expect possible error)")
    public Response deleteExpectingAnyStatus(String editor, Long playerId) {
        return deletePlayerApi.deletePlayer(editor, playerId);
    }

    @Step("Safely delete player ID={playerId} (ignore errors)")
    public void deleteSafely(String editor, Long playerId) {
        if (playerId != null) {
            try {
                deletePlayerApi.deletePlayer(editor, playerId);
                log.info("Player ID={} cleaned up", playerId);
            } catch (Exception e) {
                log.warn("Failed to cleanup player ID={}: {}", playerId, e.getMessage());
            }
        }
    }
}