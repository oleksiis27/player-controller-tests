package com.player.tests;

import com.player.data.TestDataHelper;
import com.player.models.Gender;
import com.player.models.PlayerDto;
import com.player.models.Role;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


@Epic("Player Controller")
@Feature("Create Player")
public class CreatePlayerTest extends BaseTest {

    @Test(dataProvider = "editors")
    @Story("Positive: Create player with editor")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create player with all valid fields and verify via GET. Known BUG-001: admin editor returns 403")
    @Issue("BUG-001")
    public void testCreatePlayerWithEditor(String editor) {
        PlayerDto prepared = TestDataHelper.validPreparedPlayer();
        PlayerDto created = createSteps.createPlayer(editor, prepared);
        trackPlayerForCleanup(created.getId());

        PlayerDto fetched = getSteps.getPlayerById(created.getId());

        Assert.assertEquals(fetched, prepared, "Fetched player should match the create request");
    }

    @Test
    @Story("BUG: Duplicate login returns existing player")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Creating player with duplicate login should return error, but returns existing player")
    @Issue("BUG-003")
    public void testCreatePlayerWithDuplicateLogin() {
        PlayerDto request1 = TestDataHelper.validPreparedPlayer();
        PlayerDto created1 = createSteps.createPlayer(SUPERVISOR, request1);
        trackPlayerForCleanup(created1.getId());

        PlayerDto request2 = PlayerDto.builder()
                .age(25).gender(Gender.FEMALE).login(request1.getLogin())
                .password(TestDataHelper.validPassword()).role(Role.USER)
                .screenName(TestDataHelper.generateUniqueScreenName())
                .build();

        Response response = createSteps.createExpectingAnyStatus(SUPERVISOR, request2);
        PlayerDto player = createSteps.extractIfCreated(response);
        if (player != null && !player.getId().equals(created1.getId())) {
            trackPlayerForCleanup(player.getId());
        }

        Assert.assertEquals(response.statusCode(), 400,
                "Duplicate login should return 400.");
    }

    @Test
    @Story("BUG: Duplicate screenName creates new player")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Creating player with duplicate screenName should return error")
    @Issue("BUG-004")
    public void testCreatePlayerWithDuplicateScreenName() {
        PlayerDto request1 = TestDataHelper.validPreparedPlayer();
        PlayerDto created1 = createSteps.createPlayer(SUPERVISOR, request1);
        trackPlayerForCleanup(created1.getId());

        PlayerDto request2 = PlayerDto.builder()
                .age(25).gender(Gender.MALE).login(TestDataHelper.generateUniqueLogin())
                .password(TestDataHelper.validPassword()).role(Role.USER)
                .screenName(request1.getScreenName())
                .build();

        Response response = createSteps.createExpectingAnyStatus(SUPERVISOR, request2);
        PlayerDto player = createSteps.extractIfCreated(response);
        if (player != null) trackPlayerForCleanup(player.getId());

        Assert.assertEquals(response.statusCode(), 400,
                "Duplicate screenName should return 400.");
    }

    @Test(dataProvider = "invalidPasswords")
    @Story("BUG: Invalid password accepted on create")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Creating player with invalid password should fail")
    @Issue("BUG-005")
    public void testCreatePlayerWithInvalidPassword(String password, String reason) {
        PlayerDto request = PlayerDto.builder()
                .age(25).gender(Gender.MALE).login(TestDataHelper.generateUniqueLogin())
                .password(password).role(Role.USER)
                .screenName(TestDataHelper.generateUniqueScreenName())
                .build();

        Response response = createSteps.createExpectingAnyStatus(SUPERVISOR, request);
        PlayerDto player = createSteps.extractIfCreated(response);
        if (player != null) trackPlayerForCleanup(player.getId());

        Assert.assertEquals(response.statusCode(), 400,
                "Password (" + reason + ") should return 400.");
    }

    @DataProvider(name = "editors")
    public Object[][] editors() {
        return new Object[][] {
                { SUPERVISOR },
                { ADMIN }
        };
    }

    @DataProvider(name = "invalidPasswords")
    public Object[][] invalidPasswords() {
        return new Object[][] {
                { TestDataHelper.shortPassword(), "too short" },
                { TestDataHelper.passwordWithoutSpecialChars(), "no special chars" }
        };
    }
}