package entities;

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
import pieces.Rook;

public class ChessMatch {

	private int turn;
	
	private Color currentPlayer;
	
	private Board board;
	
	private boolean checkMate;
	
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
	

	/*
	 * Os fors do m�todo ChessPiece v�o percorrer a matriz de pe�as do tabuleiro e
	 * para cada pe�a do tabuleiro vai fazer um downcasting para ChessPiece
	 */

	// retorna a matrix de pe�as da partida de xadrez
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
	//serve para mostrar os possiveis movimentos a partir da origem da pe�a
	public boolean [][] possibleMoves(ChessPosition sourcePosition){
		
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
		
		//se esse check for verdadeiro ele mesmo se colocou ent�o o movimento dever� ser desfeito
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		//se isso for veradde irei dizer que a partida esta em check, ao contrario n�o estara
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		//se a jogada que eu fiz deixou o jogo em chequemate ent�o tera que acabar
		if (testeCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
		return (ChessPiece) capturedPiece;
		
	}
	
	//l�gica para desfazer um movimento
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);//tira a pe�a que voce moveu no destino
		p.decreaseMoveCount();
		board.placePiece(p, source);//devolve para a posi��o de origem
		
		//se caso tiver capturado alguma pe�a devera retornar ela para o antigo lugar de destino
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}
	
	//l�gica de realizar o movimento
	private Piece makeMove(Position source, Position target) {
		
		ChessPiece p = (ChessPiece)board.removePiece(source);//remove a pe�a da posi��o de origem
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);//remove uma possivel pe�a da posi��o de destino
		board.placePiece(p, target);//coloca a pe�a que tava na origem na posi��o de destino
		
		
		//sempre que fizer o movimento e esse movimento capturar ma pe�a eu a retira de uma lista e coloco em outra
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		return capturedPiece;
		
	}
	
	//valida��o da posi��o de origem
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		//valida��o para caso o jogador tente mover uma pe�a adversaria
		if (currentPlayer !=((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("There chosen piece is not yours");
		}
		//para validar se existe movmentos possiveis para a atual pe�a
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
		turn ++;
		//se o jogador atual for o white ent�o agora ele vai ser black, se n�o vai ser white
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	//procura na lista de pe�as em jogo qual que � o rei dessa cor
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() ==  color).collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) { //se essa pe�a for uma instancia de rei significa que encontrei
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}
	
	
	/*
	 * esse m�todo ira percorrer todas as pe�as do tabuleiro e para cada uma tera que testar os movimentos
	 * advers�rios para ver se algum movimento possivel cai na casa do rei, se isso acontecer
	 * o rei estara em check
	 */
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition(); // pegar a posi��o do rei na matriz
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	//metodo para testar se esta em cheque mate, que seria se o rei n�o possuir mais movimentos nem para escapar
	private boolean testeCheckMate(Color color) {
		
		//se essa cor n�o estiver em cheque � impossivel estar em chequemate
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() ==  color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean [][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
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

	// esse m�todo ira receber as coordenadas do xadrez
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}

	public void initialSetup() {

		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
	}
}
