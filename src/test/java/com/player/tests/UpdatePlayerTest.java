package com.player.tests;

import com.player.data.TestDataHelper;
import com.player.models.Gender;
import com.player.models.PlayerDto;
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
@Feature("Update Player")
public class UpdatePlayerTest extends BaseTest {

    @Test
    @Story("Positive: Update player age")
    @Severity(SeverityLevel.NORMAL)
    @Description("Update player age and verify via GET")
    public void testUpdatePlayerAge() {
        PlayerDto player = createTestPlayer();
        PlayerDto update = PlayerDto.builder().age(30).build();

        updateSteps.updatePlayer(SUPERVISOR, player.getId(), update);
        PlayerDto fetched = getSteps.getPlayerById(player.getId());

        Assert.assertEquals(fetched.getAge(), Integer.valueOf(30), "Age should be updated to 30");
    }

    @Test
    @Story("Positive: Update player screenName")
    @Severity(SeverityLevel.NORMAL)
    @Description("Update player screenName to a unique value and verify via GET")
    public void testUpdatePlayerScreenName() {
        PlayerDto player = createTestPlayer();
        String newScreenName = TestDataHelper.generateUniqueScreenName();
        PlayerDto update = PlayerDto.builder().screenName(newScreenName).build();

        updateSteps.updatePlayer(SUPERVISOR, player.getId(), update);
        PlayerDto fetched = getSteps.getPlayerById(player.getId());

        Assert.assertEquals(fetched.getScreenName(), newScreenName, "ScreenName should be updated");
    }

    @Test
    @Story("Positive: Update player gender")
    @Severity(SeverityLevel.NORMAL)
    @Description("Update player gender to valid value and verify via GET")
    public void testUpdatePlayerGender() {
        PlayerDto player = createTestPlayer();
        PlayerDto current = getSteps.getPlayerById(player.getId());

        Gender newGender = current.getGender() == Gender.MALE ? Gender.FEMALE : Gender.MALE;
        PlayerDto update = PlayerDto.builder().gender(newGender).build();

        updateSteps.updatePlayer(SUPERVISOR, player.getId(), update);
        PlayerDto fetched = getSteps.getPlayerById(player.getId());

        Assert.assertEquals(fetched.getGender(), newGender, "Gender should be updated");
    }

    @Test
    @Story("Positive: Update player password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Update player password with valid format")
    public void testUpdatePlayerPassword() {
        PlayerDto player = createTestPlayer();
        String newPassword = "NewValid1!";
        PlayerDto update = PlayerDto.builder().password(newPassword).build();

        updateSteps.updatePlayer(SUPERVISOR, player.getId(), update);
        PlayerDto fetched = getSteps.getPlayerById(player.getId());

        Assert.assertEquals(fetched.getPassword(), newPassword, "Password should be updated");
    }

    @Test
    @Story("BUG: Update screenName to existing one")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Updating screenName to an already existing one should fail")
    @Issue("BUG-008")
    public void testUpdateScreenNameToDuplicate() {
        PlayerDto player1 = createTestPlayer();
        PlayerDto player2 = createTestPlayer();

        PlayerDto fetched1 = getSteps.getPlayerById(player1.getId());
        PlayerDto update = PlayerDto.builder().screenName(fetched1.getScreenName()).build();

        Response response = updateSteps.updateExpectingAnyStatus(SUPERVISOR, player2.getId(), update);

        Assert.assertEquals(response.statusCode(), StatusCode.BAD_REQUEST.getCode(),
                "Duplicate screenName should return 400");
    }

    @Test
    @Story("BUG: Update password to invalid format")
    @Severity(SeverityLevel.NORMAL)
    @Description("Updating password to invalid format should fail")
    @Issue("BUG-010")
    public void testUpdatePlayerWithInvalidPassword() {
        PlayerDto player = createTestPlayer();
        PlayerDto update = PlayerDto.builder().password(TestDataHelper.shortPassword()).build();

        Response response = updateSteps.updateExpectingAnyStatus(SUPERVISOR, player.getId(), update);

        Assert.assertEquals(response.statusCode(), StatusCode.BAD_REQUEST.getCode(),
                "Invalid password should return 400");
    }
}