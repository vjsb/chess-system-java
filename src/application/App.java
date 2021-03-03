package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import entities.ChessMatch;
import entities.ChessPiece;
import entities.ChessPosition;
import exceptions.ChessException;

public class App {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		ChessMatch chessMatch = new ChessMatch();
		
		List<ChessPiece> captured = new ArrayList<>();
		
		while (!chessMatch.getCheckMate()) {
			try {
				UI.clearScreen();
				UI.printMatch(chessMatch, captured);
				System.out.println();
				System.out.print("Source: ");//origem
				ChessPosition source = UI.readChessPosition(sc);
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), possibleMoves);
				
				System.out.println();
				System.out.print("Target: ");//destino
				ChessPosition target = UI.readChessPosition(sc);
				
				//sempre que executar o movimento e essa peça for capturada adiciona na lista captured.add(capturedPiece);
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
				
				//se a peça capturada for diferente de nulo significa que foi capturada e a adiciona a lista
				if (capturedPiece != null) {
					captured.add(capturedPiece);
				}
				//para ver se houve promoção de alguma peça
				if (chessMatch.getPromoted() != null) {
					System.out.print("Enter piece for promotion (B/N/R/Q): ");
					String type = sc.nextLine();
					chessMatch.replacePromotedPiece(type);
				}
				
			}
			catch (ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine(); //para o programa aguardar usuario apertar enter
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine(); 
			}
 			
		}
		
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
		
	}

}
