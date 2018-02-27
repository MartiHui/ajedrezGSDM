package ajedrez;

import java.util.LinkedList;

public class Tablero {
	public Piezas[][] tablero; //Tablero y piezas
	public LinkedList<Piezas> piezasMuertas; //RIP
	public String p1, p2; //Nombre de los jugadores
	
	public Tablero(String p1, String p2) {
		this.tablero = new Piezas[8][8];
		startTablero();
		this.piezasMuertas = new LinkedList<Piezas>();
		this.p1 = p1;
		this.p2 = p2;
	}
	
	private void startTablero() {
		//Torres
		tablero[0][0] = new Torre(true, new Coordenadas(0, 0));
		tablero[7][0] = new Torre(true, new Coordenadas(7, 0));
		tablero[0][7] = new Torre(false, new Coordenadas(0, 7));
		tablero[7][7] = new Torre(false, new Coordenadas(7, 7));
		//Caballos
		tablero[1][0] = new Caballo(true, new Coordenadas(1, 0));
		tablero[6][0] = new Caballo(true, new Coordenadas(6, 0));
		tablero[1][7] = new Caballo(false, new Coordenadas(1, 7));
		tablero[6][7] = new Caballo(false, new Coordenadas(6, 7));
		//Alfiles
		tablero[2][0] = new Alfil(true, new Coordenadas(2, 0));
		tablero[5][0] = new Alfil(true, new Coordenadas(5, 0));
		tablero[2][7] = new Alfil(false, new Coordenadas(2, 7));
		tablero[5][7] = new Alfil(false, new Coordenadas(5, 7));
		//Reina
		tablero[3][0] = new Reina(true, new Coordenadas(3, 0));
		tablero[3][7] = new Reina(false, new Coordenadas(3, 7));
		//Rey
		tablero[4][0] = new Rey(true, new Coordenadas(4, 0));
		tablero[4][7] = new Rey(false, new Coordenadas(4, 7));
		//Peones
		for (int coorX = 0; coorX < 8; coorX++) {
			tablero[coorX][1] = new Peon(true, new Coordenadas(coorX, 1));
			tablero[coorX][6] = new Peon(false, new Coordenadas(coorX, 6));
		}
	}
	
	public void printTablero(Coordenadas[] legalMoves) {
		Coordenadas coord = new Coordenadas(0, 0);
		boolean specialSquare;
		
		System.out.println("  +-------------------------------+");
		for (int coorY = 0; coorY < 8; coorY++) {
			System.out.print((8-coorY) + " |");
			for (int coorX = 0; coorX < 8; coorX++) {
				coord.setCoords(coorX,  coorY);
				specialSquare = 
						legalMoves!=null?coord.insideOf(legalMoves):false;
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
			System.out.println();
			if (coorY != 7) System.out.println
			("  |---+---+---+---+---+---+---+---|");
			
		}
		System.out.println("Y +-------------------------------+");
		System.out.print("  X A   B   C   D   E   F   G   H");
	}
	
	public void printTablero() {
		printTablero(null);
	}
	
	public Piezas getCasilla(Coordenadas coor) {
		return this.tablero[coor.coorX][coor.coorY];
	}
	
	public void setCasilla(Coordenadas coor, Piezas p) {
		this.tablero[coor.coorX][coor.coorY] = p;
	}
	
	public void movePieza(Coordenadas org, Coordenadas dst) {
		if (this.getCasilla(dst) != null) {
			this.piezasMuertas.add(this.getCasilla(dst));
			this.getCasilla(dst).killPieza();
		}
		this.setCasilla(dst, this.getCasilla(org));
		this.setCasilla(org, null);
	}
	
	public boolean isCheck(boolean isWhiteKing) {
		Piezas obj = null;
		for (Piezas[] y: tablero) {
			for (Piezas x: y) {
				if (x instanceof Rey && x.isWhite == isWhiteKing) obj = x;
			}
		}
		
		for (Piezas[] y: tablero) {
			for (Piezas x: y) {
				if (x.isWhite != isWhiteKing && obj.posicion.insideOf(x.legalMoves(this))) return true;
			}
		}
		
		return false;
	}
	
	public boolean possibleCheck(boolean isWhiteKing, Coordenadas org, Coordenadas dst) {
		Piezas origen = this.getCasilla(org);
		Piezas destino = this.getCasilla(dst);
		boolean isCheck;
		
		this.setCasilla(org, null);
		this.setCasilla(dst, origen);
		isCheck = this.isCheck(isWhiteKing);
		this.setCasilla(org, origen);
		this.setCasilla(dst, destino);
		
		return isCheck;
	}
	
	public boolean isCheckMate(boolean isWhiteKing) {
		//JAquemate en las posiciones actuales?
		if (!this.isCheck(isWhiteKing)) return false;
		//Probamos todas y cada una de los posibles movimientos. Si alguno evita el jaque-amte, return false
		for (Piezas[] y: this.tablero) {
			for (Piezas x: y) {
				
			}
		}
		
		return false;
	}
}
