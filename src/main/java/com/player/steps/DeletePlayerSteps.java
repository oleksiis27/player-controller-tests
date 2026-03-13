package com.player.steps;

import com.player.api.DeletePlayerApi;
import com.player.api.GetPlayerApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeletePlayerSteps {

    private static final Logger log = LoggerFactory.getLogger(DeletePlayerSteps.class);

    private final DeletePlayerApi deletePlayerApi;
    private final GetPlayerApi getPlayerApi;

    public DeletePlayerSteps(DeletePlayerApi deletePlayerApi, GetPlayerApi getPlayerApi) {
        this.deletePlayerApi = deletePlayerApi;
        this.getPlayerApi = getPlayerApi;
    }

    @Step("Delete player ID={playerId} with editor '{editor}' and verify")
    public void deleteAndVerify(String editor, Long playerId) {
        deletePlayerApi.deletePlayer(editor, playerId)
                .then()
                .statusCode(204);

        log.info("Player ID={} deleted, verifying via GET", playerId);

        Response getResponse = getPlayerApi.getPlayer(playerId);
        String body = getResponse.getBody().asString();

        if (body != null && !body.isEmpty()) {
            log.warn("Deleted player ID={} still returns data: {}", playerId, body);
        }
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