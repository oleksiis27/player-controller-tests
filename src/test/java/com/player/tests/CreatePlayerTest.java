package com.player.tests;

import com.player.data.TestDataHelper;
import com.player.models.Gender;
import com.player.models.PlayerDto;
import com.player.models.Role;
import com.player.models.StatusCode;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Player Controller")
@Feature("Create Player")
public class CreatePlayerTest extends BaseTest {

    @Test(dataProvider = "editors")
    @Story("Positive: Create player with editor")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create player with all valid fields and verify via GET.")
    @Issue("BUG-001")
    public void testCreatePlayerWithEditor(String editor) {
        PlayerDto prepared = TestDataHelper.validPreparedPlayer();
        PlayerDto created = playerSteps.createPlayer(editor, prepared);
        trackPlayerForCleanup(created.getId());

        PlayerDto fetched = playerSteps.getPlayerById(created.getId());

        Assert.assertEquals(fetched, prepared, "Fetched player should match the create request");
    }

    @Test
    @Story("BUG: Duplicate login returns existing player")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Creating player with duplicate login should return error, but returns existing player")
    @Issue("BUG-002")
    public void testCreatePlayerWithDuplicateLogin() {
        PlayerDto created1 = createTestPlayer();

        PlayerDto request2 = PlayerDto.builder()
                .age(25).gender(Gender.FEMALE).login(created1.getLogin())
                .password(TestDataHelper.validPassword()).role(Role.USER)
                .screenName(TestDataHelper.generateUniqueScreenName())
                .build();

        Response response = playerSteps.createExpectingAnyStatus(SUPERVISOR, request2);
        PlayerDto player = playerSteps.extractIfCreated(response);
        if (player != null && !player.getId().equals(created1.getId())) {
            trackPlayerForCleanup(player.getId());
        }

        Assert.assertEquals(response.statusCode(), StatusCode.BAD_REQUEST.getCode(),
                "Duplicate login should return 400");
    }

    @Test
    @Story("BUG: Duplicate screenName creates new player")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Creating player with duplicate screenName should return error")
    @Issue("BUG-003")
    public void testCreatePlayerWithDuplicateScreenName() {
        PlayerDto created1 = createTestPlayer();

        PlayerDto request2 = PlayerDto.builder()
                .age(25).gender(Gender.MALE).login(TestDataHelper.generateUniqueLogin())
                .password(TestDataHelper.validPassword()).role(Role.USER)
                .screenName(created1.getScreenName())
                .build();

        Response response = playerSteps.createExpectingAnyStatus(SUPERVISOR, request2);
        PlayerDto player = playerSteps.extractIfCreated(response);
        if (player != null) trackPlayerForCleanup(player.getId());

        Assert.assertEquals(response.statusCode(), StatusCode.BAD_REQUEST.getCode(),
                "Duplicate screenName should return 400");
    }

    @Test(dataProvider = "invalidPasswords")
    @Story("BUG: Invalid password accepted on create")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Creating player with invalid password should fail")
    @Issue("BUG-004")
    public void testCreatePlayerWithInvalidPassword(String password, String reason) {
        PlayerDto request = PlayerDto.builder()
                .age(25).gender(Gender.MALE).login(TestDataHelper.generateUniqueLogin())
                .password(password).role(Role.USER)
                .screenName(TestDataHelper.generateUniqueScreenName())
                .build();

        Response response = playerSteps.createExpectingAnyStatus(SUPERVISOR, request);
        PlayerDto player = playerSteps.extractIfCreated(response);
        if (player != null) trackPlayerForCleanup(player.getId());

        Assert.assertEquals(response.statusCode(), StatusCode.BAD_REQUEST.getCode(),
                "Password (" + reason + ") should return 400");
    }

    @Test(dataProvider = "invalidAges")
    @Story("Negative: Create player with invalid age")
    @Severity(SeverityLevel.NORMAL)
    @Description("Creating player with age outside 17-59 range should fail")
    @Issue("BUG-009")
    public void testCreatePlayerWithInvalidAge(int age, String reason) {
        PlayerDto request = PlayerDto.builder()
                .age(age).gender(Gender.MALE).login(TestDataHelper.generateUniqueLogin())
                .password(TestDataHelper.validPassword()).role(Role.USER)
                .screenName(TestDataHelper.generateUniqueScreenName())
                .build();

        Response response = playerSteps.createExpectingAnyStatus(SUPERVISOR, request);
        PlayerDto player = playerSteps.extractIfCreated(response);
        if (player != null) trackPlayerForCleanup(player.getId());

        Assert.assertEquals(response.statusCode(), StatusCode.BAD_REQUEST.getCode(),
                "Age (" + reason + ") should return 400");
    }

    @Test
    @Story("Negative: Create player with supervisor role")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Creating player with 'supervisor' role should fail — only 'admin' or 'user' allowed")
    public void testCreatePlayerWithSupervisorRole() {
        PlayerDto request = PlayerDto.builder()
                .age(25).gender(Gender.MALE).login(TestDataHelper.generateUniqueLogin())
                .password(TestDataHelper.validPassword()).role(Role.SUPERVISOR)
                .screenName(TestDataHelper.generateUniqueScreenName())
                .build();

        Response response = playerSteps.createExpectingAnyStatus(SUPERVISOR, request);
        PlayerDto player = playerSteps.extractIfCreated(response);
        if (player != null) trackPlayerForCleanup(player.getId());

        Assert.assertEquals(response.statusCode(), StatusCode.BAD_REQUEST.getCode(),
                "Creating player with supervisor role should return 400");
    }
}