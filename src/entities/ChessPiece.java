package entities;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import enums.Color;

public abstract class ChessPiece extends Piece {

	private Color color;
	private int moveCont;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public int getMoveCont() {
		return moveCont;
	}
	
	public void increaseMoveCount() {
		moveCont++;
	}
	
	public void decreaseMoveCount() {
		moveCont--;
	}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	
	//verifica se ha uma peça adversária na posição passada
	protected boolean isThereOpponentPiece (Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p.getColor() != color; //se o p é diferente de nulo e diferente da minha cor de peça
	}
	
}
