package com.player.tests;

import com.player.api.PlayerApiClient;
import com.player.config.AppConfig;
import com.player.data.TestDataHelper;
import com.player.models.PlayerDto;
import com.player.steps.PlayerSteps;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class BaseTest {

    protected static final AppConfig config = AppConfig.getInstance();
    protected static final String SUPERVISOR = config.defaultEditor();
    protected static final String ADMIN = config.adminEditor();

    private final List<Long> createdPlayerIds = new CopyOnWriteArrayList<>();

    protected PlayerSteps playerSteps;

    @BeforeClass
    public void setUp() {
        playerSteps = new PlayerSteps(new PlayerApiClient());
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        for (Long id : createdPlayerIds) {
            playerSteps.deleteSafely(SUPERVISOR, id);
        }
        createdPlayerIds.clear();
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
                { TestDataHelper.longPassword(), "too long" },
                { TestDataHelper.passwordWithoutDigits(), "no digits" },
                { TestDataHelper.passwordWithoutLetters(), "no letters" }
        };
    }

    @DataProvider(name = "invalidAges")
    public Object[][] invalidAges() {
        return new Object[][] {
                { 16, "below minimum (16)" },
                { 60, "above maximum (60)" }
        };
    }

    protected void trackPlayerForCleanup(Long playerId) {
        if (playerId != null) {
            createdPlayerIds.add(playerId);
        }
    }

    protected PlayerDto createTestPlayer() {
        PlayerDto request = TestDataHelper.validPreparedPlayer();
        PlayerDto created = playerSteps.createPlayer(SUPERVISOR, request);
        trackPlayerForCleanup(created.getId());
        return created;
    }
}