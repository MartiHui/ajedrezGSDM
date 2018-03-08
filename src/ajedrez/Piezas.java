package ajedrez;

import java.io.Serializable;
import java.util.LinkedList;

public abstract class Piezas implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5518742684251085433L;
	public boolean isWhite; //Â¿La ficha es blanca?
	Coordenadas posicion; //Posicion de la ficha en el tablero
	
	public Piezas(boolean isWhite, Coordenadas posicion) {
		this.isWhite = isWhite;
		this.posicion = posicion;
	}
	
	public abstract Coordenadas[] legalMoves(Tablero tablero);
	
	public abstract String toString();
	//La pieza puede matar al rey enemigo en el siguiente turno?
	public abstract boolean canKillKing(Tablero tablero);
	
	public void moverPieza(Tablero tablero, Coordenadas destino) {
		tablero.movePieza(this.posicion, destino);
		this.posicion.setCoords(destino);
	}
	
	public void killPieza() {
		System.out.print("Has matado ");
	}
}

class Peon extends Piezas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5883920772363369215L;
	public boolean firstMove; //El proximo movimiento de la pieza va a ser el primero que hace?
	
	public Peon(boolean isBlanca, Coordenadas posicion) {
		super(isBlanca, posicion);
		this.firstMove = true;
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2659":"\u265F";
	}

	@Override
	public Coordenadas[] legalMoves(Tablero tablero) {
		LinkedList<Coordenadas> posiciones = new LinkedList<Coordenadas>();
		
		//Primer movimiento de dos casilla verticales
		if (firstMove && tablero.getCasilla(this.posicion.addCoordenadas(0, this.isWhite?2:-2)) == null
				&& !tablero.possibleCheck(this.isWhite, this.posicion, this.posicion.addCoordenadas(0, this.isWhite?2:-2))) 
			posiciones.add(this.posicion.addCoordenadas(0, this.isWhite?2:-2));
		//Movimiento normal de una casilla vertical
		if (this.posicion.addCoordenadas(0, this.isWhite?1:-1).dentroTablero() &&
				tablero.getCasilla(this.posicion.addCoordenadas(0, this.isWhite?1:-1)) == null
				&& !tablero.possibleCheck(this.isWhite, this.posicion, this.posicion.addCoordenadas(0, this.isWhite?1:-1)))
			posiciones.add((this.posicion.addCoordenadas(0, this.isWhite?1:-1)));
		//Movimiento de ataque diagonal izquierdo
		if (this.posicion.addCoordenadas(-1, this.isWhite?1:-1).dentroTablero() &&
				tablero.getCasilla(this.posicion.addCoordenadas(-1, this.isWhite?1:-1)) != null && 
				tablero.getCasilla(this.posicion.addCoordenadas(-1, this.isWhite?1:-1)).isWhite != this.isWhite
				&& !tablero.possibleCheck(this.isWhite, this.posicion, this.posicion.addCoordenadas(-1, this.isWhite?1:-1)))
			posiciones.add(this.posicion.addCoordenadas(-1, this.isWhite?1:-1));
		//Movimiento de ataque diagonal derecho
		if (this.posicion.addCoordenadas(1, this.isWhite?1:-1).dentroTablero() &&
				tablero.getCasilla(this.posicion.addCoordenadas(1, this.isWhite?1:-1)) != null && 
				tablero.getCasilla(this.posicion.addCoordenadas(1, this.isWhite?1:-1)).isWhite != this.isWhite
				&& !tablero.possibleCheck(this.isWhite, this.posicion, this.posicion.addCoordenadas(1, this.isWhite?1:-1)))
			posiciones.add(this.posicion.addCoordenadas(1, this.isWhite?1:-1));
		
		return posiciones.toArray(new Coordenadas[posiciones.size()]);
	}

	@Override
	public void moverPieza(Tablero tablero, Coordenadas destino) {
		if (this.firstMove) this.firstMove = false;
		super.moverPieza(tablero, destino);
		if (this.posicion.coorY == (this.isWhite?7:0)) this.promotion(tablero);
	}

	@Override
	public void killPieza() {
		super.killPieza();
		System.out.println(this.isWhite?"un peón blanco.":"un peón negro");
	}
	
	public void promotion(Tablero tablero) {
		tablero.setCasilla(this.posicion, new Reina(this.isWhite, this.posicion));
	}

	@Override
	public boolean canKillKing(Tablero tablero) {
		if (this.posicion.addCoordenadas(1, this.isWhite?1:-1).dentroTablero() &&
				tablero.getCasilla(this.posicion.addCoordenadas(1, this.isWhite?1:-1)) instanceof Rey &&
				tablero.getCasilla(this.posicion.addCoordenadas(1, this.isWhite?1:-1)).isWhite != this.isWhite)
			return true;
		if (this.posicion.addCoordenadas(-1, this.isWhite?1:-1).dentroTablero() &&
				tablero.getCasilla(this.posicion.addCoordenadas(-1, this.isWhite?1:-1)) instanceof Rey &&
				tablero.getCasilla(this.posicion.addCoordenadas(-1, this.isWhite?1:-1)).isWhite != this.isWhite)
			return true;
		
		return false;
	}
}

