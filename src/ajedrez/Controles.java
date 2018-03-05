package ajedrez;

import java.util.Scanner;

public class Controles {
	public Tablero tablero;
	public boolean isWhiteTurn;
	
	public Controles(Tablero t) {
		this.tablero = t;
		this.isWhiteTurn = true;
	}
	
	public void changeTurn() {
		this.isWhiteTurn = !this.isWhiteTurn;
	}
	
	public Coordenadas elegirPieza(Scanner sc) {
		Coordenadas origen;
		
		System.out.println("Elige una pieza. Si no introduces ninguna coordenada, abrirás las opciones de jugador.");
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
			} else if (this.tablero.getCasilla(origen).legalMoves(this.tablero).length == 0) {
				System.out.println("Esta pieza no tiene movimientos permitidos.");
				continue;
			} else {
				break;
			}
		} while (true);
		
		return origen;
	}
	
	public Coordenadas elegirDestino(Piezas p, Scanner sc) {
		Coordenadas destino;
		
		this.tablero.printTablero(p.legalMoves(this.tablero), isWhiteTurn);
		System.out.println("ELige la casilla a la que moverte. Si no introduces ninguna coordenada, podrás elegir una pieza diferente.");
		do {
			destino = new Coordenadas(sc);
			if (destino.isEmpty()) {
				return null;
			} else if (!destino.insideOf(p.legalMoves(tablero)))
				System.out.println("No puedes moverte a esa casilla.");
			else
				break;
		} while (true);
		
		return destino;
	}
	
	public boolean moverPieza(Scanner sc) {
		Coordenadas org = null;
		Coordenadas dst = null;
		do {
			org = this.elegirPieza(sc);
			if (org == null) break;
			dst = this.elegirDestino(this.tablero.getCasilla(org), sc);
			if (dst != null) break;
			else this.tablero.printTablero(isWhiteTurn);
		} while (true);
		if (org == null) {
			return false;
		} else {
			this.tablero.getCasilla(org).moverPieza(tablero, dst);
			return true;
		}
	}
}
