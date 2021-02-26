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

	@Override
	public boolean[][] possibleMoves() {
		//por enquanto sempre que chamar o movimento de um rei retornara todas as posições como false, como se ele estivesse preso
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		return mat;
	}
	
}
