package ajedrez;

import java.io.Serializable;

public class Movimiento implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3465246927097124306L;
	public Coordenadas cOrigen, cDestino;
	public Piezas pOrigen, pDestino;
	public boolean isFirstMove;
	
	public Movimiento(Coordenadas cOrigen, Coordenadas cDestino, Piezas pOrigen, Piezas pDestino) {
		this.cOrigen = cOrigen;
		this.cDestino = cDestino;
		this.pOrigen = pOrigen;
		this.pDestino = pDestino;
		this.isFirstMove = false;
	}
	
	public boolean isCastling() {
		return this.pOrigen instanceof Rey && (Math.abs(this.cOrigen.distanceCasilla(cDestino).coorX) == 2);
	}
	
	public boolean isPawnDoubleMove() {
		return this.pOrigen instanceof Peon && (Math.abs(this.cOrigen.distanceCasilla(cDestino).coorY) == 2);
	}
	
	public boolean isPawnPromotion() {
		return this.pOrigen instanceof Peon && (this.cDestino.coorY == 0 || this.cDestino.coorY == 7);
	}
	
	public boolean isEnPassant() {
		if (this.pOrigen instanceof Peon && this.pDestino != null)
			return (!this.pDestino.posicion.equals(this.cDestino));
		else return false;
	}
	
	public void deshacerMovimiento(Tablero tbl) {
		if (this.isCastling()) {
			if (cDestino.coorX == 2) {
				tbl.setCasilla(new Coordenadas(0, this.cOrigen.coorY),
						tbl.getCasilla(new Coordenadas(3, this.cOrigen.coorY)));
				tbl.setCasilla(new Coordenadas(3, this.cOrigen.coorY), null);
				tbl.getCasilla(new Coordenadas(0, this.cOrigen.coorY)).posicion = new Coordenadas(0, this.cOrigen.coorY);
			}
			if (cDestino.coorX == 6) {
				tbl.setCasilla(new Coordenadas(7, this.cOrigen.coorY),
						tbl.getCasilla(new Coordenadas(5, this.cOrigen.coorY)));
				tbl.setCasilla(new Coordenadas(5, this.cOrigen.coorY), null);
				tbl.getCasilla(new Coordenadas(7, this.cOrigen.coorY)).posicion = new Coordenadas(7, this.cOrigen.coorY);
			}
		}
		
		if (isPawnPromotion()) {
			tbl.setCasilla(this.cDestino, this.pOrigen);
		}
		
		if (isEnPassant()) {
			tbl.setCasilla(this.pDestino.posicion, this.pDestino);
			if (this.pDestino != null) {
				if (this.pDestino.isWhite) tbl.piezasBlancasMuertas.remove(this.pDestino);
				else tbl.piezasNegrasMuertas.remove(this.pDestino);
			}
			this.pDestino = null;
		}
		
		if (this.isFirstMove) {
			if (this.pOrigen instanceof Peon) {
				Peon p = (Peon) this.pOrigen;
				p.firstMove = true;
			} else if (this.pOrigen instanceof Torre) {
				Torre t = (Torre) this.pOrigen;
				t.originalPosition = true;
			} else if (this.pOrigen instanceof Rey) {
				Rey r = (Rey) this.pOrigen;
				r.originalPosition = true;
			}
		}
		
		if (this.pDestino != null) {
			if (this.pDestino.isWhite) tbl.piezasBlancasMuertas.remove(this.pDestino);
			else tbl.piezasNegrasMuertas.remove(this.pDestino);
		}
		
		tbl.setCasilla(cOrigen, pOrigen);
		this.pOrigen.posicion = this.cOrigen;
		tbl.setCasilla(cDestino, pDestino);
		if (this.pDestino != null) this.pDestino.posicion = this.cDestino;
	}
	
	public String toString() {
		return "[" + this.pOrigen + "]: [" + this.cOrigen + ", " + this.cDestino
				+ "]. Ha matado a " + (this.pDestino!=null?this.pDestino:"nada");
	}

}
