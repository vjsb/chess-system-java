package com.chess.boardgame;

public class Board {

	private Integer rows;
	private Integer columns;
	private Piece[][] pieces;
	
	/*
	 * A matriz de pe�as sera instanciada com piece na quantidade de linhas e colunas informadas
	 */
	
	public Board(Integer rows, Integer columns) {
		
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
				
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getColumns() {
		return columns;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}
	
	
	
}
