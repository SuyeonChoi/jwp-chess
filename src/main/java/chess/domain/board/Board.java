package chess.domain.board;

import java.util.Collections;
import java.util.Map;

import chess.domain.piece.Color;
import chess.domain.piece.EmptyPiece;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.exception.AllyPiecePositionException;
import chess.exception.SourcePositionException;

public class Board {

    public static final int CHESS_BOARD_SIZE = 8;
    public static final int PAWN_ALLY_COUNT_CONDITION = 2;
    private Map<Position, Piece> board;

    public Board() {
        board = InitialBoard.emptyBoard();
    }

    public Board(Map<Position, Piece> board) {
        this.board = board;
    }

    public void initChessPieces() {
        board = InitialBoard.emptyBoard();
        InitialBoard.initChessPieces(board);
    }

    public Map<Position, Piece> getPieces() {
        return Collections.unmodifiableMap(board);
    }

    public Piece piece(Position position) {
        return board.getOrDefault(position, new EmptyPiece());
    }

    public void move(Position source, Position target) {
        validateMove(source, target);
        Piece piece = piece(source);
        piece.validateMove(Collections.unmodifiableMap(board), source, target);
        put(source, new EmptyPiece());
        put(target, piece);
    }

    public void validateMove(Position source, Position target) {
        if (piece(source).isEmpty()) {
            throw new SourcePositionException();
        }
        if (piece(source).isSameColor(piece(target))) {
            throw new AllyPiecePositionException();
        }
    }

    public void put(Position position, Piece piece) {
        board.put(position, piece);
    }

    public Score piecesScore(Color color) {
        BoardResult boardResult = new BoardResult(board);
        return boardResult.piecesScore(color);
    }

    public boolean isKingAlive(Color color) {
        BoardResult boardResult = new BoardResult(board);
        return boardResult.isKingAlive(color);
    }
}