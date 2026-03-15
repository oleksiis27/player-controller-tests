package com.player.steps;

import com.player.api.CreatePlayerApi;
import com.player.models.PlayerDto;
import com.player.models.StatusCode;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatePlayerSteps {

    private static final Logger log = LoggerFactory.getLogger(CreatePlayerSteps.class);

    private final CreatePlayerApi createPlayerApi;

    public CreatePlayerSteps(CreatePlayerApi createPlayerApi) {
        this.createPlayerApi = createPlayerApi;
    }

    @Step("Create player with editor '{editor}'")
    public PlayerDto createPlayer(String editor, PlayerDto request) {
        PlayerDto created = createPlayerApi.createPlayer(editor, request)
                .then()
                .statusCode(StatusCode.OK.getCode())
                .extract()
                .as(PlayerDto.class);

        log.info("Player created with ID: {}", created.getId());
        return created;
    }

    @Step("Attempt to create player with editor '{editor}' (expect possible error)")
    public Response createExpectingAnyStatus(String editor, PlayerDto request) {
        return createPlayerApi.createPlayer(editor, request);
    }

    @Step("Extract player from response if created successfully")
    public PlayerDto extractIfCreated(Response response) {
        if (response.statusCode() == StatusCode.OK.getCode()) {
            try {
                return response.as(PlayerDto.class);
            } catch (Exception e) {
                log.warn("Failed to extract player from response: {}", e.getMessage());
            }
        }
        return null;
    }
}