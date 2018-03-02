package ajedrez;

import java.util.LinkedList;

public class Tablero {
	public Piezas[][] tablero; //Tablero y piezas
	public LinkedList<Piezas> piezasBlancasMuertas; //RIP
	public LinkedList<Piezas> piezasNegrasMuertas;
	public String p1, p2; //Nombre de los jugadores
	
	public Tablero(String p1, String p2) {
		this.tablero = new Piezas[8][8];
		startTablero();
		this.piezasBlancasMuertas = new LinkedList<Piezas>();
		this.piezasNegrasMuertas = new LinkedList<Piezas>();
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
	
	public void printTablero(Coordenadas[] legalMoves, boolean isWhiteTurn) {
		Coordenadas coord = new Coordenadas(0, 0);
		boolean specialSquare;
		
		System.out.println("  +-------------------------------+");
		for (int coorY = 7; coorY > -1; coorY--) {
			System.out.print((coorY+1) + " |");
			for (int coorX = 0; coorX < 8; coorX++) {
				coord.setCoords(coorX,  coorY);
				specialSquare = 
						legalMoves!=null?coord.insideOf(legalMoves):false; //legalMoves null -> siempre false
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
			switch (coorY) {
			case 7:
				System.out.print((isWhiteTurn?"    ":" -> ") + "Negras: " + this.p2);
				break;
			case 6:
				System.out.print("    ");
				for (Piezas p: this.piezasBlancasMuertas.subList(0, Math.min(this.piezasBlancasMuertas.size(), 5))) {
					System.out.print(p);
				}
				break;
			case 5:
				System.out.print("    ");
				if (this.piezasBlancasMuertas.size() > 5) {
					for (Piezas p: this.piezasBlancasMuertas.subList(5, Math.min(this.piezasBlancasMuertas.size(), 10))) {
						System.out.print(p);
					}
				}
				break;
			case 4:
				System.out.print("    ");
				if (this.piezasBlancasMuertas.size() > 10) {
					for (Piezas p: this.piezasBlancasMuertas.subList(10, Math.min(this.piezasBlancasMuertas.size(), 15))) {
						System.out.print(p);
					}
				}
				break;
			case 3:
				System.out.print("    ");
				if (this.piezasNegrasMuertas.size() > 10) {
					for (Piezas p: this.piezasNegrasMuertas.subList(10, Math.min(this.piezasNegrasMuertas.size(), 15))) {
						System.out.print(p);
					}
				}
				break;
			case 2:
				System.out.print("    ");
				if (this.piezasNegrasMuertas.size() > 5) {
					for (Piezas p: this.piezasNegrasMuertas.subList(5, Math.min(this.piezasNegrasMuertas.size(), 10))) {
						System.out.print(p);
					}
				}
				break;
			case 1:
				System.out.print("    ");
				for (Piezas p: this.piezasNegrasMuertas.subList(0, Math.min(this.piezasNegrasMuertas.size(), 5))) {
					System.out.print(p);
				}
				break;
			case 0:
				System.out.print((isWhiteTurn?" -> ":"    ") + "Blancas: " + this.p1);
				break;
			default:
				break;
			}
			System.out.println();
			if (coorY != 0) System.out.println
			("  |---+---+---+---+---+---+---+---|");
			
		}
		System.out.println("Y +-------------------------------+");
		System.out.print("  X A   B   C   D   E   F   G   H");
		System.out.println("\n");
	}
	
	public void printTablero(boolean isWhiteTurn) {
		printTablero(null, isWhiteTurn);
	}
	
	public Piezas getCasilla(Coordenadas coor) {
		return this.tablero[coor.coorX][coor.coorY];
	}
	
	public void setCasilla(Coordenadas coor, Piezas p) {
		this.tablero[coor.coorX][coor.coorY] = p;
	}
	
	public void movePieza(Coordenadas org, Coordenadas dst) {
		if (this.getCasilla(dst) != null) {
			if (this.getCasilla(dst).isWhite) {
				this.piezasBlancasMuertas.add(this.getCasilla(dst));
			} else {
				this.piezasNegrasMuertas.add(this.getCasilla(dst));
			}
			this.getCasilla(dst).killPieza();
		}
		this.setCasilla(dst, this.getCasilla(org));
		this.setCasilla(org, null);
	}
	
	public boolean isCheck(boolean isWhiteKing) {
		for (Piezas[] y: tablero) {
			for (Piezas x: y) {
				if (x != null && x.isWhite != isWhiteKing && x.canKillKing(this))
					return true;
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
		for (Piezas[] y: this.tablero) {
			for (Piezas x: y) {
				if (x != null && x.isWhite == isWhiteKing)
					if (x.legalMoves(this).length > 0) return false;
			}
		}
		
		return true;
	}
}
