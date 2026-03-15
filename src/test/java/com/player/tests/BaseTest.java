package com.player.tests;

import com.player.api.CreatePlayerApi;
import com.player.api.DeletePlayerApi;
import com.player.api.GetPlayerApi;
import com.player.api.UpdatePlayerApi;
import com.player.config.AppConfig;
import com.player.data.TestDataHelper;
import com.player.models.PlayerDto;
import com.player.steps.CreatePlayerSteps;
import com.player.steps.DeletePlayerSteps;
import com.player.steps.GetPlayerSteps;
import com.player.steps.UpdatePlayerSteps;
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

    protected CreatePlayerSteps createSteps;
    protected GetPlayerSteps getSteps;
    protected UpdatePlayerSteps updateSteps;
    protected DeletePlayerSteps deleteSteps;

    @BeforeClass
    public void setUp() {
        createSteps = new CreatePlayerSteps(new CreatePlayerApi());
        getSteps = new GetPlayerSteps(new GetPlayerApi());
        updateSteps = new UpdatePlayerSteps(new UpdatePlayerApi());
        deleteSteps = new DeletePlayerSteps(new DeletePlayerApi());
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        for (Long id : createdPlayerIds) {
            deleteSteps.deleteSafely(SUPERVISOR, id);
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

    protected void trackPlayerForCleanup(Long playerId) {
        if (playerId != null) {
            createdPlayerIds.add(playerId);
        }
    }

    protected PlayerDto createTestPlayer() {
        PlayerDto request = TestDataHelper.validPreparedPlayer();
        PlayerDto created = createSteps.createPlayer(SUPERVISOR, request);
        trackPlayerForCleanup(created.getId());
        return created;
    }
}