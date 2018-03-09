package ajedrez;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Scanner;

public class Controles implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9161167521844489559L;
	public Tablero tablero;
	public boolean isWhiteTurn;
	
	public Controles(Tablero t) {
		this.tablero = t;
		this.isWhiteTurn = true;
	}
	
	public void changeTurn() {
		this.isWhiteTurn = !this.isWhiteTurn;
	}
	
	public Coordenadas elegirPieza(Scanner sc, Movimiento mAnterior) {
		Coordenadas origen;
		
		System.out.println("Elige una pieza:");
		do {
			origen = new Coordenadas(sc);
			if (origen.isEmpty()) {
				return null;
			} else if (this.tablero.getCasilla(origen) == null) {
				System.out.println("No hay ninguna pieza en esta casilla.");
				continue;
			} else if (this.tablero.getCasilla(origen).isWhite != this.isWhiteTurn) {
				System.out.println("Esta pieza no te pertenece.");
				continue;
			} else if (this.tablero.getCasilla(origen).legalMoves(this.tablero, mAnterior).length == 0) {
				System.out.println("Esta pieza no tiene movimientos permitidos.");
				continue;
			} else {
				break;
			}
		} while (true);
		
		return origen;
	}
	
	public Coordenadas elegirDestino(Piezas p, Scanner sc, Movimiento mAnterior) {
		Coordenadas destino;
		
		this.tablero.printTablero(p.legalMoves(this.tablero, mAnterior), isWhiteTurn);
		System.out.println("Elige una casilla:");
		do {
			destino = new Coordenadas(sc);
			if (destino.isEmpty()) {
				return null;
			} else if (!destino.insideOf(p.legalMoves(tablero, mAnterior)))
				System.out.println("No puedes moverte a esa casilla.");
			else
				break;
		} while (true);
		
		return destino;
	}
	
	public boolean moverPieza(Scanner sc, LinkedList<Movimiento> listaMovimientos) {
		Coordenadas org = null;
		Coordenadas dst = null;
		do {
			org = this.elegirPieza(sc, listaMovimientos.getLast());
			if (org == null) break;
			dst = this.elegirDestino(this.tablero.getCasilla(org), sc, listaMovimientos.getLast());
			if (dst != null) break;
			else this.tablero.printTablero(isWhiteTurn);
		} while (true);
		if (org == null) {
			return false;
		} else {
			Movimiento mActual = new Movimiento(org, dst, this.tablero.getCasilla(org), this.tablero.getCasilla(dst));
			this.tablero.getCasilla(org).moverPieza(tablero, dst, mActual);
			listaMovimientos.add(mActual);
			return true;
		}
	}
}
