package book.games.boundary;

import book.games.entity.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GamesTest {

    private static final long GAME_ID = 123L;
    final Games games = new Games();
    private Game game;

    @Mock
    EntityManager entityManager;

    @Before
    public void setup() {
        game = new Game();
        game.setId(GAME_ID);
        game.setTitle("Zelda");
    }

    @Test
    public void shouldCreateAGame() {
        when(entityManager.merge(game)).thenReturn(game);
        games.em = entityManager;

        games.create(game);

        verify(entityManager).merge(game);
    }

    @Test
    public void shouldFindAGameById() {
        when(entityManager.find(Game.class, GAME_ID)).thenReturn(game);
        games.em = entityManager;

        final Optional<Game> foundGame = games.findGameById(GAME_ID);

        verify(entityManager).find(Game.class, GAME_ID);
        assertThat(foundGame).isNotNull().hasValue(game).usingFieldByFieldValueComparator();
    }

    @Test
    public void shouldReturnAnEmptyOptionalIfElementNotFound() {
        when(entityManager.find(Game.class, GAME_ID)).thenReturn(null);
        games.em = entityManager;

        final Optional<Game> foundGame = games.findGameById(GAME_ID);

        verify(entityManager).find(Game.class, GAME_ID);
        assertThat(foundGame).isNotPresent();
    }
}
