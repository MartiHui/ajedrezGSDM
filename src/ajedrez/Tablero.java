package ajedrez;

public class Tablero {
	public Piezas[][] tablero;
	public String p1, p2;
	
	public Tablero(String p1, String p2) {
		this.tablero = new Piezas[8][8];
		startTablero();
		this.p1 = p1;
		this.p2 = p2;
	}
	
	private void startTablero() {
		//Torres
		tablero[0][0] = new Torre(true);
		tablero[7][0] = new Torre(true);
		tablero[0][7] = new Torre(false);
		tablero[7][7] = new Torre(false);
		//Caballos
		tablero[1][0] = new Caballo(true);
		tablero[6][0] = new Caballo(true);
		tablero[1][7] = new Caballo(false);
		tablero[6][7] = new Caballo(false);
		//Alfiles
		tablero[2][0] = new Alfil(true);
		tablero[5][0] = new Alfil(true);
		tablero[2][7] = new Alfil(false);
		tablero[5][7] = new Alfil(false);
		//Reina
		tablero[3][0] = new Reina(true);
		tablero[3][7] = new Reina(false);
		//Rey
		tablero[4][0] = new Rey(true);
		tablero[4][7] = new Rey(false);
		//Peones
		for (int coorX = 0; coorX < 8; coorX++) {
			tablero[coorX][1] = new Peon(true);
			tablero[coorX][6] = new Peon(false);
		}
	}
	
	public void printTablero() {
		System.out.println("  +-------------------------------+");
		for (int coorY = 0; coorY < 8; coorY++) {
			System.out.print((8-coorY) + " |");
			for (int coorX = 0; coorX < 8; coorX++) {
				System.out.print(" " + (this.tablero[coorX][coorY]==null?' ':this.tablero[coorX][coorY].draw()) + " |");
				
			}
			System.out.println();
			if (coorY != 7) System.out.println("  |---+---+---+---+---+---+---+---|");
			
		}
		System.out.println("Y +-------------------------------+");
		System.out.print("  X A   B   C   D   E   F   G   H");
	}

	public void clonarTablero(Tablero t) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				this.tablero[x][y] = t.tablero[x][y];
			}
		}
		this.p1 = t.p1;
		this.p2 = t.p2;
	}
}
