package ajedrez;

import java.util.Scanner;

public class Main {
	
	//Fuente: DeJavu Sans Mono

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean salir = false;
		ListaPartidas lp = new ListaPartidas();
		Partida p;
		
		do {
			switch (introduccion(sc)) {
			case 0:
				salir = true;
				break;
				
			case 1:
				crearPartida(sc, lp);
				break;
				
			case 2:
				p = elegirPartida(sc, lp);
				p.jugar(sc);
				if (p.gameStatus == 1) {
					System.out.println("Partida guardada");
				} else {
					lp.acabarPartida(p);
				}
				
			case 3:
				borrarPartida(sc, lp);
				break;
				
			case 4:
				lp.mostrarPartidasAcabadas();
				break;
				
			default:
				break;
			}
		} while (!salir);
	}
	
	public static int introduccion(Scanner sc) {
		System.out.println("-----------AJEDREZ-----------"
				+ "\n 1 - Nueva partida"
				+ "\n 2 - Cargar partida"
				+ "\n 3 - Borrar partida"
				+ "\n 4 - Ver resultados"
				+ "\n"
				+ "\n 0 - Salir");
		
		int opc = -1;
		try {
			opc = Integer.parseInt(sc.nextLine());
			if (opc < 0 || opc > 4) throw new Exception();
		} catch (Exception e) {
			System.out.println("Opción no disponible");
		}
		
		return opc;
	}
	
	public static void crearPartida(Scanner sc, ListaPartidas lp) {
		Partida p = new Partida(sc);
		lp.partidasActivas.add(p);
		p.jugar(sc);
		//Cuando salgan de partida se mira el porque
		if (p.gameStatus == 1) {
			System.out.println("Partida guardada");
		} else {
			lp.acabarPartida(p);
		}
	}
	
	public static void borrarPartida(Scanner sc, ListaPartidas lp) {
		int opc = -2;
		
		lp.mostrarPartidasActivas();
		System.out.println("\nElige que partida borrar (elige 0 para no volver atrás): ");
		do {
			try {
				opc = Integer.parseInt(sc.nextLine()) - 1;
				if (opc < -1 || opc > lp.partidasActivas.size()) throw new Exception();
			} catch (Exception e) {
				System.out.println("Opción no disponible.");
			}
		} while (opc < -1 || opc > lp.partidasActivas.size());
		
		if (opc != -1) lp.partidasActivas.remove(opc); 
	}
	
	public static Partida elegirPartida(Scanner sc, ListaPartidas lp) {
		
	}

}