class Torre extends Piezas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3067161873074010069L;
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
	public Coordenadas[] legalMoves(Tablero tablero) {
		LinkedList<Coordenadas> posiciones = new LinkedList<Coordenadas>();
		Coordenadas[] moves = new Coordenadas[] {new Coordenadas(1, 0),
				new Coordenadas(0, 1), new Coordenadas(-1, 0),
				new Coordenadas(0, -1)};
		Coordenadas temp;
		
		for (Coordenadas coor: moves) {
			temp = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			temp = temp.addCoordenadas(coor);
			while (temp.dentroTablero() && tablero.getCasilla(temp) == null) {
				if (!tablero.possibleCheck(this.isWhite, this.posicion, temp))
					posiciones.add(temp);
				temp = temp.addCoordenadas(coor);
			} 
			//El while anterior se parara en al casilla anterior a una pieza o limite. Miramos si la razon es una ficha
			//y si esta es de color contraria a la nuestra. EN este caso, podemos capturarla y es un movimiento posible
			if (temp.dentroTablero() && tablero.getCasilla(temp) != null && tablero.getCasilla(temp).isWhite != this.isWhite)
				if (!tablero.possibleCheck(this.isWhite, this.posicion, temp))
					posiciones.add(temp);
		}
		
		return posiciones.toArray(new Coordenadas[posiciones.size()]);
	}

	@Override
	public void killPieza() {
		super.killPieza();
		System.out.println(this.isWhite?"una torre blanca.":"una torre negra.");
	}

	@Override
	public boolean canKillKing(Tablero tablero) {
		Coordenadas[] moves = new Coordenadas[] {new Coordenadas(1, 0),
				new Coordenadas(0, 1), new Coordenadas(-1, 0),
				new Coordenadas(0, -1)};
		Coordenadas temp;
		
		for (Coordenadas coor: moves) {
			temp = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			temp = temp.addCoordenadas(coor);
			while (temp.dentroTablero() && tablero.getCasilla(temp) == null) {
				temp = temp.addCoordenadas(coor);
			}
			if (temp.dentroTablero() && tablero.getCasilla(temp) instanceof Rey &&
					tablero.getCasilla(temp).isWhite != this.isWhite)
				return true;
		}
		
		return false;
	}
	
	@Override
	public void moverPieza(Tablero tablero, Coordenadas destino) {
		if (this.originalPosition) this.originalPosition = false;
		super.moverPieza(tablero, destino);
	}
}

class Caballo extends Piezas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 597191665010835210L;

	public Caballo(boolean isBlanca, Coordenadas posicion) {
		super(isBlanca, posicion);
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2658":"\u265E";
	}

	@Override
	public Coordenadas[] legalMoves(Tablero tablero) {
		LinkedList<Coordenadas> posiciones = new LinkedList<Coordenadas>();
		Coordenadas[] moves = new Coordenadas[] {new Coordenadas(2, 1),
				new Coordenadas(2, -1), new Coordenadas(-2, 1),
				new Coordenadas(-2, -1), new Coordenadas(-1, 2),
				new Coordenadas(1, 2), new Coordenadas(-1, -2),
				new Coordenadas(1, -2)
		};
		Coordenadas temp;
		
		for (Coordenadas coor: moves) {
			temp = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			temp = temp.addCoordenadas(coor);
			if (temp.dentroTablero() && (tablero.getCasilla(temp) == null || tablero.getCasilla(temp).isWhite != this.isWhite))
				if (!tablero.possibleCheck(this.isWhite, this.posicion, temp))
					posiciones.add(temp);
		}
		
		return posiciones.toArray(new Coordenadas[posiciones.size()]);
	}

	@Override
	public void killPieza() {
		super.killPieza();
		System.out.println(this.isWhite?"un caballo blanco.":"un caballo negro");
	}

	@Override
	public boolean canKillKing(Tablero tablero) {
		Coordenadas[] moves = new Coordenadas[] {new Coordenadas(2, 1),
				new Coordenadas(2, -1), new Coordenadas(-2, 1),
				new Coordenadas(-2, -1), new Coordenadas(-1, 2),
				new Coordenadas(1, 2), new Coordenadas(-1, -2),
				new Coordenadas(1, -2)
		};
		Coordenadas temp;
		
		for (Coordenadas coor: moves) {
			temp = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			temp = temp.addCoordenadas(coor);
			if (temp.dentroTablero() && tablero.getCasilla(temp) instanceof Rey && tablero.getCasilla(temp).isWhite != this.isWhite)
				return true;
		}
		
		return false;
	}
}
	
