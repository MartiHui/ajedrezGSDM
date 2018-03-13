package ajedrez;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Scanner;

public class Controles implements Serializable{
	/**llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll
	 * 
	 */
	private static final long serialVersionUID = 1738660591554248778L;
	public Tablero tablero; // Cada posición contiene lo que haya en la casilla correspondiente
	public boolean isWhiteTurn; // Determina si actualmente es el turno de las blancas
	
	public Controles(Tablero t) {
		this.tablero = t;
		this.isWhiteTurn = true;
	}
	
	public void changeTurn() {
		this.isWhiteTurn = !this.isWhiteTurn;
	}
	
	public Coordenadas elegirPieza(Scanner sc, Movimiento mAnterior) {
		
		System.out.println("Elige una pieza:");
		
		do {
			Coordenadas origen = new Coordenadas(sc);
			
			if (origen.isEmpty()) return null;
			
			Piezas tempPieza = this.tablero.getCasilla(origen);
			
			if (tempPieza == null) {
				System.out.println("No hay ninguna pieza en esta casilla.");
				continue;
			} else if (tempPieza.isWhite != this.isWhiteTurn) {
				System.out.println("Esta pieza no te pertenece.");
				continue;
			} else if (tempPieza.legalMoves(this.tablero, mAnterior).length == 0) {
				System.out.println("Esta pieza no tiene movimientos permitidos.");
				continue;
			} else {
				return origen;
			}
		} while (true);
	}
	
	public Coordenadas elegirDestino(Piezas p, Scanner sc, Movimiento mAnterior) {
		
		
		this.tablero.printTablero(p.legalMoves(this.tablero, mAnterior), isWhiteTurn);
		System.out.println("Pieza elegida: " + p + " | Elige una casilla:");
		do {
			Coordenadas destino = new Coordenadas(sc);
			if (destino.isEmpty())
				return null;
			else if (!destino.insideOf(p.legalMoves(tablero, mAnterior)))
				System.out.println("No puedes moverte a esa casilla.");
			else
				return destino;
		} while (true);
	}
	
	public boolean moverPieza(Scanner sc, LinkedList<Movimiento> listaMovimientos) {
		do {
			Coordenadas org = this.elegirPieza(sc, listaMovimientos.getLast());
			if (org == null) return false;
			Coordenadas dst = this.elegirDestino(this.tablero.getCasilla(org), sc, listaMovimientos.getLast());
			if (dst == null) this.tablero.printTablero(isWhiteTurn);
			else {
				Movimiento mActual = new Movimiento(org, dst, this.tablero.getCasilla(org), this.tablero.getCasilla(dst));
				this.tablero.getCasilla(org).moverPieza(tablero, dst, mActual);
				listaMovimientos.add(mActual);
				return true;
			}
		} while (true);
	}
}
