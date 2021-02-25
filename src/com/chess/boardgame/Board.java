package com.chess.boardgame;

import com.chess.Exceptions.BoardException;

public class Board {

	private Integer rows;
	private Integer columns;
	private Piece[][] pieces;
	
	/*
	 * A matriz de peças sera instanciada com piece na quantidade de linhas e colunas informadas
	 */
	
	public Board(Integer rows, Integer columns) {
		if (rows < 1 || columns < 1) {
			throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
				
	}

	public Integer getRows() {
		return rows;
	}

	public Integer getColumns() {
		return columns;
	}

	//retorna a matriz pieces na linha row e na coluna column
	public Piece piece(Integer row, Integer column) {
		if (!positionExists(row, column)) {
			throw new BoardException("Position not on the board");
		}
		return pieces[row][column];
	}
	
	//recebe a peça pela posição
	public Piece piece(Position position) {
		//se a posição que deseja inserir não existe
		if (!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	//método para por a peça em uma posição
	public void placePiece(Piece piece, Position position) {
		//se existir uma peça ja na mesma posição
		if (thereIsAPiece(position)) {
			throw new BoardException("There is already a piece on position " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position; //coloca a posição recebendo posição ao invés de receber nula igual antes
	}
	
	//metodo auxiliar, para ver se a posição existe medindo as medidas (rows, columns) do tabuleiro inteiro
	private boolean positionExists(Integer row, Integer column) {
		return row >= 0 && row < rows && column >= 0 && column < columns;
	}
	
	//pega o metodo auxiliar e esta se uma posição existe
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	//metodo para ver se ja existe uma peça
	public boolean thereIsAPiece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException("There is already a piece on position " + position);
		}
		return piece(position) != null;
	}
	
	
	
	
}
	
	