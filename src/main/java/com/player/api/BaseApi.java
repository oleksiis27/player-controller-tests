package com.player.api;

import com.player.config.AppConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseApi {

    protected final RequestSpecification spec;

    protected BaseApi() {
        spec = new RequestSpecBuilder()
                .setBaseUri(AppConfig.getInstance().baseUrl())
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    protected RequestSpecification givenSpec() {
        return given().spec(spec);
    }
}