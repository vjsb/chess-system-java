package pieces;

import boardgame.Board;
import entities.ChessPiece;
import enums.Color;

//Torre
public class Rook extends ChessPiece{

	public Rook(Board board, Color color) {
		super(board, color);
		
	}

	@Override
	public String toString() {
		return "R";
	}
	
}
