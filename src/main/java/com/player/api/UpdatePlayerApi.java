package com.player.api;

import com.player.models.PlayerDto;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UpdatePlayerApi extends BaseApi {

    private static final Logger log = LoggerFactory.getLogger(UpdatePlayerApi.class);

    @Step("Update player ID={id} with editor '{editor}'")
    public Response updatePlayer(String editor, Long id, PlayerDto request) {
        log.info("Updating player ID={} with editor '{}': {}", id, editor, request);
        return givenSpec()
                .body(request)
                .when()
                .patch("/player/update/{editor}/{id}", editor, id);
    }

    @Step("Update player ID={id} with raw body, editor '{editor}'")
    public Response updatePlayerRaw(String editor, Long id, Map<String, Object> body) {
        log.info("Updating player ID={} with raw body, editor '{}': {}", id, editor, body);
        return givenSpec()
                .body(body)
                .when()
                .patch("/player/update/{editor}/{id}", editor, id);
    }
}