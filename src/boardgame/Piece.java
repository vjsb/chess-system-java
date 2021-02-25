package boardgame;

public class Piece {

	protected Position position;
	private Board board;

	/*
	 * a posi��o de uma pe�a recem-criada vai ser inicialmente como nula
	 *  dizendo assim que n�o foi colocada no tabuleiro ainda
	 *  por isso o construtor possui o position como null
	 */
	
	public Piece(Board board) {
		super();
		this.board = board;
		position = null;
	}

	protected Board getBoard() {
		return board;
	}
	
	
}
