package ajedrez;

import java.util.LinkedList;

public abstract class Piezas {
	public boolean isAlive; //La ficha esta viva?
	public boolean isWhite; //¿La ficha es blanca?
	Coordenadas[] moves; //Movimientos generales
	
	public Piezas(boolean isWhite, Coordenadas[] moves) {
		this.isAlive = true;
		this.isWhite = isWhite;
		this.moves = moves;
	}
	
	public abstract Coordenadas[] legalMoves();
	
	public abstract String toString();
}

class Peon extends Piezas {
	public Peon(boolean isBlanca) {
		super(isBlanca, new Coordenadas[] {new Coordenadas(0, isBlanca?1:-1)});
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2659":"\u265F";
	}

	@Override
	public Coordenadas[] legalMoves() {
		// TODO Auto-generated method stub
		return null;
	}
}

class Torre extends Piezas {
	public Torre(boolean isBlanca) {
		super(isBlanca, new Coordenadas[] {new Coordenadas(1, 0),
				new Coordenadas(0, 1), new Coordenadas(-1, 0),
				new Coordenadas(0, -1)
		});
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2656":"\u265C";
	}

	@Override
	public Coordenadas[] legalMoves() {
		// TODO Auto-generated method stub
		return null;
	}
}

class Caballo extends Piezas {
	public Caballo(boolean isBlanca) {
		super(isBlanca, new Coordenadas[] {new Coordenadas(2, 1),
				new Coordenadas(2, -1), new Coordenadas(-2, 1),
				new Coordenadas(-2, -1), new Coordenadas(-1, 2),
				new Coordenadas(1, 2), new Coordenadas(-1, -2),
				new Coordenadas(1, -2)
		});
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2658":"\u265E";
	}

	@Override
	public Coordenadas[] legalMoves() {
		// TODO Auto-generated method stub
		return null;
	}
}
	
class Alfil extends Piezas {
	public Alfil(boolean isBlanca) {
		super(isBlanca, new Coordenadas[] {new Coordenadas(-1, 1),
				new Coordenadas(1, 1), new Coordenadas(1, -1),
				new Coordenadas(-1, -1)
		});
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2657":"\u265D";
	}

	@Override
	public Coordenadas[] legalMoves() {
		// TODO Auto-generated method stub
		return null;
	}
}

class Reina extends Piezas {
	public Reina(boolean isBlanca) {
		super(isBlanca, new Coordenadas[] {new Coordenadas(-1, 0),
				new Coordenadas(1, 0), new Coordenadas(0, 1),
				new Coordenadas(0, -1), new Coordenadas(1, 1),
				new Coordenadas(-1, -1), new Coordenadas(1, -1),
				new Coordenadas(-1, 1)
		});
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2655":"\u265B";
	}

	@Override
	public Coordenadas[] legalMoves() {
		// TODO Auto-generated method stub
		return null;
	}
}

class Rey extends Piezas {
	public Rey(boolean isBlanca) {
		super(isBlanca, new Coordenadas[] {new Coordenadas(-1, 0),
				new Coordenadas(1, 0), new Coordenadas(0, 1),
				new Coordenadas(0, -1), new Coordenadas(1, 1),
				new Coordenadas(-1, -1), new Coordenadas(1, -1),
				new Coordenadas(-1, 1)
		});
	}

	@Override
	public String toString() {
		return this.isWhite?"\u2654":"\u265A";
	}

	@Override
	public Coordenadas[] legalMoves() {
		// TODO Auto-generated method stub
		return null;
	}
}