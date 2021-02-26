package boardgame;

public abstract class Piece {

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
	
	//retorna true ou false para se houver movimentos
	public abstract boolean[][] possibleMoves();
	
	//implementa��o concreta que depende do m�todo abstrato
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];
	}
	
	public boolean isThereAnyPossibleMovie() {
		
		boolean[][] mat = possibleMoves();
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) {
				if (mat[i][j]) {//se essa senten�a for verdadeira existe um movimento possivel, retorna true
					return true;
				}
			}
		}
		return false;//se a varredura na matriz esgotar e n�o achar nenhum movimento retorna false
		
	}
	
}
