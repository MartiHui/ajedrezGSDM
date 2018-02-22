package ajedrez;

import java.util.LinkedList;
import java.util.Scanner;

public class Controles {
	public Tablero t;
	private boolean isWhiteTurn;
	private Scanner sc;
	
	public Controles(Tablero t) {
		this.t= t;
		this.isWhiteTurn = true;
	}
	
	public int[] elegirPieza() {
		int[] coor = new int[2];
		String input;
		boolean piezaValida = false;
		
		System.out.println("Coordenadas de la pieza. Primero letra y luego numero (ej.:A1):");
		do {
			try {
				input = sc.nextLine();
				if (input.length() != 2) throw new Exception("Hay que introducir dos valores.");
				coor[0] = Character.getNumericValue(input.charAt(0)) - 65;
				coor[1] = Character.getNumericValue(input.charAt(1)) - 49;
				if (0 > coor[0] || coor[0] > 7 || 0 > coor[1] || coor[1] > 7) 
					throw new Exception("Fuera de rango.");
				if (this.t.tablero[coor[0]][coor[1]].isWhite != isWhiteTurn)
					throw new Exception("No es una ficha de tu color.");
				piezaValida = true;
			} catch (Exception e) {
				System.out.println("Vuelve a introducir las coordenadas:");
			}
		} while (!piezaValida);
		
		return coor;
	}

//	public int[][] showPosiblesMovimientos(int[] pieza) {
//		LinkedList<int[]> m = new LinkedList<int[]>();
//		Tablero mTablero = new Tablero("", "");
//		mTablero.clonarTablero(t);
//		
//		if (mTablero.tablero[pieza[0]][pieza[1]].limitedMove) {
//			for (int[] movs: mTablero.tablero[pieza[0]][pieza[1]].allowedMove()) {
//				if (pieza[0]+movs[0] >= 0 && pieza[0]+movs[0] <= 7 
//						&& pieza[1]+movs[1] >= 0 && pieza[1]+movs[1] <= 7) {
//					if (mTablero.tablero[pieza[0]+movs[0]][pieza[1]+movs[1]] == null
//							|| mTablero.tablero[pieza[0]+movs[0]][pieza[1]+movs[1]].isWhite == !isWhiteTurn) {
//						
//					}
//				}
//			}
//		}
//	}
	
	public void moverPieza() {
		int[] piezaOrigen = elegirPieza();
	}
	
//	public boolean isCheckMate() {
//		
//	}
}
