package ajedrez;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Scanner;

public class Partida implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5416013799361183948L;
	Controles ctrl; 
	Tablero tbl;
	int gameStatus; // 2:pausado 1:jugando 0:empate -1:derrota
	int numRondas;
	LinkedList<Movimiento> movimientos; // Guarda odos los movimientos que se van realizando
	
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
		// Añadimos un primer elemento a la lista para evitar excepciones
		this.movimientos.add(null);
	}
	
	public void jugar(Scanner sc) {
		// Si la partida estaba pausada, se actualiza el status a jugando
		if (this.gameStatus == 2) 
			this.gameStatus = 1;
		
		do {
			if (this.ctrl.isWhiteTurn) 
				this.numRondas++;
			
			boolean continueGame;
			do {
				this.tbl.printTablero(this.ctrl.isWhiteTurn);
				continueGame = this.ctrl.moverPieza(sc, movimientos);
				
				// Si no ha elegido una pieza, abre el menú
				if (!continueGame) {
					switch (menuJugador(sc)) {
						case 0: // Salir del menu
							break;
							
						case 1: // Pide un empate al oponente
							if (aceptarEmpate(sc)) {
								this.tbl.setMsg("~~~~~~~~~~~~EMPATE PACTADO~~~~~~~~~~~~");
								this.gameStatus = 0;
								continueGame = true;
							} else 
								this.tbl.setMsg("Empate denegado.");
							
							break;
						
						case 2: // El jugador se rinde
							continueGame = true;
							this.tbl.setMsg((this.ctrl.isWhiteTurn?this.tbl.p1:this.tbl.p2) 
									+ " SE HA RENDIDO");
							this.ctrl.changeTurn(); // Si te rindes, gana el otro jugador
							this.gameStatus = -1;
							
							break;
						
						case 3: // Muestra las instrucciones de como jugar
							System.out.println("\nPara elegir una pieza o casilla, escribe las coordenadas,"
									+ "\nprimero la letra y luego el n�mero; por ejemplo, A1."
									+ "\nLa partida finalizar� autom�ticamente en caso de jaquemate o"
									+ "\nde empate. El empate puede ser porque las piezas disponibles"
									+ "\nhacen imposible el jaquemate a ambos jugadores o un jugador"
									+ "\nno dispone de movimientos posibles."
									+ "\n\nEnter para continuar...");
							sc.nextLine();
							
							break;
						
						case 4: // Retoceder hasta el turno anterior del jugador
							if (movimientos.size() >= 3) 
								retrocederTurno();
							else // Si es el primer turno del jugador en toda la partida
								this.tbl.setMsg("No se puede retroceder");
							
							break;
						
						case 5: // Sale de la partida aunque no sea empate o jaquemate
							continueGame = true;
							this.gameStatus = 2;
							
							break;
					}
				} 
			} while (!continueGame);
			
			if (this.gameStatus == 1) { // Ni se han rendido, empatado o salido de partida
				// Actualizamos el estado según el movimento realizado por el jugador
				this.gameStatus = this.tbl.refreshGameStatus
						(!this.ctrl.isWhiteTurn, this.movimientos.getLast()); 
				
				if (this.gameStatus == -1) {
					this.tbl.setMsg("~~~~~~~~~~~~~~[JAQUEMATE]~~~~~~~~~~~~~~");
				} else if (this.gameStatus == 0) {
					// No hacemos nada. El caso de empate se  maneja en el tablero
				} else {
					if (this.tbl.isCheck(!this.ctrl.isWhiteTurn)) 
						this.tbl.setMsg("El rey " + (this.ctrl.isWhiteTurn?" negro ":" blanco ") 
							+ " esta en jaque.");
					
					this.ctrl.changeTurn();
				}
			}
		} while (this.gameStatus == 1);
		
		if (this.gameStatus == 0) {
			this.tbl.printTablero(this.ctrl.isWhiteTurn);
			System.out.println("----------------EMPATE----------------"
					+ "\nNing�n jugador ha ganado."
					+ "\n--------------------------------------");
		} else if (this.gameStatus == -1) {
			this.tbl.printTablero(this.ctrl.isWhiteTurn);
			System.out.println("--------------VICTORIA--------------\n" 
					+ (this.ctrl.isWhiteTurn?this.tbl.p1:this.tbl.p2) + " es el ganador"
					+ "\n------------------------------------");
		}
			
		if (this.gameStatus != 2) 
			System.out.println("Partida acabada. N�mero de rondas:" + this.numRondas);
	}
	
	public int menuJugador(Scanner sc) {
		int opc = -1;
		
		do {
			System.out.println("Elige una opci�n:"
					+ "\n 1 - Solicitar empate."
					+ "\n 2 - Rendirse."
					+ "\n 3 - �Como jugar?"
					+ "\n 4 - Retrocedes un turno"
					+ "\n 5 - Salir de la partida."
					+ "\n\n 0 - Volver");
			
			try {
				opc = Integer.parseInt(sc.nextLine());
				if (opc < 0 || opc > 5) 
					throw new Exception();
			} catch (Exception e) {
				System.out.println();
			}
		} while (opc < 0 || opc > 5);
		
		return opc;
	}
	
	public boolean aceptarEmpate(Scanner sc) {
		System.out.println((this.ctrl.isWhiteTurn?this.tbl.p1:this.tbl.p2) 
				+ " ha solicitado un empate. �Aceptas? (Y/N)");
		String opc;
		
		do {
			opc = sc.nextLine(); // Solo esta permitido responder y o n
		} while (!opc.equalsIgnoreCase("Y") && !opc.equalsIgnoreCase("N"));
		
		return opc.equalsIgnoreCase("Y");
	}
	
	public boolean partidaAcabada() {
		return this.gameStatus != 1;
	}
	
	public String estadoPartida() {
		String estado = this.tbl.p1 + " VS " + this.tbl.p2;
		if (this.gameStatus == 0) {
			estado += "  | EMPATE tras ";
		} else if (this.gameStatus == -1) {
			estado += "  | VICTORIA de " + (this.ctrl.isWhiteTurn?this.tbl.p1:this.tbl.p2) +
					" tras ";
		} else {
			estado += "  | Partida en curso. Han pasado ";
		}
		
		return estado + this.numRondas + " rondas."; 
	}
	
	/*
	 * Si queremos volver al turno anterior del jugador tenemos que deshacer dos movimientos:
	 * el anterior del oponente y el que hizo el jugador en su turno anterior
	 */
	public void retrocederTurno() {
		this.numRondas--;
		
		this.movimientos.getLast().deshacerMovimiento(tbl);
		this.movimientos.removeLast();
		
		this.movimientos.getLast().deshacerMovimiento(tbl);
		this.movimientos.removeLast();
		
		this.tbl.setMsg("Has retrocedido un turno");
	}

}
