package com.player.tests;

import com.player.models.PlayerDto;
import com.player.models.StatusCode;

import java.util.List;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.SkipException;
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

        playerSteps.deletePlayer(editor, created.getId());

        Assert.assertFalse(playerSteps.playerExists(created.getId()),
                "Deleted player should not exist");
    }

    @Test
    @Story("BUG: Delete player with user role editor succeeds")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Player with 'user' role should not be able to delete other players.")
    @Issue("BUG-008")
    public void testDeletePlayerWithUserRoleEditor() {
        PlayerDto created = createTestPlayer();
        PlayerDto fetched = playerSteps.getPlayerById(created.getId());

        Response response = playerSteps.deleteExpectingAnyStatus(fetched.getLogin(), created.getId());

        Assert.assertEquals(response.statusCode(), StatusCode.FORBIDDEN.getCode(),
                "User role editor should get 403 Forbidden when deleting players");
    }

    @Test
    @Story("Negative: Supervisor cannot delete supervisor")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Supervisor should not be able to delete supervisor user")
    public void testSupervisorCannotDeleteSupervisor() {
        List<Long> allIds = playerSteps.getAllPlayerIds();
        PlayerDto supervisor = allIds.stream()
                .map(playerSteps::getPlayerById)
                .filter(p -> SUPERVISOR.equals(p.getLogin()))
                .findFirst()
                .orElseThrow(() -> new SkipException("Supervisor user not found in the system"));

        Response response = playerSteps.deleteExpectingAnyStatus(SUPERVISOR, supervisor.getId());

        Assert.assertEquals(response.statusCode(), StatusCode.FORBIDDEN.getCode(),
                "Supervisor should not be able to delete supervisor");
    }
}