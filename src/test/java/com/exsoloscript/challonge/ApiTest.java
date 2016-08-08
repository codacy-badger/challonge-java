package com.exsoloscript.challonge;

import com.exsoloscript.challonge.handler.sync.TournamentHandler;
import com.exsoloscript.challonge.model.Tournament;
import com.exsoloscript.challonge.model.query.TournamentQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static junit.framework.TestCase.assertTrue;

public class ApiTest {

    private Challonge challonge;

    @Before
    public void setUp() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File("src/test/resources/user.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.challonge = ChallongeApi.getFor(properties.getProperty("username"), properties.getProperty("api-key"));
    }

    @Test
    public void getTournamentsTest() throws IOException {
        TournamentHandler handler = new TournamentHandler(this.challonge.sync().tournaments());
        List<Tournament> tournaments = handler.getTournaments(Tournament.TournamentState.ALL, Tournament.TournamentType.DOUBLE_ELIMINATION, null, null, null);
        assertTrue(tournaments != null);
    }

    @After
    public void tearDown() {

    }
}