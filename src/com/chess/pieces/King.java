package com.chess.pieces;

import com.chess.boardgame.Board;
import com.chess.entities.ChessPiece;
import com.chess.enums.Color;

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
