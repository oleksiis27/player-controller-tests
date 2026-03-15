package com.player.tests;

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
@Feature("Delete Player")
public class DeletePlayerTest extends BaseTest {

    @Test(dataProvider = "editors")
    @Story("Positive: Delete player by editor")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Delete player using editor and verify player no longer exists")
    public void testDeletePlayerByEditor(String editor) {
        PlayerDto created = createTestPlayer();

        deleteSteps.deletePlayer(editor, created.getId());

        Assert.assertFalse(getSteps.playerExists(created.getId()),
                "Deleted player should not exist");
    }

    @Test
    @Story("BUG: Delete player with user role editor succeeds")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Player with 'user' role should not be able to delete other players. Known BUG: allows deletion")
    @Issue("BUG-012")
    public void testDeletePlayerWithUserRoleEditor() {
        PlayerDto created = createTestPlayer();
        PlayerDto fetched = getSteps.getPlayerById(created.getId());

        Response response = deleteSteps.deleteExpectingAnyStatus(fetched.getLogin(), created.getId());

        Assert.assertEquals(response.statusCode(), StatusCode.FORBIDDEN.getCode(),
                "User role editor should get 403 Forbidden when deleting players");
    }
}