package book.games.boundary;

import book.games.control.GamesService;
import book.games.entity.SearchResult;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.json.Json;
import javax.json.JsonArray;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GamesResourceTest {

    @Mock
    GamesService gamesService;

    @Mock
    ExecutorServiceProducer executorServiceProducer;

    @Mock
    AsyncResponse asyncResponse;

    @Captor
    ArgumentCaptor<Response> argumentCaptorResponse;

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Before
    public void setupExecutorServiceProducer() {
        when(executorServiceProducer.getManagedExecutorService()).thenReturn(executorService);
    }

    @AfterClass
    public static void stopExecutorService() {
        executorService.shutdown();
    }

    @Test
    public void restAPIShouldSearchGamesByTheirNames() throws IOException, InterruptedException {
        final GamesResource gamesResource = new GamesResource();
        gamesResource.managedExecutorService = executorServiceProducer;
        gamesResource.gamesService = gamesService;

        when(gamesService.searchGames("zelda")).thenReturn(getSearchResults());

        gamesResource.searchGames(asyncResponse, "zelda");
        executorService.awaitTermination(2, TimeUnit.SECONDS);

        verify(asyncResponse).resume(argumentCaptorResponse.capture());

        final Response response = argumentCaptorResponse.getValue();

        assertThat(response.getStatusInfo().getFamily())
            .isEqualTo(Response.Status.Family.SUCCESSFUL);

        assertThat((JsonArray) response.getEntity())
            .hasSize(2)
            .containsExactlyInAnyOrder(
                Json.createObjectBuilder()
                    .add("id", 1)
                    .add("name", "The Legend Of Zelda")
                    .build(),
                Json.createObjectBuilder()
                    .add("id", 2)
                    .add("name", "Zelda II: The Adventure of Link")
                    .build()
            );
    }

    @Test
    public void exceptionShouldBePropagatedToCaller() throws IOException, InterruptedException {
        final GamesResource gamesResource = new GamesResource();
        gamesResource.managedExecutorService = executorServiceProducer;
        gamesResource.gamesService = gamesService;

        when(gamesService.searchGames("zelda")).thenThrow(IOException.class);

        gamesResource.searchGames(asyncResponse, "zelda");
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        verify(gamesService).searchGames("zelda");
        verify(asyncResponse).resume(any(IOException.class));
    }

    private List<SearchResult> getSearchResults() {
        final List<SearchResult> searchResults = new ArrayList<>();

        searchResults.add(new SearchResult(1, "The Legend Of Zelda"));
        searchResults.add(new SearchResult(2, "Zelda II: The Adventure of Link"));

        return searchResults;
    }
}
