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
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Player Controller")
@Feature("Get Player")
public class GetPlayerTest extends BaseTest {

    @Test
    @Story("BUG: Password returned in GET response")
    @Severity(SeverityLevel.CRITICAL)
    @Description("GET player should NOT return password — security vulnerability")
    @Issue("BUG-006")
    public void testGetPlayerShouldNotReturnPassword() {
        PlayerDto request = TestDataHelper.validPreparedPlayer();
        PlayerDto created = createSteps.createPlayer(SUPERVISOR, request);
        trackPlayerForCleanup(created.getId());

        PlayerDto fetched = getSteps.getPlayerById(created.getId());

        Assert.assertNull(fetched.getPassword(),
                "Password should NOT be returned in response. Known BUG: password is exposed");
    }

    @Test
    @Story("Negative: Get player with non-existing ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Getting player with non-existing ID should return empty response or error status")
    public void testGetNonExistingPlayer() {
        Assert.assertTrue(getSteps.isPlayerAbsent(999999999L),
                "Non-existing player should return empty body");
    }
}