package entities;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import enums.Color;
import exceptions.ChessException;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;

public class ChessMatch {

	private int turn;

	private Color currentPlayer;

	private Board board;

	private boolean checkMate;
	
	private ChessPiece enPassantVulnerable;
	
	private ChessPiece promoted;

	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	private boolean check;

	// construtor que ira gerar o jogo
	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		check = false;
		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
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

	// serve para mostrar os possiveis movimentos a partir da origem da peça
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {

		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();

	}

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {

		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);

		// se esse check for verdadeiro ele mesmo se colocou então o movimento deverá
		// ser desfeito
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}

		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		// #specialmove promotion
		promoted = null;
		if (movedPiece instanceof Pawn) {
			if((movedPiece.getColor() == Color.WHITE && target.getRow() == 0 || movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q");
			}
		}
		
		// se isso for veradde irei dizer que a partida esta em check, ao contrario não
		// estara
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		// se a jogada que eu fiz deixou o jogo em chequemate então tera que acabar
		if (testeCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		} else {
			nextTurn();
		}
		
		// #specialmove en passant
		//se a peça escolhida for um peão e a diferença for 2 seja para mais ou para menos, significa que foi o movimento inicial do peão de duas casas
		if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
			enPassantVulnerable = movedPiece;
		}
		else {
			enPassantVulnerable = null;
		}
		
		return (ChessPiece) capturedPiece;

	}

	// lógica para desfazer um movimento
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target);// tira a peça que voce moveu no destino
		p.decreaseMoveCount();
		board.placePiece(p, source);// devolve para a posição de origem

		// se caso tiver capturado alguma peça devera retornar ela para o antigo lugar
		// de destino
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}

		if (p instanceof King && target.getColumn() == source.getColumn() + 2) { 
																					
																					
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();;

		}
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) { 
																					
																					
																					
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();

		}
		
		// #specialmone en passant
				if (p instanceof Pawn) {
					if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
						ChessPiece pawn = (ChessPiece)board.removePiece(target);
						Position pawnPosition;
						if (p.getColor() == Color.WHITE) {
							pawnPosition = new Position(3, target.getColumn());
						}
						else {
							pawnPosition = new Position(4, target.getColumn());
						}
						board.placePiece(pawn, pawnPosition);
					}
				}

	}

	public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null) {
			throw new IllegalStateException("There is no piece to be promoted");
		}
		
		if (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
			throw new InvalidParameterException("Inavalid type for promotion");
		}
		
		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		
		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
		
	}
	
	private ChessPiece newPiece(String type, Color color) {
		if (type.equals("B")) return new Bishop(board, color);
		if (type.equals("N")) return new Knight(board, color);
		if (type.equals("Q")) return new Queen(board, color);
		return new Rook(board, color);
	}
	
	// lógica de realizar o movimento
	private Piece makeMove(Position source, Position target) {

		ChessPiece p = (ChessPiece) board.removePiece(source);// remove a peça da posição de origem
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);// remove uma possivel peça da posição de destino
		board.placePiece(p, target);// coloca a peça que tava na origem na posição de destino

		// sempre que fizer o movimento e esse movimento capturar ma peça eu a retira de
		// uma lista e coloco em outra
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}

		// specialmove castling pequeno duas casa a direita
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) { // posição de origem + 2 significa que
																					// o rei andou duas casas para
																					// dreita e caiu no castling pequeno
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();

		}
		// specialmove castling grande tres casas a esquerda
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) { // posição de origem + 2 significa que
																					// o rei andou tres casas para
																					// esquerda e caiu no castling
																					// grande
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();

		}
		
		// #specialmone en passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				}
				else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}

		return capturedPiece;

	}

	// validação da posição de origem
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		// validação para caso o jogador tente mover uma peça adversaria
		if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
			throw new ChessException("There chosen piece is not yours");
		}
		// para validar se existe movmentos possiveis para a atual peça
		if (!board.piece(position).isThereAnyPossibleMovie()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}

	public void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}

	private void nextTurn() {
		turn++;
		// se o jogador atual for o white então agora ele vai ser black, se não vai ser
		// white
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	// procura na lista de peças em jogo qual que é o rei dessa cor
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) { // se essa peça for uma instancia de rei significa que encontrei
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}

	/*
	 * esse método ira percorrer todas as peças do tabuleiro e para cada uma tera
	 * que testar os movimentos adversários para ver se algum movimento possivel cai
	 * na casa do rei, se isso acontecer o rei estara em check
	 */
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition(); // pegar a posição do rei na matriz
		List<Piece> opponentPieces = piecesOnTheBoard.stream()
				.filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}

	// metodo para testar se esta em cheque mate, que seria se o rei não possuir
	// mais movimentos nem para escapar
	private boolean testeCheckMate(Color color) {

		// se essa cor não estiver em cheque é impossivel estar em chequemate
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = ((ChessPiece) p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}

		return true;

	}

	// esse método ira receber as coordenadas do xadrez
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}

	public void initialSetup() {

		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
	}
}
