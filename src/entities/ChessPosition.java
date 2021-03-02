package entities;

import boardgame.Position;
import exceptions.BoardException;

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
	
	/*metodo vai retornar uma nova posi��o
	 * 
	 * a linha vai ser o 8 - a linha da posi��o do xadres
	 * coluna vai ser a coluna do xadrez - o caractere 'a'
	 * 
	 */
	protected Position toPosition() {
		return new Position(8 - row, column - 'a');
	}
	
	//esse m�todo retorna a formula inversa de toPosition
	//esse m�todo pega a posi��o da matriz e converte para posi��o de xadrez
	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char)('a' + position.getColumn()), 8 - position.getRow());
	}
	
	//no xadrez primeiro se posiciona a coluna e depois a linha
	@Override
	public String toString() {
		return "" + column + row;
	}
	
	
	
	
	
}
