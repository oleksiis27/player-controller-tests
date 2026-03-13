package com.player.tests;

import com.player.data.TestDataHelper;
import com.player.models.PlayerDto;
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
@Feature("Delete Player")
public class DeletePlayerTest extends BaseTest {

    @DataProvider(name = "editors")
    public Object[][] editors() {
        return new Object[][] {
                { SUPERVISOR },
                { ADMIN }
        };
    }

    @Test(dataProvider = "editors")
    @Story("Positive: Delete player by editor")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Delete player using editor and verify player no longer exists")
    public void testDeletePlayerByEditor(String editor) {
        PlayerDto request = TestDataHelper.validPreparedPlayer();
        PlayerDto created = createSteps.createPlayer(SUPERVISOR, request);

        deleteSteps.deleteAndVerify(editor, created.getId());
    }

    @Test
    @Story("BUG: Delete player with user role editor succeeds")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Player with 'user' role should not be able to delete other players. Known BUG: allows deletion")
    @Issue("BUG-012")
    public void testDeletePlayerWithUserRoleEditor() {
        PlayerDto request = TestDataHelper.validPreparedPlayer();
        PlayerDto created = createSteps.createPlayer(SUPERVISOR, request);
        trackPlayerForCleanup(created.getId());

        PlayerDto fetched = getSteps.getPlayerById(created.getId());

        Response response = deleteSteps.deleteExpectingAnyStatus(fetched.getLogin(), created.getId());

        Assert.assertEquals(response.statusCode(), 403,
                "User role editor should get 403 Forbidden when deleting players");
    }
}