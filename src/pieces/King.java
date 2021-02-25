package pieces;

import boardgame.Board;
import entities.ChessPiece;
import enums.Color;

//Rei
public class King extends ChessPiece {

	public King(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "K";
	}
	
}
