package com.player.tests;

import com.player.models.PlayerDto;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Player Controller")
@Feature("Get Player")
public class GetPlayerTest extends BaseTest {

    @Test
    @Story("BUG: Password returned in GET response")
    @Severity(SeverityLevel.CRITICAL)
    @Description("GET player should NOT return password — security vulnerability")
    @Issue("BUG-005")
    public void testGetPlayerShouldNotReturnPassword() {
        PlayerDto created = createTestPlayer();
        PlayerDto fetched = playerSteps.getPlayerById(created.getId());

        Assert.assertNull(fetched.getPassword(),
                "Password should NOT be returned in response.");
    }

    @Test
    @Story("Negative: Get player with non-existing ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Getting player with non-existing ID should return empty response")
    public void testGetNonExistingPlayer() {
        Assert.assertFalse(playerSteps.playerExists(999999999L),
                "Non-existing player should not exist");
    }
}