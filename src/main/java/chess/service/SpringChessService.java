package chess.service;

import chess.dao.SpringBoardDao;
import chess.domain.Side;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.PositionDTO;
import chess.dto.ResponseDTO;
import chess.exception.ChessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class SpringChessService {
    private static final String GAME_NUMBER = "1";
    private static final String SUCCEED_CODE = "200";
    private static final String GAME_SET_CODE = "300";
    private static final String FAIL_CODE = "400";
    private static final String WHITE = "W";
    private static final String BLACK = "B";
    private static final String BLANK = ".";

    private SpringBoardDao springBoardDao;

    public SpringChessService(SpringBoardDao springBoardDao) {
        this.springBoardDao = springBoardDao;
    }

    public Map<String, String> currentBoardByRoomName(String roomName) {
        Map<Position, Piece> board = springBoardDao.initBoardTable(roomName);
        Map<String, String> boardName = new LinkedHashMap<>();
        for (Position position : board.keySet()) {
            String positionName = position.positionName();
            boardName.put(positionName, pieceToName(board.get(position)));
        }
        return boardName;
    }

    public ResponseDTO move(PositionDTO positionDTO, String roomName) {
        Board board = new Board(springBoardDao.findBoard(GAME_NUMBER));
        try {
            return moveExecute(positionDTO, board, roomName);
        } catch (ChessException e) {
            return new ResponseDTO(FAIL_CODE, e.getMessage(), currentTurn(roomName).name());
        }
    }

    private ResponseDTO moveExecute(PositionDTO positionDTO, Board board, String roomName) {
        board.move(Position.from(positionDTO.from()), Position.from(positionDTO.to()), currentTurn(roomName));
        springBoardDao.updateBoard(board, currentTurn(roomName).changeTurn().name(), roomName);
        if (board.isGameSet()) {
            Side side = board.winner();
            return new ResponseDTO(GAME_SET_CODE, side.name(), "게임 종료(" + side.name() + " 승리)");
        }
        return new ResponseDTO(SUCCEED_CODE, "Succeed", currentTurn(roomName).name());
    }

    public void newBoard(String roomName) {
        springBoardDao.updateBoard(Board.getGamingBoard(), "WHITE", roomName);
    }

    private String pieceToName(Piece piece) {
        if (piece.side() == Side.WHITE) {
            return WHITE + piece.getInitial().toUpperCase();
        }
        if (piece.side() == Side.BLACK) {
            return BLACK + piece.getInitial().toUpperCase();
        }
        return BLANK;
    }

    public String turnName(String roomName) {
        return currentTurn(roomName).name();
    }

    private Side currentTurn(String roomName) {
        return springBoardDao.findTurn(roomName);
    }
}
