package ajedrez;

import java.io.Serializable;
import java.util.LinkedList;

public class Tablero implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1332063901361324268L;
	public Piezas[][] tablero; // Información sobre que pieza hay en cada casilla
	public LinkedList<Piezas> piezasBlancasMuertas; // Lista de piezas blancas muertas
	public LinkedList<Piezas> piezasNegrasMuertas; // Lo mismo pero con piezas negras
	public String p1, p2; // Nombre de los jugadores. p1:blancas p2:negras
	/*
	 *  En caso de que haya un evento, se muestra el mensaje bajo el tablero, para que el jugador
	 *  pueda verlo sin problemas.
	 */
	public String msg; 
	
	public Tablero(String p1, String p2) {
		this.tablero = new Piezas[8][8];
		startTablero();
		this.piezasBlancasMuertas = new LinkedList<Piezas>();
		this.piezasNegrasMuertas = new LinkedList<Piezas>();
		this.p1 = p1;
		this.p2 = p2;
		this.msg = "";
	}
	
	// Colocamos las piezas en su posición original
	private void startTablero() {
		// Torres blancas
		tablero[0][0] = new Torre(true, new Coordenadas(0, 0));
		tablero[7][0] = new Torre(true, new Coordenadas(7, 0));
		tablero[0][7] = new Torre(false, new Coordenadas(0, 7));
		tablero[7][7] = new Torre(false, new Coordenadas(7, 7));
		
		// Caballos
		tablero[1][0] = new Caballo(true, new Coordenadas(1, 0));
		tablero[6][0] = new Caballo(true, new Coordenadas(6, 0));
		tablero[1][7] = new Caballo(false, new Coordenadas(1, 7));
		tablero[6][7] = new Caballo(false, new Coordenadas(6, 7));
		
		// Alfiles
		tablero[2][0] = new Alfil(true, new Coordenadas(2, 0));
		tablero[5][0] = new Alfil(true, new Coordenadas(5, 0));
		tablero[2][7] = new Alfil(false, new Coordenadas(2, 7));
		tablero[5][7] = new Alfil(false, new Coordenadas(5, 7));
		
		// Reina
		tablero[3][0] = new Reina(true, new Coordenadas(3, 0));
		tablero[3][7] = new Reina(false, new Coordenadas(3, 7));
		
		// Rey
		tablero[4][0] = new Rey(true, new Coordenadas(4, 0));
		tablero[4][7] = new Rey(false, new Coordenadas(4, 7));
		
		// Peones
		for (int coorX = 0; coorX < 8; coorX++) {
			tablero[coorX][1] = new Peon(true, new Coordenadas(coorX, 1));
			tablero[coorX][6] = new Peon(false, new Coordenadas(coorX, 6));
		}
	}
	
	/*
	 * Mostrará el tablero por pantalla, girando según de quien sea el turno. 
	 * 
	 * En caso de que se muestren las posiciones a las que puede moverse una pieza, las vacías
	 * estarán marcadas por una X y en las que haya alguna pieza, la pieza estará entre [].
	 * 
	 * En caso de que haya algún mensaje por evento, se mostrará bajo el tablero
	 */
	public void printTablero(Coordenadas[] legalMoves, boolean isWhiteTurn) {
		Coordenadas coord = new Coordenadas(0, 0);
		// Determina si la casilla está dentro de los posibles movimientos de una pieza en cuestión
		boolean specialSquare;
		// Según de que jugador sea el turno, se mostrará un lado abajo o el otro
		int[] columnas = 
				isWhiteTurn?new int[] {7, 6, 5, 4, 3, 2, 1, 0}:new int[] {0, 1, 2, 3, 4, 5, 6, 7};
		int[] filas = 
				isWhiteTurn?new int[] {0, 1, 2, 3, 4, 5, 6, 7}:new int[] {7, 6, 5, 4, 3, 2, 1, 0};
		
		// Se mostrará las coordenadas en los 4 lados del tablero, para que sea más facil elegir		
		System.out.println(isWhiteTurn?
				"    A   B   C   D   E   F   G   H X":"    H   G   F   E   D   C   B   A X");
		System.out.println("  +-------------------------------+ Y");
		
		for (int coorY: columnas) {
			System.out.print((coorY+1) + " |");
			
			for (int coorX : filas) {
				coord.setCoords(coorX,  coorY);
				specialSquare = coord.insideOf(legalMoves);
				
				if (this.getCasilla(coord) == null) {
					System.out.print(specialSquare?" X ":"   ");
				} else {
					if (specialSquare) {
						System.out.print("[" + this.getCasilla(coord) + "]");
					} else {
						System.out.print(" " + this.getCasilla(coord) + " ");
					}
				}
				
				System.out.print("|");
			}
			System.out.print(" " + (coorY+1));
			
			/*
			 * Mensajes especiales a la derecha del tablero:
			 *   - Fichas del enemigo capturadas
			 *   - Nombre y color de los jugadores
			 *   - Una flecha indicando de quién es el turno
			 */
			switch (coorY) {
				case 7:
					System.out.print((isWhiteTurn?"    ":" -> ") + "Negras: " + this.p2);
					
					break;
				
				case 6: // Se mostraran las piezas capturadas por el equipo negro de 5 en 5
					System.out.print("    ");
					for (Piezas p: this.piezasBlancasMuertas.subList
							(0, Math.min(this.piezasBlancasMuertas.size(), 5))) 
						System.out.print(p);
					
					break;
				
				case 5:
					System.out.print("    ");
					
					if (this.piezasBlancasMuertas.size() > 5) 
						for (Piezas p: this.piezasBlancasMuertas.subList
								(5, Math.min(this.piezasBlancasMuertas.size(), 10))) 
							System.out.print(p);
					
					break;
				
				case 4: // Maximo de piezas capturadas en cualquier momento: 15
					System.out.print("    ");
					
					if (this.piezasBlancasMuertas.size() > 10) 
						for (Piezas p: this.piezasBlancasMuertas.subList
								(10, Math.min(this.piezasBlancasMuertas.size(), 15))) 
							System.out.print(p);
					
					break;
				
				case 3:
					System.out.print("    ");
					
					if (this.piezasNegrasMuertas.size() > 10) 
						for (Piezas p: this.piezasNegrasMuertas.subList
								(10, Math.min(this.piezasNegrasMuertas.size(), 15))) 
							System.out.print(p);
					
					break;
				
				case 2:
					System.out.print("    ");
					
					if (this.piezasNegrasMuertas.size() > 5) 
						for (Piezas p: this.piezasNegrasMuertas.subList
								(5, Math.min(this.piezasNegrasMuertas.size(), 10))) 
							System.out.print(p);
					
					break;
				
				case 1:
					System.out.print("    ");
					
					for (Piezas p: this.piezasNegrasMuertas.subList
							(0, Math.min(this.piezasNegrasMuertas.size(), 5))) 
						System.out.print(p);
					
					break;
				
				case 0:
					System.out.print((isWhiteTurn?" -> ":"    ") + "Blancas: " + this.p1);
					
					break;
				
				default:
					break;
			}
			
			System.out.println();
			if ((isWhiteTurn && coorY != 0) || (!isWhiteTurn && coorY != 7)) 
				System.out.println("  |---+---+---+---+---+---+---+---|");
			
		}
		
		System.out.println("Y +-------------------------------+");
		System.out.println(isWhiteTurn?
				"    A   B   C   D   E   F   G   H X":"    H   G   F   E   D   C   B   A X");
		System.out.println("          Enter: abrir menú\n");
		
		if (!this.msg.equals("")) {
			System.out.println("-----------------------------------------"
					+ "\n" + msg
					+ "\n-----------------------------------------");
			
			this.msg = ""; // Reseteamos el mensaje, para que no se repita la proxima vez
		}
		
		System.out.println("------Turno de " + (isWhiteTurn?this.p1:this.p2) + "------");
	}
	
	/*
	 * En caso de que no mostremos las casillas a las que pueda moverse una pieza, llamamos
	 * al método con un array de Coordenadas que solo contiene un elemento, que apunta a una
	 * posición fuera del tablero, y el boolean specialSquares siempre será false.
	 */
	public void printTablero(boolean isWhiteTurn) {
		printTablero(new Coordenadas[] {new Coordenadas(-1, -1)}, isWhiteTurn);
	}
	
	public void setMsg(String str) {
		this.msg = str;
	}
	
	// Nos devuelve la pieza que haya en una casilla del tablero determinada
	public Piezas getCasilla(Coordenadas coor) {
		return this.tablero[coor.coorX][coor.coorY];
	}
	
	// Ponemos una pieza en la casilla del tablero determinada
	public void setCasilla(Coordenadas coor, Piezas p) {
		this.tablero[coor.coorX][coor.coorY] = p;
	}
	
	// Movemos la pieza que haya en la casilla org a la casilla dst
	public void movePieza(Coordenadas org, Coordenadas dst) {
		// Si hay una pieza en la casilla destino, tiene que ser del oponente
		if (this.getCasilla(dst) != null) { 
			if (this.getCasilla(dst).isWhite) {
				this.piezasBlancasMuertas.add(this.getCasilla(dst));
			} else {
				this.piezasNegrasMuertas.add(this.getCasilla(dst));
			}
			
			this.getCasilla(dst).killPieza(this);
		}
		
		this.setCasilla(dst, this.getCasilla(org)); // Movemos la pieza a su destino
		this.setCasilla(org, null); // Ponemos la casilla original como vacia
	}
	
	// Determina si el rey del color determinado por la variable isWhiteKing esta en jaque
	public boolean isCheck(boolean isWhiteKing) {
		// Recorremos las piezas del tablero
		for (Piezas[] y: tablero) {
			for (Piezas x: y) {
				if (x != null // Si hay una pieza en la casilla
						&& x.isWhite != isWhiteKing  // Si es del oponente
						&& x.canKillKing(this)) // Si puede matar al rey en el proximo turno
					return true;
			}
		}
		
		return false;
	}
	
	/*
	 * Determina si haciendo un movimiento, el rey, de color determinado por la variable isWhiteKing
	 * acabaría en jaque. 
	 * 
	 * No usamos el método movePieza para evitar que nos mate la pieza que haya en el destino.
	 */
	public boolean possibleCheck(boolean isWhiteKing, Coordenadas org, Coordenadas dst) {
		// Guardamos las piezas originales para poder restablecer los cambios
		Piezas origen = this.getCasilla(org);
		Piezas destino = this.getCasilla(dst);
		
		// Realizamos el movimiento
		this.setCasilla(org, null);
		this.setCasilla(dst, origen);
		
		// Comprobamos si seria jaque
		boolean isCheck = this.isCheck(isWhiteKing);
		
		// Devolvemos las piezas a su sitio
		this.setCasilla(org, origen);
		this.setCasilla(dst, destino);
		
		return isCheck;
	}
	
	/*
	 * Determina si el juego puede continuar, si se ha conseguido un jaquemaque o si hay un empate
	 * y ninguno de los jugadores puede ganar sin que el otro pierda aposta
	 * 
	 * Devuelve un número:
	 *   -1 : jaquemate
	 *   0 : empate
	 *   1 : el juego puede continuar
	 */
	public int refreshGameStatus(boolean isWhiteTurn, Movimiento mAnterior) {
		// Determina si el jugador tiene alguna pieza que puede moverse
		boolean canMove = false;
		
		for (Piezas[] y: this.tablero) {
			for (Piezas x: y) {
				if (x != null // Hay una pieza
						&& x.isWhite == isWhiteTurn // La pieza es del jugador
						&& x.legalMoves(this, mAnterior).length > 0) { // La pieza tiene movimientos
					canMove = true; // El jugador puede mover alguna pieza
					break;
				}
			}
		}
		
		// Si no puede moverse
		if (!canMove) {
			// No puede moverse y además esta en jaque
			if (this.isCheck(isWhiteTurn)) 
				return -1; // Jaquemate
			else { // No puede moverse pero no esta en jaque
				this.setMsg("No hay movimientos legales.");
				return 0; // Empate
			}
		/*
		 * Aunque puede moverse xisten combinaciones de piezas que hacen imposible conseguir 
		 * un jaquemate y las partidas se alargarían infinitamente:
		 *   - Rey vs Rey
		 *   - Rey y Alfil vs Rey
		 *   - Rey y Caballo vs Rey
		 */
		} else {
			// Lista para guardar todas las piezas que hay en el tablero
			LinkedList<Coordenadas> piezas = new LinkedList<Coordenadas>();
			
			for (Piezas[] y: this.tablero) {
				for (Piezas x: y) {
					if (x != null)
						piezas.add(x.posicion);
					
					// Si hay más de tres piezas, no puede ser ninguna de las combinaciones
					if (piezas.size() > 3) 
						return 1;  // El juego puede continuar
				}
			}
			
			if (piezas.size() == 2) return 0; //Quedan los dos reyes, es un empate
			
			/*
			 * Si quedan tres piezas, dos de ellas tienes que ser los reyes, por lo que solo 
			 * necesitamos ver si la pieza restante es diferentes de un alfil o un caballos. Si
			 * lo es, no puede ser alguna de las combinaciones de empate
			 */
			for (Coordenadas coor: piezas) {
				Piezas pDestino = this.getCasilla(coor);
				
				if (!((pDestino instanceof Alfil) 
						|| (pDestino instanceof Caballo)
						|| (pDestino instanceof Rey))) 
					return 1; // La partida puede continuar
			}
		}
		
		this.setMsg("No hay suficientes piezas para lograr un jaquemate.");
		return 0; // Es alguna de las combinaciones de empate. Empate
	}
}
