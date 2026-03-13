package com.player.tests;

import com.player.data.TestDataHelper;
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
        PlayerDto request = TestDataHelper.validPreparedPlayer();
        PlayerDto created = createSteps.createPlayer(SUPERVISOR, request);
        trackPlayerForCleanup(created.getId());

        List<Long> playerIds = getSteps.getAllPlayerIds();

        Assert.assertTrue(playerIds.contains(created.getId()),
                "Created player should appear in the list");
    }

    @Test
    @Story("Positive: Deleted player disappears from list")
    @Severity(SeverityLevel.NORMAL)
    @Description("After deleting a player, it should no longer appear in the get all players list")
    public void testDeletedPlayerDisappearsFromList() {
        PlayerDto request = TestDataHelper.validPreparedPlayer();
        PlayerDto created = createSteps.createPlayer(SUPERVISOR, request);

        deleteSteps.deleteAndVerify(SUPERVISOR, created.getId());

        List<Long> playerIds = getSteps.getAllPlayerIds();

        Assert.assertFalse(playerIds.contains(created.getId()),
                "Deleted player should not appear in the list");
    }
}