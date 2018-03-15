package ajedrez;

import java.io.Serializable;
import java.util.LinkedList;

public abstract class Piezas implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1603287332197999255L;
	public boolean isWhite; // Determina si la ficha es blanca
	Coordenadas posicion; // Determina en que casilla del tablero esta la pieza
	
	public Piezas(boolean isWhite, Coordenadas posicion) {
		this.isWhite = isWhite;
		this.posicion = posicion;
	}
	
	/*
	 * Devuelve un array con todas las casillas a las que puede mover el jugador la pieza elegida
	 * sin provocarse un jaquemate a si mismo. La variable mAnterior solo es necesitada por la
	 * clase peón, para determinar si puede realizar un en-passant.
	 */
	public abstract Coordenadas[] legalMoves(Tablero tablero, Movimiento mAnterior);
	
	// Devuelve el símbolo unicode de la pieza correspondiente
	public abstract String toString();
	
	// Determina si la pieza puede matar al rey enemigo en el próximo turno
	public abstract boolean canKillKing(Tablero tablero);
	
	// Modifica la variable posición de la pieza
	public void moverPieza(Tablero tablero, Coordenadas destino, Movimiento mActual) {
		tablero.movePieza(this.posicion, destino);
		this.posicion.setCoords(destino);
	}
	
	// Introduce un mensaje cuando una pieza muere
	public abstract void killPieza(Tablero tbl);
}


class Peon extends Piezas {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1485607008816298060L;
	// Determina si la pieza aún no se ha movido en toda la partida
	public boolean originalPosition; 
	
	public Peon(boolean isBlanca, Coordenadas posicion) {
		super(isBlanca, posicion);
		this.originalPosition = true;
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2659":"\u265F";
	}

	@Override
	public Coordenadas[] legalMoves(Tablero tablero, Movimiento mAnterior) {
		/*
		 * Guarda todas las casillas a las que puede moverse sin problemas la pieza.
		 * Como no podemos saber cuantas van a ser desde el principio usamos unas 
		 * LinkedList
		 */
		LinkedList<Coordenadas> posiciones = new LinkedList<Coordenadas>();
		Coordenadas cDestino; // Guarda las coordenadas a las que va a moverse la pieza
		Piezas pDestino; // Guarda la pieza que haya en la posición destino	
		
		// Calcula si puede moverse dos casillas verticalmente
		cDestino = this.posicion.addCoordenadas(0, this.isWhite?2:-2);
		if (cDestino.dentroTablero()) {
			pDestino = tablero.getCasilla(cDestino);
			
			if (originalPosition // Si la pieza aún no se ha movido en toda la partida
					&& pDestino == null // La casilla destino esta vacía
					// Mover la pieza no provoca una jaquemate al propio jugador
					&& !tablero.possibleCheck(this.isWhite, this.posicion, cDestino))
				posiciones.add(cDestino);
		}
		// Movimiento normal de una casilla vertical
		cDestino = this.posicion.addCoordenadas(0, this.isWhite?1:-1);
		if (cDestino.dentroTablero()) { // Tiene alguna casilla delante, no se sale del tablero
			pDestino = tablero.getCasilla(cDestino);
			
			if (pDestino == null
					&& !tablero.possibleCheck(this.isWhite, this.posicion, cDestino))
				posiciones.add(cDestino);
		}
				
		// Movimiento de ataque. Diagonal izquierdo
		cDestino = this.posicion.addCoordenadas(-1, this.isWhite?1:-1);
		if (cDestino.dentroTablero()) {
			pDestino = tablero.getCasilla(cDestino);
			
			if(pDestino != null // Tiene que haber una pieza en la casilla destino
					&& pDestino.isWhite != this.isWhite // La pieza ha de ser del color opuesto
					&& !tablero.possibleCheck(this.isWhite, this.posicion, cDestino))
				posiciones.add(cDestino);
		}
		
		// Movimiento de ataque. Diagonal derecho
		cDestino = this.posicion.addCoordenadas(1, this.isWhite?1:-1);
		if (cDestino.dentroTablero()) {
			pDestino = tablero.getCasilla(cDestino);
			
			if(pDestino != null  
					&& pDestino.isWhite != this.isWhite
					&& !tablero.possibleCheck(this.isWhite, this.posicion, cDestino))
				posiciones.add(cDestino);
		}
		
		// Movimiento en-passant
		// El peón tiene que haberse movido tres casillas verticalmente
		if (this.posicion.coorY == (this.isWhite?4:3)) { 
			// En-passant a la izquierda
			cDestino = this.posicion.addCoordenadas(new Coordenadas(-1, 0));
			
			if (cDestino.dentroTablero() ) {
				pDestino = tablero.getCasilla(cDestino);
				
				if (pDestino instanceof Peon  // La pieza objetivo tiene que ser un peón
						&& pDestino.isWhite != this.isWhite  
						&& pDestino == mAnterior.pOrigen  // El peon objetivo se acaba de mover
						// El peon objetivo se ha movido dos casillas verticalmente
						&& mAnterior.isPawnDoubleMove())  
					posiciones.add(this.posicion.addCoordenadas
							(new Coordenadas(-1, this.isWhite?1:-1)));	
			}
			
			// En-passant a la derecha
			cDestino = this.posicion.addCoordenadas(new Coordenadas(1, 0));
			
			if (cDestino.dentroTablero() ) {
				pDestino = tablero.getCasilla(cDestino);
				if (pDestino instanceof Peon  
						&& pDestino.isWhite != this.isWhite  
						&& pDestino == mAnterior.pOrigen 
						&& mAnterior.isPawnDoubleMove())
					posiciones.add(this.posicion.addCoordenadas
							(new Coordenadas(1, this.isWhite?1:-1)));
			}
		}
		
		// Pasamos la LinkedList a un Array y lo devolvemos
		return posiciones.toArray(new Coordenadas[posiciones.size()]);
	}

