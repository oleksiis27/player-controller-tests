package com.player.api;

import com.player.models.PlayerDto;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatePlayerApi extends BaseApi {

    private static final Logger log = LoggerFactory.getLogger(CreatePlayerApi.class);

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
}