class Alfil extends Piezas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8994524081522199417L;

	public Alfil(boolean isBlanca, Coordenadas posicion) {
		super(isBlanca, posicion);
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2657":"\u265D";
	}

	@Override
	public Coordenadas[] legalMoves(Tablero tablero) {
		LinkedList<Coordenadas> posiciones = new LinkedList<Coordenadas>();
		Coordenadas[] moves = new Coordenadas[] {new Coordenadas(-1, 1),
				new Coordenadas(1, 1), new Coordenadas(1, -1),
				new Coordenadas(-1, -1)
		};
		Coordenadas temp;
		
		for (Coordenadas coor: moves) {
			temp = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			temp = temp.addCoordenadas(coor);
			while (temp.dentroTablero() && tablero.getCasilla(temp) == null) {
				if (!tablero.possibleCheck(this.isWhite, this.posicion, temp))
					posiciones.add(temp);
				temp = temp.addCoordenadas(coor);
			} 
			if (temp.dentroTablero() && tablero.getCasilla(temp) != null && tablero.getCasilla(temp).isWhite != this.isWhite)
				posiciones.add(temp);
		}
		
		return posiciones.toArray(new Coordenadas[posiciones.size()]);
	}

	@Override
	public void killPieza() {
		super.killPieza();
		System.out.println(this.isWhite?"un alfil blanco.":"un alfil negro");
	}

	@Override
	public boolean canKillKing(Tablero tablero) {
		Coordenadas[] moves = new Coordenadas[] {new Coordenadas(-1, 1),
				new Coordenadas(1, 1), new Coordenadas(1, -1),
				new Coordenadas(-1, -1)};
		Coordenadas temp;
		
		for (Coordenadas coor: moves) {
			temp = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			temp = temp.addCoordenadas(coor);
			while (temp.dentroTablero() && tablero.getCasilla(temp) == null) {
				temp = temp.addCoordenadas(coor);
			}
			if (temp.dentroTablero() && tablero.getCasilla(temp) instanceof Rey &&
					tablero.getCasilla(temp).isWhite != this.isWhite)
				return true;
		}
		
		return false;
	}
}

class Reina extends Piezas {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2635363113082533547L;

	public Reina(boolean isBlanca, Coordenadas posicion) {
		super(isBlanca, posicion);
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2655":"\u265B";
	}

	@Override
	public Coordenadas[] legalMoves(Tablero tablero) {
		LinkedList<Coordenadas> posiciones = new LinkedList<Coordenadas>();
		Coordenadas[] moves = new Coordenadas[] {new Coordenadas(-1, 0),
				new Coordenadas(1, 0), new Coordenadas(0, 1),
				new Coordenadas(0, -1), new Coordenadas(1, 1),
				new Coordenadas(-1, -1), new Coordenadas(1, -1),
				new Coordenadas(-1, 1)
		};
		Coordenadas temp;
		
		for (Coordenadas coor: moves) {
			temp = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			temp = temp.addCoordenadas(coor);
			while (temp.dentroTablero() && tablero.getCasilla(temp) == null) {
				if (!tablero.possibleCheck(this.isWhite, this.posicion, temp))
					posiciones.add(temp);
				temp = temp.addCoordenadas(coor);
			} 
			if (temp.dentroTablero() && tablero.getCasilla(temp) != null && tablero.getCasilla(temp).isWhite != this.isWhite)
				if (!tablero.possibleCheck(this.isWhite, this.posicion, temp))
					posiciones.add(temp);
		}
		
		return posiciones.toArray(new Coordenadas[posiciones.size()]);
	}

	@Override
	public void killPieza() {
		super.killPieza();
		System.out.println(this.isWhite?"una reina blanca.":"una reina negra");
	}

	@Override
	public boolean canKillKing(Tablero tablero) {
		Coordenadas[] moves = new Coordenadas[] {new Coordenadas(-1, 0),
				new Coordenadas(1, 0), new Coordenadas(0, 1),
				new Coordenadas(0, -1), new Coordenadas(1, 1),
				new Coordenadas(-1, -1), new Coordenadas(1, -1),
				new Coordenadas(-1, 1)};
		Coordenadas temp;
		
		for (Coordenadas coor: moves) {
			temp = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			temp = temp.addCoordenadas(coor);
			while (temp.dentroTablero() && tablero.getCasilla(temp) == null) {
				temp = temp.addCoordenadas(coor);
			}
			if (temp.dentroTablero() && tablero.getCasilla(temp) instanceof Rey &&
					tablero.getCasilla(temp).isWhite != this.isWhite)
				return true;
		}
		
		return false;
	}
}

