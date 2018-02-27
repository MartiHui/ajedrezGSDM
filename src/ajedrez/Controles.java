package ajedrez;

public class Controles {
	public Tablero tablero;
	private boolean isWhiteTurn;
	
	public Controles(Tablero t) {
		this.tablero = t;
		this.isWhiteTurn = true;
	}
	
	public Coordenadas elegirPieza() {
		Coordenadas coorPieza;
		
		System.out.print("Elige una pieza. ");
		do {
			coorPieza = new Coordenadas();
			if (this.tablero.getCasilla(coorPieza).isWhite
				!= this.isWhiteTurn) {
				System.out.println("Elige una pieza de tu color.");
			} else break;
		} while (true);
		
		return coorPieza;
	}

	public Coordenadas elegirObjetivo() {
		Coordenadas coorOrigen, coorDestino;
		coorOrigen = elegirPieza();
		Coordenadas[] legalMoves
						= this.tablero.getCasilla(coorOrigen)
						.legalMoves(tablero);
		
		return null;
	}
}
