package chess.dao;

import chess.dto.CommandDto;
import chess.dto.MoveRequestDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SpringChessLogDao implements ChessRepository {
    private SimpleJdbcInsert simpleJdbcInsert;
    private JdbcTemplate jdbcTemplate;

    public SpringChessLogDao(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("chessGame")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long add(MoveRequestDto moveRequestDto) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(moveRequestDto);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    @Override
    public List<CommandDto> find(String id) {
        String query = "select target, destination from chessgame where room_id = ? ORDER BY command_date ASC;";
        return jdbcTemplate.query(
                query,
                (resultSet, rowNum) -> {
                    CommandDto commandDto = new CommandDto(
                            id,
                            resultSet.getString("target"),
                            resultSet.getString("destination")
                    );
                    return commandDto;
                }, id);
    }

    @Override
    public void delete(String roomId) {
        String query = "DELETE FROM chessgame WHERE room_id = ?";
        this.jdbcTemplate.update(query, roomId);
    }

    @Override
    public String findRoomById(String id) {
        String query = "select room_id from chessroom where room_id = ?";
        return jdbcTemplate.queryForObject(query, String.class, id);
    }
}