package ajedrez;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Scanner;

public class Controles implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1738660591554248778L;
	public Tablero tablero; // El tablero sobre el que actúan los controles
	public boolean isWhiteTurn; // Determina si actualmente es el turno de las blancas
	
	public Controles(Tablero t) {
		this.tablero = t;
		// Todas las partidas empiezan siendo el turno de las blancas
		this.isWhiteTurn = true;
	}
	
	public void changeTurn() {
		this.isWhiteTurn = !this.isWhiteTurn;
	}
	
	/*
	 * El jugador tiene que elegir una casilla con sus coordenadas, y esa casilla tiene
	 * que contener una pieza de su color con movimientos permitidos.
	 */
	public Coordenadas elegirPieza(Scanner sc, Movimiento mAnterior) {
		System.out.println("Elige una pieza:");
		
		do {
			Coordenadas origen = new Coordenadas(sc);
			
			if (origen.isEmpty()) // El jugador quiere abrir el menu
				return null;
			
			Piezas piezaElegida = this.tablero.getCasilla(origen);
			
			if (piezaElegida == null) {
				System.out.println("No hay ninguna pieza en esta casilla.");
				
				continue;
			} else if (piezaElegida.isWhite != this.isWhiteTurn) {
				System.out.println("Esta pieza no te pertenece.");
				
				continue;
			} else if (piezaElegida.legalMoves(this.tablero, mAnterior).length == 0) {
				System.out.println("Esta pieza no tiene movimientos permitidos.");
				
				continue;
			} else { // A elegido una casilla que contiene una pieza suya y que puede mover
				return origen;
			}
		} while (true);
	}
	
	/*
	 * El jugador tiene que elegir la casilla a la que quiere moverse, mediante sus coordenadas.
	 * Esta casilla tiene que estar dentro de los movimientos permitidos de la pieza.
	 */
	public Coordenadas elegirDestino(Piezas p, Scanner sc, Movimiento mAnterior) {
		this.tablero.printTablero(p.legalMoves(this.tablero, mAnterior), isWhiteTurn);
		System.out.println("Pieza elegida: " + p + " | Elige una casilla:");
		
		do {
			Coordenadas destino = new Coordenadas(sc);
			
			if (destino.isEmpty()) // El jugador quiere elegir otra pieza
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
			
			if (org == null) 
				return false;
			
			Coordenadas dst = this.elegirDestino(this.tablero.getCasilla(org), sc, listaMovimientos.getLast());
			
			if (dst == null) 
				this.tablero.printTablero(isWhiteTurn);
			else {
				Movimiento mActual = new Movimiento
						(org, dst, this.tablero.getCasilla(org), this.tablero.getCasilla(dst));
				
				this.tablero.getCasilla(org).moverPieza(tablero, dst, mActual);
				// Se añade al final porque existe la posibilidad de haber cambiado algo al mover
				listaMovimientos.add(mActual);
				
				return true;
			}
		} while (true);
	}
}
