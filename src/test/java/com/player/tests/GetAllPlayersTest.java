package com.player.tests;

import com.player.models.PlayerDto;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Player Controller")
@Feature("Get All Players")
public class GetAllPlayersTest extends BaseTest {

    @Test
    @Story("Positive: Created player appears in list")
    @Severity(SeverityLevel.NORMAL)
    @Description("After creating a player, it should appear in the get all players list")
    public void testCreatedPlayerAppearsInList() {
        PlayerDto created = createTestPlayer();

        List<Long> playerIds = playerSteps.getAllPlayerIds();

        Assert.assertTrue(playerIds.contains(created.getId()),
                "Created player should appear in the list");
    }

    @Test
    @Story("Positive: Deleted player disappears from list")
    @Severity(SeverityLevel.NORMAL)
    @Description("After deleting a player, it should no longer appear in the get all players list")
    public void testDeletedPlayerDisappearsFromList() {
        PlayerDto created = createTestPlayer();

        playerSteps.deletePlayer(SUPERVISOR, created.getId());

        List<Long> playerIds = playerSteps.getAllPlayerIds();

        Assert.assertFalse(playerIds.contains(created.getId()),
                "Deleted player should not appear in the list");
    }
}