	@Override
	public void moverPieza(Tablero tablero, Coordenadas destino, Movimiento mActual) {
		// Si es la primera vez que se mueve, lo indicamos en el objeto Movimiento
		if (this.originalPosition) { 
			this.originalPosition = false;
			mActual.isFirstMove = true;
		}
		
		// Comprobamos si se ha movido en-passant
		// Se ha movido hacia un lado, y eso solo sucede cuando ataca
		if (Math.abs(mActual.cOrigen.distanceCasilla(mActual.cDestino).coorX) == 1  
				// A pesar de moverse en diagonal, no hay pieza en la casilla destino
				&& tablero.getCasilla(mActual.cDestino) == null) { 
			// Obtenemos el peon que ha muerto debido al en-passant
			Piezas pDestino = tablero.getCasilla
					(new Coordenadas(mActual.cDestino.coorX, mActual.cOrigen.coorY));
			
			mActual.pDestino = pDestino; // Añadimos la pieza victima al objeto Movimiento
			
			// Realizamos todo el proceso de eliminar la pieza
			pDestino.killPieza(tablero);
			if (this.isWhite) {
				tablero.piezasNegrasMuertas.add(pDestino);
			} else {
				tablero.piezasBlancasMuertas.add(pDestino);
			}
			tablero.setCasilla(pDestino.posicion, null);
		}
		
		// Llamamos al método estándar para mover nuestra pieza
		super.moverPieza(tablero, destino, mActual);
		
		// Si ha llegado a la última fila, cambiamos el peón por una reina
		if (this.posicion.coorY == (this.isWhite?7:0)) this.promotion(tablero);
	}

	@Override
	public void killPieza(Tablero tbl) {
		tbl.setMsg("Has matado " + (this.isWhite?"un pe�n blanco.":"un pe�n negro."));
	}
	
	// Creamos una reina en la posición actual del peón, y este desaparece
	public void promotion(Tablero tablero) {
		tablero.setCasilla(this.posicion, new Reina(this.isWhite, this.posicion));
	}

	@Override
	public boolean canKillKing(Tablero tablero) {
		Piezas pDestino;
		
		Coordenadas cDestino = this.posicion.addCoordenadas(1, this.isWhite?1:-1);
		if (cDestino.dentroTablero()) {
			pDestino = tablero.getCasilla(cDestino);
			
			if (pDestino instanceof Rey // Si la pieza que puede atacar es un Rey 
					&& pDestino.isWhite != this.isWhite) // Si el rey es del color opuesto al peon
				return true;
		}
			
		cDestino = this.posicion.addCoordenadas(-1, this.isWhite?1:-1);
		if (cDestino.dentroTablero()) {
			pDestino = tablero.getCasilla(cDestino);
			
			if (pDestino instanceof Rey 
					&& pDestino.isWhite != this.isWhite)
				return true;
		}
		
		return false;
	}
}