class Rey extends Piezas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2977013684368089937L;
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
	public Coordenadas[] legalMoves(Tablero tablero) {
		LinkedList<Coordenadas> posiciones = new LinkedList<Coordenadas>();
		Coordenadas[] moves = new Coordenadas[] {new Coordenadas(-1, 0),
				new Coordenadas(1, 0), new Coordenadas(0, 1),
				new Coordenadas(0, -1), new Coordenadas(1, 1),
				new Coordenadas(-1, -1), new Coordenadas(1, -1),
				new Coordenadas(-1, 1)
		};
		Coordenadas temp;
		
		for (Coordenadas coor: moves) {
			temp = new Coordenadas(this.posicion.coorX, this.posicion.coorY);
			temp = temp.addCoordenadas(coor);
			if (temp.dentroTablero() && (tablero.getCasilla(temp) == null || tablero.getCasilla(temp).isWhite != this.isWhite))
				if (!tablero.possibleCheck(this.isWhite, this.posicion, temp))
					posiciones.add(temp);
		}
		//Enroque. COndiciones: ni la torrey ni el rey debe haberse movido. Ni el rey ni las casillas por las que vaya a pasar el rey pueden estar en jaque
		if (this.originalPosition) {
			if (!tablero.isCheck(this.isWhite)) {//Si el rey no esta en jaque 
				int coorY = this.posicion.coorY;
				//Torre izquierda. En la casilla A1 o A8, segun sea negra o blanca
				if (tablero.getCasilla(new Coordenadas(0, coorY)) instanceof Torre) { //Si es una torre
					Torre t = (Torre) tablero.getCasilla(new Coordenadas(0, coorY));
					if (t.originalPosition) { //Si no se ha movido en toda la partida (Si no se ha movido, tiene que ser tuya)
						if (tablero.getCasilla(new Coordenadas(1, coorY)) == null //Si todas las casillas entre torre y rey están vacías
								&& tablero.getCasilla(new Coordenadas(2, coorY)) == null
								&& tablero.getCasilla(new Coordenadas(3, coorY)) == null) {
							if (!tablero.possibleCheck(this.isWhite, this.posicion, new Coordenadas(3, coorY)) //Si las casillas por las que pasa el rey no estan en jaque
									&& !tablero.possibleCheck(this.isWhite, this.posicion, new Coordenadas(2, coorY))) {
								posiciones.add(new Coordenadas(2, coorY)); //Todas las condiciones se cumplen, puede hacer enroque
							}
						}
					}
				} 
				//Torre derecha. En casilla H1 o H8, segun sea negra o blanca.
				if (tablero.getCasilla(new Coordenadas(7, coorY)) instanceof Torre) { //Si es una torre
					Torre t = (Torre) tablero.getCasilla(new Coordenadas(7, coorY));
					if (t.originalPosition) { //Si no se ha movido en toda la partida (Si no se ha movido, tiene que ser tuya)
						if (tablero.getCasilla(new Coordenadas(5, coorY)) == null //Si todas las casillas entre torre y rey están vacías
								&& tablero.getCasilla(new Coordenadas(6, coorY)) == null) {
							if (!tablero.possibleCheck(this.isWhite, this.posicion, new Coordenadas(5, coorY)) //Si las casillas por las que pasa el rey no estan en jaque
									&& !tablero.possibleCheck(this.isWhite, this.posicion, new Coordenadas(6, coorY))) {
								posiciones.add(new Coordenadas(6, coorY)); //Todas las condiciones se cumplen, puede hacer enroque
							}
						}
					}
				}
			}
		}
		
		return posiciones.toArray(new Coordenadas[posiciones.size()]);
	}

	@Override
	public void killPieza() {
		super.killPieza();
		System.out.println(this.isWhite?"un rey blanco.":"un rey negro");
	}

	@Override
	public boolean canKillKing(Tablero tablero) {
		//Un rey nunca puede matar a otro rey
		return false;
	}
	
	@Override
	public void moverPieza(Tablero tablero, Coordenadas destino) {
		if (this.originalPosition) this.originalPosition = false;
		 //Si el rey se ha movido dos casillas tiene que ser por enroque
		if (this.posicion.distanceCasilla(destino).coorX == 2) { //Dos casillas a la derecha
			tablero.movePieza(new Coordenadas(7, this.posicion.coorY), new Coordenadas(5, this.posicion.coorY));
		}
		if (this.posicion.distanceCasilla(destino).coorX == -2) { //Dos casillas a la izquierda
			tablero.movePieza(new Coordenadas(0, this.posicion.coorY), new Coordenadas(3, this.posicion.coorY));
		}
		super.moverPieza(tablero, destino);
	}
}