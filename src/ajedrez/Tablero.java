package ajedrez;

import java.io.Serializable;
import java.util.LinkedList;

public class Tablero implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1332063901361324268L;
	/**
	 * 
	 */
	public Piezas[][] tablero; //Tablero y piezas
	public LinkedList<Piezas> piezasBlancasMuertas; //RIP
	public LinkedList<Piezas> piezasNegrasMuertas;
	public String p1, p2; //Nombre de los jugadores
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
		//El tablero girara para que se muestre tal como lo veria cada jugador en la vida real
		Coordenadas coord = new Coordenadas(0, 0);
		boolean specialSquare;
		int[] columnas = isWhiteTurn?new int[] {7, 6, 5, 4, 3, 2, 1, 0}:new int[] {0, 1, 2, 3, 4, 5, 6, 7};
		int[] filas =isWhiteTurn?new int[] {0, 1, 2, 3, 4, 5, 6, 7}:new int[] {7, 6, 5, 4, 3, 2, 1, 0};
		
		System.out.println(isWhiteTurn?"    A   B   C   D   E   F   G   H X":"    H   G   F   E   D   C   B   A X");
		System.out.println("  +-------------------------------+ Y");
		for (int coorY: columnas) {
			System.out.print((coorY+1) + " |");
			for (int coorX : filas) {
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
			System.out.print(" " + (coorY+1));
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
			if ((isWhiteTurn && coorY != 0) || (!isWhiteTurn && coorY != 7)) 
				System.out.println("  |---+---+---+---+---+---+---+---|");
			
		}
		System.out.println("Y +-------------------------------+");
		System.out.println(isWhiteTurn?"    A   B   C   D   E   F   G   H X":"    H   G   F   E   D   C   B   A X");
		System.out.println("          Enter: abrir menú\n");
		
		if (!this.msg.equals("")) {
			System.out.println("-----------------------------------------"
					+ "\n" + msg
					+ "\n-----------------------------------------");
			this.msg = "";
		}
		
		System.out.println("------Turno de " + (isWhiteTurn?this.p1:this.p2) + "------");
	}
	
	public void printTablero(boolean isWhiteTurn) {
		printTablero(null, isWhiteTurn);
	}
	
	public void setMsg(String str) {
		this.msg = str;
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
			this.getCasilla(dst).killPieza(this);
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
	
	public int refreshGameStatus(boolean isWhiteKing, Movimiento mAnterior) {
		boolean canMove = false;
		for (Piezas[] y: this.tablero) {
			for (Piezas x: y) {
				if (x != null && x.isWhite == isWhiteKing)
					if (x.legalMoves(this, mAnterior).length > 0) {
						canMove = true;
						break;
					}
			}
		}
		
		if (!canMove) {
			if (this.isCheck(isWhiteKing)) return -1; //Si no tiene movimientos y esta en jaque, es jaque mate
			else {
				this.setMsg("No hay movimientos legales.");
				return 0; //Si no tiene movimientos pero no esta en jaque, es stalemate
			}
		} else {
			/*Existen combinaciones de piezas que hacen imposible conseguir un jaquemate y las partidas se alargarían
			 * infinitamente:
			 * Rey vs Rey
			 * Rey y Alfil vs Rey
			 * Rey y Caballo vs Rey
			 */
			LinkedList<Coordenadas> piezas = new LinkedList<Coordenadas>();
			
			for (Piezas[] y: this.tablero) {
				for (Piezas x: y) {
					if (x != null)
						piezas.add(x.posicion);
					if (piezas.size() > 3) return 1; //Si hay mas de 3 piezas, no puede ser ninguna de las combinaciones
				}
			}
			if (piezas.size() == 2) return 0; //Quedan los dos reyes, es un empate
			//Como tenemos solo 3 piezas, dos de ellas tienen que ser necesariamente los dos reyes
			//Por lo que solo tenemos que ver si entre las tres piezas hay un alfil o un caballo, da igual el color
			for (Coordenadas c: piezas) {
				if (!((this.getCasilla(c) instanceof Alfil) 
						|| (this.getCasilla(c) instanceof Caballo)
						|| (this.getCasilla(c) instanceof Rey))) return 1; //Si la pieza es diferente de alfil, caballo y rey
			}
		}
		this.setMsg("No hay suficientes piezas para lograr un jaquemate.");
		return 0; //Las piezas son alguna de las combinaciones de empate
	}
}
