package com.player.api;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class GetPlayerApi extends BaseApi {

    private static final Logger log = LoggerFactory.getLogger(GetPlayerApi.class);

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
}