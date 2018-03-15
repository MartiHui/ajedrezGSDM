package ajedrez;

import java.io.Serializable;

public class Movimiento implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8488347152242408806L;
	public Coordenadas cOrigen; // La casilla en la que estaba originalmente
	public Coordenadas cDestino; // La casilla a la que se mueve
	public Piezas pOrigen; // La pieza que se mueve
	public Piezas pDestino; // La pieza que estaba en la casilla destino
	public boolean isFirstMove; // En caso de peon, torre y rey, saber si es el primer movimiento
	
	public Movimiento(Coordenadas cOrigen, Coordenadas cDestino, Piezas pOrigen, Piezas pDestino) {
		this.cOrigen = cOrigen;
		this.cDestino = cDestino;
		this.pOrigen = pOrigen;
		this.pDestino = pDestino;
		this.isFirstMove = false;
	}
	
	// Saber si el movimiento es un enroque
	public boolean isCastling() {
		return this.pOrigen instanceof Rey // El enroque esta hecho por el rey
				// El rey se ha movido dos casillas horizontalmente
				&& (Math.abs(this.cOrigen.distanceCasilla(cDestino).coorX) == 2);
	}
	
	// Saber si el movimiento es un peon que se ha movido dos casillas verticalmente
	public boolean isPawnDoubleMove() {
		return this.pOrigen instanceof Peon 
				// El peon se ha movido dos casillas verticalmente
				&& (Math.abs(this.cOrigen.distanceCasilla(cDestino).coorY) == 2);
	}
	
	// Saber si en el movimiento, un peon ha promocionado a reina
	public boolean isPawnPromotion() {
		return this.pOrigen instanceof Peon 
				// El peon esta en el extremo del tablero
				&& (this.cDestino.coorY == 0 || this.cDestino.coorY == 7);
	}
	
	// Saber si el movimiento ha sido un enPassant
	public boolean isEnPassant() {
		if (this.pOrigen instanceof Peon 
				&& this.pDestino != null) // Hay alguna pieza capturada en el movimiento
			// La posición de la pieza capturada y la casilla a la que se mueve no coinciden
			return (!this.pDestino.posicion.equals(this.cDestino));
		else 
			return false;
	}
	
	public void deshacerMovimiento(Tablero tbl) {
		if (this.isCastling()) { // Tenemos que deshacer el movimiento de las torres
			Coordenadas org;
			Coordenadas dst;
			
			if (cDestino.coorX == 2) { // Ha hecho el enroque a la derecha
				org = new Coordenadas(3, this.cOrigen.coorY);
				dst = new Coordenadas(0, this.cOrigen.coorY);
			} else { // Ha hecho el enroque a la izquierda
				org = new Coordenadas(5, this.cOrigen.coorY);
				dst = new Coordenadas(7, this.cOrigen.coorY);
			}
			
			// Movemos la torre a su sitio
			tbl.setCasilla(dst, tbl.getCasilla(org));
			tbl.setCasilla(org, null);
			
			// Ponemos la posicion correcta en la variable de la torre
			tbl.getCasilla(dst).posicion = dst;
		}
		
		if (isPawnPromotion()) { // Cambiamos la reina por el peon original
			tbl.setCasilla(this.cDestino, this.pOrigen);
		}
		
		if (isEnPassant()) { // Recuperamos el peon capturado, pero no va en la casilla destino
			tbl.setCasilla(this.pDestino.posicion, this.pDestino);
			
			this.revivePieza(tbl);
			
			this.pDestino = null;
		}
		
		if (this.isFirstMove) { // Si era el primer movimiento, corregimos la variable de la pieza
			if (this.pOrigen instanceof Peon) {
				Peon p = (Peon) this.pOrigen;
				p.originalPosition = true;
			} else if (this.pOrigen instanceof Torre) {
				Torre t = (Torre) this.pOrigen;
				t.originalPosition = true;
			} else if (this.pOrigen instanceof Rey) {
				Rey r = (Rey) this.pOrigen;
				r.originalPosition = true;
			}
		}
		
		if (this.pDestino != null) {
			this.revivePieza(tbl);
		}
		
		tbl.setCasilla(cOrigen, pOrigen);
		this.pOrigen.posicion = this.cOrigen;
		
		tbl.setCasilla(cDestino, pDestino);
		if (this.pDestino != null) this.pDestino.posicion = this.cDestino;
	}
	
	// Sacamos la pieza de la lista de piezas capturadas
	public void revivePieza(Tablero tbl) {
		if (this.pDestino.isWhite) 
			tbl.piezasBlancasMuertas.remove(this.pDestino);
		else 
			tbl.piezasNegrasMuertas.remove(this.pDestino);
	}
	
	public String toString() {
		return "[" + this.pOrigen + "]: [" + this.cOrigen + ", " + this.cDestino
				+ "]. Ha matado a " + (this.pDestino!=null?this.pDestino:"nada");
	}

}