class Torre extends Piezas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8985372017626132637L;
	public boolean originalPosition;
	
	public Torre(boolean isBlanca, Coordenadas posicion) {
		super(isBlanca, posicion);
		this.originalPosition = true;
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2656":"\u265C";
	}

	@Override
	public Coordenadas[] legalMoves(Tablero tablero, Movimiento mAnterior) {
		LinkedList<Coordenadas> posiciones = new LinkedList<Coordenadas>();
		// Creamos un array con las direcciones en las que pueda moverse poder utilizar un bucle
		Coordenadas[] moves = new Coordenadas[] {
				new Coordenadas(1, 0), // Horizontal izquierda
				new Coordenadas(0, 1), // Vertical abajo
				new Coordenadas(-1, 0), // Horizontal derecha
				new Coordenadas(0, -1) // Vertical arriba
		};
		Coordenadas cDestino;
		
		for (Coordenadas coor: moves) {
			// Empezamos con la casilla original de la torre en cuestión
			cDestino = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			
			// Nos movemos una casilla en la dirección determinada y empezamos a comprobar
			cDestino = cDestino.addCoordenadas(coor);
			while (cDestino.dentroTablero() // Mientras la casilla destino este dentro del tablero
					// Mientras la casilla este vacía
					&& tablero.getCasilla(cDestino) == null) {
				//Si movernos a esta casilla no provoca un jaquemate al propio jugador
				if (!tablero.possibleCheck(this.isWhite, this.posicion, cDestino))
					posiciones.add(cDestino); // La casilla es legal y se añade a la lista
				
				cDestino = cDestino.addCoordenadas(coor); // Pasamos a la siguiente casilla
			} 
			
			/*
			 * Cuando el bucle anterior acabe, es posible que haya acabado al encontrarse una pieza
			 * Si esta pieza es del oponente, podemos matarla y por lo tanto es un movimiento legal
			 */
			if (cDestino.dentroTablero()) {
				Piezas pDestino = tablero.getCasilla(cDestino);
				
				if (pDestino != null // Hay una pieza en la casilla
						&& pDestino.isWhite != this.isWhite // Es una pieza del oponente
						&& !tablero.possibleCheck(this.isWhite, this.posicion, cDestino))
					posiciones.add(cDestino);
			}
		}
		
		return posiciones.toArray(new Coordenadas[posiciones.size()]);
	}

	@Override
	public void killPieza(Tablero tbl) {
		tbl.setMsg("Has matado " + (this.isWhite?"una torre blanca.":"una torre negra."));
	}

	@Override
	public boolean canKillKing(Tablero tablero) {
		Coordenadas[] moves = new Coordenadas[] {
				new Coordenadas(1, 0),
				new Coordenadas(0, 1), 
				new Coordenadas(-1, 0),
				new Coordenadas(0, -1)
		};
		Coordenadas cDestino;
		
		for (Coordenadas coor: moves) {
			cDestino = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			
			cDestino = cDestino.addCoordenadas(coor);
			// Vamos mirando casillas hasta que encontremos una pieza o nos salgamos del tablero
			while (cDestino.dentroTablero() 
					&& tablero.getCasilla(cDestino) == null) 
				cDestino = cDestino.addCoordenadas(coor);
			
			if (cDestino.dentroTablero()) {
				Piezas pDestino = tablero.getCasilla(cDestino);
				
				if (pDestino instanceof Rey 
						&& pDestino.isWhite != this.isWhite)
					return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void moverPieza(Tablero tablero, Coordenadas destino, Movimiento mActual) {
		if (this.originalPosition) {
			this.originalPosition = false;
			mActual.isFirstMove = true;
		}
		
		super.moverPieza(tablero, destino, mActual);
	}
}

class Caballo extends Piezas {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -829404229754169540L;

	public Caballo(boolean isBlanca, Coordenadas posicion) {
		super(isBlanca, posicion);
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2658":"\u265E";
	}

	@Override
	public Coordenadas[] legalMoves(Tablero tablero, Movimiento mAnterior) {
		LinkedList<Coordenadas> posiciones = new LinkedList<Coordenadas>();
		Coordenadas[] moves = new Coordenadas[] {
				new Coordenadas(2, 1),
				new Coordenadas(2, -1), 
				new Coordenadas(-2, 1),
				new Coordenadas(-2, -1), 
				new Coordenadas(-1, 2),
				new Coordenadas(1, 2), 
				new Coordenadas(-1, -2),
				new Coordenadas(1, -2)
		};
		Coordenadas cDestino;
		
		for (Coordenadas coor: moves) {
			cDestino = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			
			cDestino = cDestino.addCoordenadas(coor);
			if (cDestino.dentroTablero() ) {
				Piezas pDestino = tablero.getCasilla(cDestino);
				
				// Puede moverse tanto a una casilla vacía como a una ocupada por el oponente
				if ((pDestino == null || pDestino.isWhite != this.isWhite) 
						&& !tablero.possibleCheck(this.isWhite, this.posicion, cDestino))
					posiciones.add(cDestino);
			}
		}
		
		return posiciones.toArray(new Coordenadas[posiciones.size()]);
	}

	@Override
	public void killPieza(Tablero tbl) {
		tbl.setMsg("Has matado " + (this.isWhite?"un caballo blanco.":"un caballo negro."));
	}

	@Override
	public boolean canKillKing(Tablero tablero) {
		Coordenadas[] moves = new Coordenadas[] {
				new Coordenadas(2, 1),
				new Coordenadas(2, -1),
				new Coordenadas(-2, 1),
				new Coordenadas(-2, -1),
				new Coordenadas(-1, 2),
				new Coordenadas(1, 2), 
				new Coordenadas(-1, -2),
				new Coordenadas(1, -2)
		};
		Coordenadas cDestino;
		
		for (Coordenadas coor: moves) {
			cDestino = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			
			cDestino = cDestino.addCoordenadas(coor);
			if (cDestino.dentroTablero()) {
				Piezas pDestino = tablero.getCasilla(cDestino);
				
				if (pDestino instanceof Rey 
						&& pDestino.isWhite != this.isWhite) 
					return true;
			}	
		}
		
		return false;
	}
}
	
class Alfil extends Piezas {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5376515083662974052L;

	public Alfil(boolean isBlanca, Coordenadas posicion) {
		super(isBlanca, posicion);
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2657":"\u265D";
	}

	// Igual que la Torre, pero en diagonal
	@Override
	public Coordenadas[] legalMoves(Tablero tablero, Movimiento mAnterior) {
		LinkedList<Coordenadas> posiciones = new LinkedList<Coordenadas>();
		Coordenadas[] moves = new Coordenadas[] {
				new Coordenadas(-1, 1),
				new Coordenadas(1, 1), 
				new Coordenadas(1, -1),
				new Coordenadas(-1, -1)
		};
		Coordenadas cDestino;
		
		for (Coordenadas coor: moves) {
			cDestino = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			
			cDestino = cDestino.addCoordenadas(coor);
			while (cDestino.dentroTablero() 
					&& tablero.getCasilla(cDestino) == null) {
				if (!tablero.possibleCheck(this.isWhite, this.posicion, cDestino))
					posiciones.add(cDestino);
				
				cDestino = cDestino.addCoordenadas(coor);
			} 
			
			if (cDestino.dentroTablero()) {
				Piezas pDestino = tablero.getCasilla(cDestino);
				
				if (pDestino != null 
						&& pDestino.isWhite != this.isWhite) 
					posiciones.add(cDestino);
			}
		}
		
		return posiciones.toArray(new Coordenadas[posiciones.size()]);
	}

	@Override
	public void killPieza(Tablero tbl) {
		tbl.setMsg("Has matado " + (this.isWhite?"un alfil blanco.":"un alfil negro."));
	}

	// Igual que la Torre pero en diagonal
	@Override
	public boolean canKillKing(Tablero tablero) {
		Coordenadas[] moves = new Coordenadas[] {
				new Coordenadas(-1, 1),
				new Coordenadas(1, 1), 
				new Coordenadas(1, -1),
				new Coordenadas(-1, -1)};
		Coordenadas cDestino;
		
		for (Coordenadas coor: moves) {
			cDestino = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			
			cDestino = cDestino.addCoordenadas(coor);
			while (cDestino.dentroTablero() 
					&& tablero.getCasilla(cDestino) == null) 
				cDestino = cDestino.addCoordenadas(coor);
			
			if (cDestino.dentroTablero()) {
				Piezas pDestino = tablero.getCasilla(cDestino);
				
				if (pDestino instanceof Rey &&
						pDestino.isWhite != this.isWhite) 
					return true;
			}	
		}
		
		return false;
	}
}

class Reina extends Piezas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6576239582191325733L;

	public Reina(boolean isBlanca, Coordenadas posicion) {
		super(isBlanca, posicion);
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2655":"\u265B";
	}

	// Alfil y Torre combinados
	@Override
	public Coordenadas[] legalMoves(Tablero tablero, Movimiento mAnterior) {
		LinkedList<Coordenadas> posiciones = new LinkedList<Coordenadas>();
		Coordenadas[] moves = new Coordenadas[] {
				new Coordenadas(-1, 0),
				new Coordenadas(1, 0), 
				new Coordenadas(0, 1),
				new Coordenadas(0, -1), 
				new Coordenadas(1, 1),
				new Coordenadas(-1, -1), 
				new Coordenadas(1, -1),
				new Coordenadas(-1, 1)
		};
		Coordenadas cDestino;
		
		for (Coordenadas coor: moves) {
			cDestino = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			
			cDestino = cDestino.addCoordenadas(coor);
			while (cDestino.dentroTablero() 
					&& tablero.getCasilla(cDestino) == null) {
				if (!tablero.possibleCheck(this.isWhite, this.posicion, cDestino))
					posiciones.add(cDestino);
				
				cDestino = cDestino.addCoordenadas(coor);
			} 
			
			if (cDestino.dentroTablero() ) {
				Piezas pDestino = tablero.getCasilla(cDestino);
				
				if (pDestino != null 
						&& pDestino.isWhite != this.isWhite
						&& !tablero.possibleCheck(this.isWhite, this.posicion, cDestino))
					posiciones.add(cDestino);
			}		
		}
		
		return posiciones.toArray(new Coordenadas[posiciones.size()]);
	}

	@Override
	public void killPieza(Tablero tbl) {
		tbl.setMsg("Has matado " + (this.isWhite?"una reina blanca.":"una reina negra."));
	}
	
	// Alfil y Torre combinados
	@Override
	public boolean canKillKing(Tablero tablero) {
		Coordenadas[] moves = new Coordenadas[] {
				new Coordenadas(-1, 0),
				new Coordenadas(1, 0), 
				new Coordenadas(0, 1),
				new Coordenadas(0, -1), 
				new Coordenadas(1, 1),
				new Coordenadas(-1, -1), 
				new Coordenadas(1, -1),
				new Coordenadas(-1, 1)};
		Coordenadas cDestino;
		
		for (Coordenadas coor: moves) {
			cDestino = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			
			cDestino = cDestino.addCoordenadas(coor);
			while (cDestino.dentroTablero() 
					&& tablero.getCasilla(cDestino) == null) 
				cDestino = cDestino.addCoordenadas(coor);
			
			if (cDestino.dentroTablero()) {
				Piezas pDestino = tablero.getCasilla(cDestino);
				
				if (pDestino instanceof Rey &&
					pDestino.isWhite != this.isWhite) return true;
			}
		}
		
		return false;
	}
}

class Rey extends Piezas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9115111872678364910L;
	public boolean originalPosition;
	
	public Rey(boolean isBlanca, Coordenadas posicion) {
		super(isBlanca, posicion);
		this.originalPosition = true;
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2654":"\u265A";
	}

	@Override
	public Coordenadas[] legalMoves(Tablero tablero, Movimiento mAnterior) {
		LinkedList<Coordenadas> posiciones = new LinkedList<Coordenadas>();
		Coordenadas[] moves = new Coordenadas[] {
				new Coordenadas(-1, 0),
				new Coordenadas(1, 0),
				new Coordenadas(0, 1),
				new Coordenadas(0, -1), 
				new Coordenadas(1, 1),
				new Coordenadas(-1, -1),
				new Coordenadas(1, -1),
				new Coordenadas(-1, 1)
		};
		Coordenadas cDestino;
		
		for (Coordenadas coor: moves) {
			cDestino = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			
			cDestino = cDestino.addCoordenadas(coor);
			if (cDestino.dentroTablero()) {
				Piezas pDestino = tablero.getCasilla(cDestino);
				
				if ((pDestino == null || pDestino.isWhite != this.isWhite) && 
						!tablero.possibleCheck(this.isWhite, this.posicion, cDestino)) 
					posiciones.add(cDestino);
			}		
		}
		
		// Enroque con las torres
		if (this.originalPosition) { // El rey no se ha movid en toda la partida
			if (!tablero.isCheck(this.isWhite)) { // El rey no esta en jaque
				int coorY = this.posicion.coorY; // La fila en la que se encuentre el rey
				
				// Enroque con la torre izquierda
				cDestino = new Coordenadas(0, coorY);
				Piezas pDestino = tablero.getCasilla(cDestino);
				if (pDestino instanceof Torre) { // La pieza es una torre
					Torre t = (Torre) pDestino;
					
					if (t.originalPosition) { // La torre no se ha movido en toda la partida
						/*
						 * No necesitamos mirar si la torre es nuestra. Si no se ha movido tiene
						 * que se nuestra.
						 * 
						 * Miramos que las casillas entre la torre y el rey estén vacías.
						 */
						if (tablero.getCasilla(new Coordenadas(1, coorY)) == null 
								&& tablero.getCasilla(new Coordenadas(2, coorY)) == null
								&& tablero.getCasilla(new Coordenadas(3, coorY)) == null) {
							/*
							 * Comprobamos que las casillas por las que pasa el rey no estan
							 * a la vista de alguna pieza enemiga
							 */
							if (!tablero.possibleCheck
									(this.isWhite, this.posicion, new Coordenadas(3, coorY)) 
									&& !tablero.possibleCheck
									(this.isWhite, this.posicion, new Coordenadas(2, coorY))) {
								// Puede hacerse enroque con la torre izquierda
								posiciones.add(new Coordenadas(2, coorY)); 
							}
						}
					}
				} 
				
				// Enroque con la torre derecha. Mismo procedimiento pero con diferentes casillas
				cDestino = new Coordenadas(7, coorY);
				pDestino = tablero.getCasilla(cDestino);
				if (pDestino instanceof Torre) { 
					Torre t = (Torre) pDestino;
					
					if (t.originalPosition) { 
						if (tablero.getCasilla(new Coordenadas(5, coorY)) == null 
								&& tablero.getCasilla(new Coordenadas(6, coorY)) == null) {
							if (!tablero.possibleCheck
									(this.isWhite, this.posicion, new Coordenadas(5, coorY)) 
									&& !tablero.possibleCheck
									(this.isWhite, this.posicion, new Coordenadas(6, coorY))) {
								posiciones.add(new Coordenadas(6, coorY)); 
							}
						}
					}
				}
			}
		}
		
		return posiciones.toArray(new Coordenadas[posiciones.size()]);
	}

	// No es posible llegar a matar al rey. Antes se acabará la partida por jaquemate
	@Override
	public void killPieza(Tablero tbl) {
		return; 
	}

	// Un rey nunca va a poder matar a otro rey
	@Override
	public boolean canKillKing(Tablero tablero) {
		return false;
	}
	
	@Override
	public void moverPieza(Tablero tablero, Coordenadas destino, Movimiento mActual) {
		if (this.originalPosition) {
			this.originalPosition = false;
			mActual.isFirstMove = true;
		}
		
		// Si el rey se ha movido dos casillas, tiene que ser por enroque
		Coordenadas cDestino = this.posicion.distanceCasilla(destino);
		if (Math.abs(cDestino.coorX) == 2) { 
			Coordenadas cTorreOrigen;
			Coordenadas cTorreDestino;
			Piezas pTorre;
			if (cDestino.coorX == 2) { // Enroque con la torre derecha. Movemos la torre
				cTorreOrigen = new Coordenadas(7, this.posicion.coorY);
				cTorreDestino = new Coordenadas(5, this.posicion.coorY);
				pTorre = tablero.getCasilla(cTorreOrigen);
				
				tablero.movePieza(cTorreOrigen, cTorreDestino);
				pTorre.posicion = cTorreDestino;
			}
			else { // Enroque con la torre izquierda. Movemos la torre
				cTorreOrigen = new Coordenadas(0, this.posicion.coorY);
				cTorreDestino = new Coordenadas(3, this.posicion.coorY);
				pTorre = tablero.getCasilla(cTorreOrigen);
				
				tablero.movePieza(cTorreOrigen, cTorreDestino);
				pTorre.posicion = cTorreDestino;
			} 
		}
		
		super.moverPieza(tablero, destino, mActual);
	}
}