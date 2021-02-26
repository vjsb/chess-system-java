package pieces;

import boardgame.Board;
import boardgame.Position;
import entities.ChessPiece;
import enums.Color;

//Torre
public class Rook extends ChessPiece{

	public Rook(Board board, Color color) {
		super(board, color);
		
	}

	@Override
	public String toString() {
		return "R";
	}
	
	@Override
	public boolean[][] possibleMoves() {
		//por enquanto sempre que chamar o movimento de um rei retornara todas as posições como false, como se ele estivesse preso
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		//variavel auxiliar
		Position p = new Position(0, 0);
		
		//acima, como esta analisando acima iremos pegar a posição da peça e fazer um -1 que é para subir
		p.setValues(position.getRow() - 1, position.getColumn());
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { //enquanto a posição p existir e não tiver uma peça la retorna true
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() - 1);
		}	
		//se haver uma peça do oponente a frente tambem sera uma posição valida
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//a esquerda, mesma logica acima porem mudando de linhas para colunas
		p.setValues(position.getRow(), position.getColumn() - 1);
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() - 1);
		}	
		
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//direita
		p.setValues(position.getRow(), position.getColumn() + 1);
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() + 1);
		}	
		
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//para baixo
		p.setValues(position.getRow() + 1, position.getColumn());
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1);
		}	
		
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		return mat;
	}
	
	
	
}
