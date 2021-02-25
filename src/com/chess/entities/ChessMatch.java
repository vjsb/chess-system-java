package com.chess.entities;

import com.chess.boardgame.Board;
import com.chess.boardgame.Position;
import com.chess.enums.Color;
import com.chess.pieces.King;
import com.chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	
	//construtor que ira gerar o jogo
	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
	}
	
	/*
	 * Os fors do m�todo ChessPiece
	 * v�o percorrer a matriz de pe�as do tabuleiro
	 * e para cada pe�a do tabuleiro vai fazer um downcasting para ChessPiece
	 */
	
	//retorna a matrix de pe�as da partida de xadrez
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j <board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
	public void initialSetup() {
		board.placePiece(new Rook(board, Color.WHITE), new Position(2, 1));
		board.placePiece(new King(board, Color.BLACK), new Position(0, 4));
		board.placePiece(new King(board, Color.WHITE), new Position(7, 4));

	}
}
