package ajedrez;

import java.util.Scanner;

public class Partida {
	Controles ctrl;
	Tablero tbl;
	int gameStatus; //1: jugar 0:empate -1:derrota
	int numRondas;
	
	public Partida(Scanner sc) {
		System.out.println("Set blanco. Nombre del jugador: ");
		String p1 = sc.nextLine();
		System.out.println("Set negro. Nombre del jugador: ");
		String p2 = sc.nextLine();
		
		this.tbl = new Tablero(p1, p2);
		this.ctrl = new Controles(this.tbl);
		this.gameStatus = 1;
		this.numRondas = 0;
	}
	
	public void jugar(Scanner sc) {
		boolean moved, empate;
		
		do {
			if (this.ctrl.isWhiteTurn) this.numRondas++;
			
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
							this.tbl.printTablero(this.ctrl.isWhiteTurn);
							System.out.println("~~~~~~~~~~~~EMPATE PACTADO~~~~~~~~~~~~");
							this.gameStatus = 0;
							moved = true;
						} else System.out.println("Empate denegado");
						break;
					case 2:
						moved = true;
						this.ctrl.changeTurn(); //Si te rindes, gana el otro jugador
						this.gameStatus = -1;
						break;
					case 3:
						System.out.println("\nPara elegir una pieza o casilla, escribe las coordenadas,"
								+ "\nprimero la letra y luego el número; por ejemplo, A1."
								+ "\nLa partida finalizará automáticamente en caso de jaquemate o"
								+ "\nde empate. El empate puede ser porque las piezas disponibles"
								+ "\nhacen imposible el jaquemate a ambos jugadores o un jugador"
								+ "\nno dispone de movimientos posibles."
								+ "\n\nEnter para continuar...");
						sc.nextLine();
					}
				} 
			} while (!moved);
			//Si ha movido pieza en vez de rendirse o empatar
			if (this.gameStatus == 1) {
				//Se actuaiza el estado del juego y tiene que ser -1, 0 o 1
				this.gameStatus = this.tbl.refreshGameStatus(!this.ctrl.isWhiteTurn);
				
				if (this.gameStatus == -1) {
					this.tbl.printTablero(this.ctrl.isWhiteTurn);
					System.out.println("~~~~~~~~~~~~~~[JAQUEMATE]~~~~~~~~~~~~~~");
				} else if (this.gameStatus == 0) {
					
				} else {
					if (this.tbl.isCheck(!this.ctrl.isWhiteTurn)) 
						System.out.println("El rey " + (this.ctrl.isWhiteTurn?" negro ":" blanco ") + " esta en jaque.");
					this.ctrl.changeTurn();
				}
			}
		} while (this.gameStatus == 1);
		
		if (this.gameStatus == 0) {
			System.out.println("----------------EMPATE----------------"
					+ "\nNingún jugador ha ganado."
					+ "\n--------------------------------------");
		}
			
		else  {
			System.out.println("--------------VICTORIA--------------\n" 
					+ (this.ctrl.isWhiteTurn?this.tbl.p1:this.tbl.p2) + " es el ganador"
					+ "\n------------------------------------");
		}
			
		System.out.println("Partida acabada. Número de rondas:" + this.numRondas);
	}
	
	public int menuJugador(Scanner sc) {
		int opc = -1;
		
		do {
			System.out.println("Elige una opción:"
					+ "\n 1 - Solicitar empate."
					+ "\n 2 - Rendirse."
					+ "\n 3 - ¿Como jugar?"
					+ "\n\n 0 - Volver");
			
			try {
				opc = Integer.parseInt(sc.nextLine());
				if (opc < 0 || opc > 3) throw new Exception();
			} catch (Exception e) {
				System.out.println();
			}
		} while (opc < 0 || opc > 3);
		
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
	
	public boolean partidaAcabada() {
		return this.gameStatus != 1;
	}
	
	public String estadoPartida() {
		if (this.gameStatus == 1) {
			return (this.tbl.p1 + " VS " + this.tbl.p2 + "  |  Ronda nº" + this.numRondas + ".");
		} else if (this.gameStatus == 0) {
			return (this.tbl.p1 + " VS " + this.tbl.p2 + "  | EMPATE tras " + this.numRondas + " rondas.");
		} else {
			return (this.tbl.p1 + " VS " + this.tbl.p2 + "  | VICTORIA de " + (this.ctrl.isWhiteTurn?this.tbl.p1:this.tbl.p2) +
					" tras " + this.numRondas + " rondas.");
		}
	}

}
