package ajedrez;

import java.util.Scanner;

public class Partida {
	Controles ctrl;
	Tablero tbl;
	boolean play;
	boolean empate;
	
	public Partida(Scanner sc) {
		System.out.println("Jugador 1: ");
		String p1 = sc.nextLine();
		System.out.println("Jugador 2: ");
		String p2 = sc.nextLine();
		
		this.tbl = new Tablero(p1, p2);
		this.ctrl = new Controles(this.tbl);
		this.play = true;
		this.empate = false;
	}
	
	public void jugar(Scanner sc) {
		boolean moved;
		
		do {
			do {
				this.tbl.printTablero(this.ctrl.isWhiteTurn);
				moved = this.ctrl.moverPieza(sc);
				
				if (!moved) {
					switch (menuJugador(sc)) {
					case 0:
						break;
					case 1:
						empate = aceptarEmpate(sc);
						if (empate) {
							System.out.println("empate aceptado");
							play = false;
							moved = true;
						} else System.out.println("epate deegado");
						break;
					case 2:
						moved = true;
						this.ctrl.changeTurn();
						play = false;
						break;
					default:
						break;
					}
				} 
			} while (!moved);
			
			if (play) {
				//Se comprueba si se ha hecho jaque o jaquemate al OPONENTE
				if (this.tbl.isCheckMate(!this.ctrl.isWhiteTurn)) {
					this.tbl.printTablero(this.ctrl.isWhiteTurn);
					System.out.println("ï¿½JAQUEMATE!");
					play = false;
				} else if (this.tbl.isCheck(!this.ctrl.isWhiteTurn)) {
					System.out.println(
							"Jaque. El rey " + (this.ctrl.isWhiteTurn ? "negro" : "blanco") + " esta bajo amenaza.");
				} 
				
				if (play) this.ctrl.changeTurn();
			}
		} while (play);
		if (empate) System.out.println("La partida ha acabado en empate");
		else System.out.println((this.ctrl.isWhiteTurn?this.tbl.p1:this.tbl.p2) + " es el ganador.");
		System.out.println("Partida acabada.");
	}
	
	public int menuJugador(Scanner sc) {
		System.out.println("Elige una opción:"
				+ "\n 1 - Solicitar empate."
				+ "\n 2 - Rendirse."
				+ "\n\n 0 - Volver");
		int opc = -1;
		do {
			try {
				opc = Integer.parseInt(sc.nextLine());
				if (opc < 0 || opc > 2) throw new Exception();
			} catch (Exception e) {
				System.out.println("Introduce un número");
			}
		} while (opc < 0 || opc > 2);
		return opc;
	}
	
	public boolean aceptarEmpate(Scanner sc) {
		System.out.println((this.ctrl.isWhiteTurn?this.tbl.p1:this.tbl.p2) + " ha solicitado un empate. ¿Aceptas? (Y/N)");
		String opc;
		do {
			opc = sc.nextLine();
		} while (!opc.equalsIgnoreCase("Y") && !opc.equalsIgnoreCase("N"));
		return opc.equalsIgnoreCase("Y");
	}

}
