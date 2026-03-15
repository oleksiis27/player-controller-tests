package com.player.data;

import com.player.models.Gender;
import com.player.models.PlayerDto;
import com.player.models.Role;
import net.datafaker.Faker;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataHelper {

    private TestDataHelper() {}

    private static final Faker faker = new Faker();

    public static PlayerDto validPreparedPlayer() {
        return validPreparedPlayerWithRole(Role.USER);
    }

    public static PlayerDto validPreparedPlayerWithRole(Role role) {
        return buildPlayer(role);
    }

    private static PlayerDto buildPlayer(Role role) {
        return PlayerDto.builder()
                .age(faker.number().numberBetween(17, 60))
                .gender(randomGender())
                .login(generateUniqueLogin())
                .password(validPassword())
                .role(role)
                .screenName(generateUniqueScreenName())
                .build();
    }

    public static Gender randomGender() {
        Gender[] values = Gender.values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

    public static String generateUniqueLogin() {
        return "login_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generateUniqueScreenName() {
        return "screen_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String validPassword() {
        return "ValidPass1!";
    }

    public static String shortPassword() {
        return "Ab1!";
    }

    public static String longPassword() {
        return "ValidPassword123";
    }

    public static String passwordWithoutDigits() {
        return "ValidPassword!";
    }

    public static String passwordWithoutLetters() {
        return "1234567!";
    }
}