package entities;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import enums.Color;
import exceptions.ChessException;
import pieces.King;
import pieces.Rook;

public class ChessMatch {

	private Board board;

	// construtor que ira gerar o jogo
	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
	}

	/*
	 * Os fors do método ChessPiece vão percorrer a matriz de peças do tabuleiro e
	 * para cada peça do tabuleiro vai fazer um downcasting para ChessPiece
	 */

	// retorna a matrix de peças da partida de xadrez
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		return (ChessPiece) capturedPiece;
		
	}
	
	//lógica de realizar o movimento
	private Piece makeMove(Position source, Position target) {
		
		Piece p = board.removePiece(source);//remove a peça da posição de origem
		Piece capturedPiece = board.removePiece(target);//remove uma possivel peça da posição de destino
		board.placePiece(p, target);//coloca a peça que tava na origem na posição de destino
		return capturedPiece;
		
	}
	
	//validação da posição de origem
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		
		//para validar se existe movmentos possiveis para a atual peça
		if (!board.piece(position).isThereAnyPossibleMovie()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	public void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}

	// esse método ira receber as coordenadas do xadrez
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}

	public void initialSetup() {

		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new King(board, Color.WHITE));

		placeNewPiece('c', 7, new Rook(board, Color.BLACK));
		placeNewPiece('c', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
