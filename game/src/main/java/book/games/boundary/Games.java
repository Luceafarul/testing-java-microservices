package book.games.boundary;


import book.games.entity.Game;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Stateless
public class Games {

    @PersistenceContext
    EntityManager em;

    public Long create(final Game request) {
        final Game game = em.merge(request);
        return game.getId();
    }

    public Optional<Game> findGameById(final Long gameId) {
        Optional<Game> g = Optional.ofNullable(em.find(Game.class, gameId));

        if (g.isPresent()) {
            //Force load of lazy collections before detach
            Game game = g.get();
            game.getReleaseDates().size();
            game.getPublishers().size();
            game.getDevelopers().size();
            em.detach(game);
        }

        return g;
    }

}