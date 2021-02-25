package com.chess.application;

import com.chess.entities.ChessMatch;

public class App {

	public static void main(String[] args) {
		
		ChessMatch chessMatch = new ChessMatch();
		UI.printBoard(chessMatch.getPieces());
	}

}
