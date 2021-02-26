package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import entities.ChessPiece;
import entities.ChessPosition;
import enums.Color;

public class UI {

	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
	//codigos especiais de cores para serem impressos no console
	//cores do texto
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	//cores do fundo
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
	
	//limpa a tela do gitBash
	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	
	public static ChessPosition readChessPosition(Scanner sc) {
		
		try {
			
			String s = sc.nextLine();//recebe uma String para posi��o coluna
			char column = s.charAt(0);//charAt(0) por conta de apenas primeiro caractere definir a coluna
			int row = Integer.parseInt(s.substring(1));//recortar o String a partir da posi��o 1 e transforma-lo em inteiro que � a linha
			return new ChessPosition(column, row);//retornara o comando digitado por exemplo a1
			
		} catch (Exception e) {
			throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8");
		}
	}

	// m�todo para imprimir o tabuleiro
	public static void printBoard(ChessPiece[][] pieces) {

		for (int i = 0; i < pieces.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], false);//false para que nenhuma pe�a tenha fundo colorido
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");

	}
	
	public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {

		for (int i = 0; i < pieces.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], possibleMoves[i][j]);//ira pintar o fundo colorido dependendo da variavel possibleMoves
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");

	}

	// m�todo para imprimir uma pe�a apenas
	public static void printPiece(ChessPiece piece, boolean background) {

		/*
		 * se essa pessa for igual a nulo significa que n�o tinha pe�a nessa posi��o do
		 * tabuleiro ent�o vai imprimir um "-" caso contrario ira imprimir a pe�a
		 * (piece) espa�o em branco para que n�o fiquem grudadas umas nas outras
		 * testara para ver se as pe�as s�o brancas ou pretas
		 * reproduzindo o branco e amarelo por conta do fundo ser preto
		 */
		if (background) {
			System.out.print(ANSI_BLUE_BACKGROUND);
		}
		
		if (piece == null) {
            System.out.print("-" + ANSI_RESET);
        }
        else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_WHITE + piece + ANSI_RESET);
            }
            else {
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
            }
        }
        System.out.print(" ");
	}

}
