package com.backendwebsite.DatabaseBuilder.Step.FetchPlayers;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GetPlayersFromCouchDBStepTest {

/*
    @MockitoBean
    private CouchDBClient couchDBClient;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private GetPlayersFromCouchDBStep step;

    private String urn = "/players/_all_docs?include_docs=true";


    @Test
    public void whenBodyMissingRows_thenAddsFailedLog() throws Exception {
        JsonNode body = mapper.readTree("{}");
        CouchDBClient.Response response = new CouchDBClient.Response(StepsOrder.RequestStatus.SUCCESSFUL, body,
                "OK");
        Mockito.when(couchDBClient.sendGet(urn)).thenReturn(response);

        FetchPlayersContext context = new FetchPlayersContext(FetchPlayersContext.Region.eun1, "PLATINUM",
                "II", "RANKED_SOLO_5x5", "1");

        step.execute(context);

        assertNotNull(context.logs);
        assertEquals(1, context.logs.size(), "Expected one log entry when 'rows' missing");
        StepLog log = context.logs.get(0);
        assertEquals(StepsOrder.RequestStatus.FAILED, log.requestStatus());
        assertTrue(log.message().contains("missing 'rows'"));
    }

    @Test
    public void whenRowHasNullDoc_thenAddsFailedLogAndSkips() throws Exception {
        String json = "{\"rows\":[{\"doc\":null}]}";
        JsonNode body = mapper.readTree(json);
        CouchDBClient.Response response = new CouchDBClient.Response(StepsOrder.RequestStatus.SUCCESSFUL, body, "OK");
        Mockito.when(couchDBClient.sendGet(urn)).thenReturn(response);

        FetchPlayersContext context = new FetchPlayersContext(FetchPlayersContext.Region.eun1, "PLATINUM",
                "II", "RANKED_SOLO_5x5", "1");

        step.execute(context);

        assertEquals(1, context.logs.size(), "Expected one failed log for null doc");
        assertEquals(0, context.existingPlayers.size(), "No players should be added when doc is null");
        StepLog log = context.logs.get(0);
        assertEquals(StepsOrder.RequestStatus.FAILED, log.requestStatus());
        assertTrue(log.message().contains("Docs empty"));
    }

    @Test
    public void whenRowsContainPlayer_thenAddsPlayerAndSuccessLog() throws Exception {
        String docJson = "{"
                + "\"_id\":\"player:eun1:--xq-8z-qkIxoRUSquEo70Qm4beV0ejPa5mOi4x9x1nL97gZVq6A42smHcBOThiPnQiQ207Y4FnK8Q\","
                + "\"_rev\":\" 2-2184a13b74ae1cc40c386fb59e6b1e27\","
                + "\"queueType\":\"RANKED_SOLO_5x5\","
                + "\"tier\":\"GOLD\","
                + "\"rank\":\"II\","
                + "\"puuid\":\"--xq-8z-qkIxoRUSquEo70Qm4beV0ejPa5mOi4x9x1nL97gZVq6A42smHcBOThiPnQiQ207Y4FnK8Q\","
                + "\"leaguePoints\":100,"
                + "\"wins\":10,"
                + "\"losses\":5,"
                + "\"veteran\":false,"
                + "\"inactive\":false,"
                + "\"freshBlood\":false,"
                + "\"hotStreak\":false,"
                + "\"region\":\"eun1\""
                + "}";

        String json = "{\"rows\":[{\"doc\":" + docJson + "}]}";
        JsonNode body = mapper.readTree(json);
        CouchDBClient.Response response = new CouchDBClient.Response(StepsOrder.RequestStatus.SUCCESSFUL, body, "OK");
        Mockito.when(couchDBClient.sendGet(urn)).thenReturn(response);

        FetchPlayersContext context = new FetchPlayersContext(FetchPlayersContext.Region.eun1, "GOLD", "II",
                "RANKED_SOLO_5x5", "1");

        step.execute(context);

        assertEquals(1, context.existingPlayers.size(), "Expected one player to be added");
        assertEquals(1, context.logs.size(), "Expected one log entry for the fetched player");
        StepLog log = context.logs.get(0);
        assertEquals(StepsOrder.RequestStatus.SUCCESSFUL, log.requestStatus());
        assertTrue(log.message().contains("Fetched player ID: player:eun1:--xq-8z-qkIxoRUSquEo70Qm4beV0ejPa5mOi4x9x1nL97gZVq6A42smHcBOThiPnQiQ207Y4FnK8Q"));

        // verify player fields
        assertEquals("player:eun1:--xq-8z-qkIxoRUSquEo70Qm4beV0ejPa5mOi4x9x1nL97gZVq6A42smHcBOThiPnQiQ207Y4FnK8Q"
                , context.existingPlayers.get(0)._id);
        assertEquals("--xq-8z-qkIxoRUSquEo70Qm4beV0ejPa5mOi4x9x1nL97gZVq6A42smHcBOThiPnQiQ207Y4FnK8Q"
                , context.existingPlayers.get(0).puuid);
    }*/
}
