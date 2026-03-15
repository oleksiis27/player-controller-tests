package com.player.api;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class DeletePlayerApi extends BaseApi {

    private static final Logger log = LoggerFactory.getLogger(DeletePlayerApi.class);

    public Response deletePlayer(String editor, Long playerId) {
        log.info("Deleting player ID={} with editor '{}'", playerId, editor);
        return givenSpec()
                .body(Collections.singletonMap("playerId", playerId))
                .when()
                .delete("/player/delete/{editor}", editor);
    }
}