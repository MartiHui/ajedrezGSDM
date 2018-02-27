package ajedrez;

public class Main {
	
	//Fuente: DeJavu Sans Mono

	public static void main(String[] args) {
		Tablero t = new Tablero("as", "dsada");
		

		for (Coordenadas x: t.getCasilla(new Coordenadas(5, 6)).legalMoves(t)) {
			System.out.println(x);
		}
		t.printTablero(t.getCasilla(new Coordenadas(5, 6)).legalMoves(t));
	}

}
