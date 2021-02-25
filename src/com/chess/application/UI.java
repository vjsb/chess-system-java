package com.chess.application;

import com.chess.entities.ChessPiece;

public class UI {

	//m�todo para imprimir o tabuleiro
	public static void printBoard(ChessPiece[][] pieces) {
		
		for (int i = 0; i < pieces.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j]);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
		
		
	}
	
	//m�todo para imprimir uma pe�a apenas
	public static void printPiece(ChessPiece piece) {
		
		/*se essa pessa for igual a nulo significa que n�o tinha pe�a nessa posi��o do tabuleiro
		 * ent�o vai imprimir um "-"
		 * caso contrario ira imprimir a pe�a (piece)
		 * espa�o em branco para que n�o fiquem grudadas umas nas outras
		 */
		if (piece == null) {
			System.out.print("-");
		}
		else {
			System.out.print(piece);
		}
		System.out.print(" ");
	}
	
}
