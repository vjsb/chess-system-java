package com.chess.entities;

import com.chess.Exceptions.BoardException;
import com.chess.boardgame.Position;

public class ChessPosition {

	private char column;
	private Integer row;
	
	public ChessPosition(char column, Integer row) {
	
		if (column < 'a' || column > 'h' || row < 1 || row > 8) {
			throw new BoardException("Error instantiating ChessPosition. Valid values are from a1 to h8");
		}
		
		this.column = column;
		this.row = row;
	}
	public char getColumn() {
		return column;
	}
	
	
	public Integer getRow() {
		return row;
	}
	
	/*metodo vai retornar uma nova posição
	 * 
	 * a linha vai ser o 8 - a linha da posição do xadres
	 * coluna vai ser a coluna do xadrez - o caractere 'a'
	 * 
	 */
	protected Position toPosition() {
		return new Position(8 - row, column - 'a');
	}
	
	//esse método retorna a formula inversa de toPosition
	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char)('a' - position.getColumn()), 8 - position.getRow());
	}
	
	//no xadrez primeiro se posiciona a coluna e depois a linha
	@Override
	public String toString() {
		return "" + column + row;
	}
	
	
	
	
	
}
