package com.player.api;

import com.player.models.PlayerDto;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class PlayerApiClient extends BaseApiClient {

    private static final Logger log = LoggerFactory.getLogger(PlayerApiClient.class);

    public Response createPlayer(String editor, PlayerDto request) {
        log.info("Creating player with editor '{}': {}", editor, request);
        return givenSpec()
                .queryParam("age", request.getAge())
                .queryParam("gender", request.getGender())
                .queryParam("login", request.getLogin())
                .queryParam("password", request.getPassword())
                .queryParam("role", request.getRole())
                .queryParam("screenName", request.getScreenName())
                .when()
                .get("/player/create/{editor}", editor);
    }

    public Response getPlayer(Long playerId) {
        log.info("Getting player by ID: {}", playerId);
        return givenSpec()
                .body(Collections.singletonMap("playerId", playerId))
                .when()
                .post("/player/get");
    }

    public Response getAllPlayers() {
        log.info("Getting all players");
        return givenSpec()
                .when()
                .get("/player/get/all");
    }

    public Response updatePlayer(String editor, Long id, PlayerDto request) {
        log.info("Updating player ID={} with editor '{}': {}", id, editor, request);
        return givenSpec()
                .body(request)
                .when()
                .patch("/player/update/{editor}/{id}", editor, id);
    }

    public Response deletePlayer(String editor, Long playerId) {
        log.info("Deleting player ID={} with editor '{}'", playerId, editor);
        return givenSpec()
                .body(Collections.singletonMap("playerId", playerId))
                .when()
                .delete("/player/delete/{editor}", editor);
    }
}