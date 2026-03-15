package com.player.api;

import com.player.models.PlayerDto;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdatePlayerApi extends BaseApi {

    private static final Logger log = LoggerFactory.getLogger(UpdatePlayerApi.class);

    public Response updatePlayer(String editor, Long id, PlayerDto request) {
        log.info("Updating player ID={} with editor '{}': {}", id, editor, request);
        return givenSpec()
                .body(request)
                .when()
                .patch("/player/update/{editor}/{id}", editor, id);
    }
}