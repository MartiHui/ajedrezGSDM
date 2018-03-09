package ajedrez;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Scanner;

public class Partida implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1757931269172083173L;
	Controles ctrl;
	Tablero tbl;
	int gameStatus; //2:pausado 1: jugar 0:empate -1:derrota
	int numRondas;
	LinkedList<Movimiento> movimientos;
	
	public Partida(Scanner sc) {
		System.out.println("Set blanco. Nombre del jugador: ");
		String p1 = sc.nextLine();
		System.out.println("Set negro. Nombre del jugador: ");
		String p2 = sc.nextLine();
		
		this.tbl = new Tablero(p1, p2);
		this.ctrl = new Controles(this.tbl);
		this.gameStatus = 1;
		this.numRondas = 0;
		this.movimientos = new LinkedList<Movimiento>();
		this.movimientos.add(null);
	}
	
	public void jugar(Scanner sc) {
		boolean moved, empate;
		
		if (this.gameStatus == 2) this.gameStatus = 1;
		
		do {
			if (this.ctrl.isWhiteTurn) this.numRondas++;
			
			do {
				this.tbl.printTablero(this.ctrl.isWhiteTurn);
				moved = this.ctrl.moverPieza(sc, movimientos);
				
				if (!moved) {
					switch (menuJugador(sc)) {
					case 0:
						break;
					case 1:
						empate = aceptarEmpate(sc);
						if (empate) {
							this.tbl.setMsg("~~~~~~~~~~~~EMPATE PACTADO~~~~~~~~~~~~");
							this.gameStatus = 0;
							moved = true;
						} else this.tbl.setMsg("Empate denegado.");
						break;
					case 2:
						moved = true;
						this.tbl.setMsg((this.ctrl.isWhiteTurn?this.tbl.p1:this.tbl.p2) + " SE HA RENDIDO");
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
						break;
					case 4:
						moved = true;
						this.gameStatus = 2;
						break;
					}
				} 
			} while (!moved);
			//Si ha movido pieza en vez de rendirse o empatar
			if (this.gameStatus == 1) {
				//Se actuaiza el estado del juego y tiene que ser -1, 0 o 1
				this.gameStatus = this.tbl.refreshGameStatus(!this.ctrl.isWhiteTurn, this.movimientos.getLast()); 
				
				if (this.gameStatus == -1) {
					this.tbl.setMsg("~~~~~~~~~~~~~~[JAQUEMATE]~~~~~~~~~~~~~~");
				} else if (this.gameStatus == 0) {
					
				} else {
					if (this.tbl.isCheck(!this.ctrl.isWhiteTurn)) 
						this.tbl.setMsg("El rey " + (this.ctrl.isWhiteTurn?" negro ":" blanco ") + " esta en jaque.");
					this.ctrl.changeTurn();
				}
			}
		} while (this.gameStatus == 1);
		
		if (this.gameStatus == 0) {
			this.tbl.printTablero(this.ctrl.isWhiteTurn);
			System.out.println("----------------EMPATE----------------"
					+ "\nNingún jugador ha ganado."
					+ "\n--------------------------------------");
		} else if (this.gameStatus == -1) {
			this.tbl.printTablero(this.ctrl.isWhiteTurn);
			System.out.println("--------------VICTORIA--------------\n" 
					+ (this.ctrl.isWhiteTurn?this.tbl.p1:this.tbl.p2) + " es el ganador"
					+ "\n------------------------------------");
		}
			
		if (this.gameStatus != 2) System.out.println("Partida acabada. Número de rondas:" + this.numRondas);
	}
	
	public int menuJugador(Scanner sc) {
		int opc = -1;
		
		do {
			System.out.println("Elige una opción:"
					+ "\n 1 - Solicitar empate."
					+ "\n 2 - Rendirse."
					+ "\n 3 - ¿Como jugar?"
					+ "\n 4 - Salir de la partida."
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
		String estado = this.tbl.p1 + " VS " + this.tbl.p2;
		if (this.gameStatus == 1) {
			return (estado + "  |  Ronda nº" + this.numRondas + ".");
		} else if (this.gameStatus == 0) {
			return (estado + "  | EMPATE tras " + this.numRondas + " rondas.");
		} else if (this.gameStatus == -1) {
			return (estado + "  | VICTORIA de " + (this.ctrl.isWhiteTurn?this.tbl.p1:this.tbl.p2) +
					" tras " + this.numRondas + " rondas.");
		} else {
			return (estado + "  | Partida en curso. Ronda nº" + this.numRondas + " rondas.");
		}
	}

}
