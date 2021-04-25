package chess.webdao;

import chess.webdto.dao.BoardInfosDto;
import chess.webdto.dao.TeamInfoDto;
import chess.webdto.dao.TurnDto;

import java.util.List;

public interface ChessDao {
    void deleteRoomByRoomId(int roomId);

    void deleteBoardByRoomId(int roomId);

    void changeTurnByRoomId(String turn, boolean isPlaying, int roomId);

    TurnDto selectTurnByRoomId(long roomId);

    long createRoom(String currentTurn, boolean isPlaying);

    void createBoard(TeamInfoDto teamInfoDto);

    List<BoardInfosDto> selectBoardInfosByRoomId(int i);
}