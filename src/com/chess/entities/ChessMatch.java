package com.chess.entities;

import com.chess.boardgame.Board;

public class ChessMatch {

	private Board board;
	
	public ChessMatch() {
		board = new Board(8, 8);
	}
	
	/*
	 * Os fors do método ChessPiece
	 * vão percorrer a matriz de peças do tabuleiro
	 * e para cada peça do tabuleiro vai fazer um downcasting para ChessPiece
	 */
	
	//retorna a matrix de peças da partida de xadrez
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j <board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
}
