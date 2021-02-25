package com.chess.pieces;

import com.chess.boardgame.Board;
import com.chess.entities.ChessPiece;
import com.chess.enums.Color;

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
