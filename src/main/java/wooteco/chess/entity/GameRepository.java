package wooteco.chess.entity;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
	@Query("SELECT * FROM game WHERE can_continue = true")
	List<Game> findAvailableGames();
}