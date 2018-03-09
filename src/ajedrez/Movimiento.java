package ajedrez;

public class Movimiento {
	public Coordenadas cOrigen, cDestino;
	public Piezas pOrigen, pDestino;
	
	public Movimiento(Coordenadas cOrigen, Coordenadas cDestino, Piezas pOrigen, Piezas pDestino) {
		this.cOrigen = cOrigen;
		this.cDestino = cDestino;
		this.pOrigen = pOrigen;
		this.pDestino = pDestino;
	}
	
	public boolean isCastling() {
		return (Math.abs(this.cOrigen.distanceCasilla(cDestino).coorX) == 2);
	}
	
	public boolean isPawnDoubleMove() {
		return (Math.abs(this.cOrigen.distanceCasilla(cDestino).coorY) == 2);
	}
	
	public boolean isPawnPromotion() {
		return (this.cDestino.coorY == 0 || this.cDestino.coorY == 7);
	}
	
	public boolean isEnPassant() {
		return (this.pDestino.posicion != this.cDestino);
	}

}
