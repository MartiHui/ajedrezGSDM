package ajedrez;

import java.util.Scanner;

public class Partida {
	Controles ctrl;
	Tablero tbl;
	boolean play;
	
	public Partida(Scanner sc) {
		System.out.println("Jugador 1: ");
		String p1 = sc.nextLine();
		System.out.println("Jugador 2: ");
		String p2 = sc.nextLine();
		
		this.tbl = new Tablero(p1, p2);
		this.ctrl = new Controles(this.tbl);
		this.play = true;
	}
	
	public void jugar(Scanner sc) {
		do {
			this.tbl.printTablero(this.ctrl.isWhiteTurn);
			this.ctrl.moverPieza(sc);
			
			//Se comprueba si se ha hecho jaque o jaquemate al OPONENTE
			if (this.tbl.isCheckMate(!this.ctrl.isWhiteTurn)) {
				System.out.println("¡JAQUEMATE!");
				System.out.println((this.ctrl.isWhiteTurn?this.tbl.p1:this.tbl.p2) + " es el ganador.");
				play = false;
			} else if (this.tbl.isCheck(!this.ctrl.isWhiteTurn)) {
				System.out.println("Jaque. El rey " + (this.ctrl.isWhiteTurn?"negro":"blanco") + " esta bajo amenaza.");
			} 
			
			this.ctrl.changeTurn();
		} while (play);
		
		System.out.println("Partida acabada.");
	}

}
