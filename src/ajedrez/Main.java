package ajedrez;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Main {
	
	//Fuente: DeJavu Sans Mono

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean salir = false;
		ListaPartidas lp = new ListaPartidas();
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.dat"));
			readData(ois, lp);
			ois.close();
		} catch (Exception e) {}
		
		do {
			switch (introduccion(sc)) {
				case 0:
					salir = true;
					break;
					
				case 1:
					crearPartida(sc, lp);
					break;
					
				case 2:
					elegirPartida(sc, lp);
					break;
					
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
		
		System.out.println("Guardando...");
		saveData(lp);
		System.out.println("Programa cerrado.");
	}
	
	// Método general para cuando tenemos que elegir una opcion
	public static int selectOpc(Scanner sc, int min, int max) {
		do {
			try {
				int opc = Integer.parseInt(sc.nextLine());
				if (opc < min || opc > max) throw new Exception();
				return opc;
			} catch (Exception e) {
				System.out.println("Opción no disponible");
			}
		} while (true);
	}
	
	// Menu general
	public static int introduccion(Scanner sc) {
		System.out.println("-----------AJEDREZ-----------"
				+ "\n 1 - Nueva partida"
				+ "\n 2 - Cargar partida"
				+ "\n 3 - Borrar partida"
				+ "\n 4 - Ver resultados"
				+ "\n"
				+ "\n 0 - Salir");
		
		return selectOpc(sc, 0, 4);
	}
	
	// Crea una nueva partida, la añade a la lista y la empieza automaticamente
	public static void crearPartida(Scanner sc, ListaPartidas lp) {
		Partida p = new Partida(sc);
		lp.partidasActivas.add(p);
		jugarPartida(p, lp, sc);
	}
	
	// Borramos alguna partida de la lista de partidas activas
	public static void borrarPartida(Scanner sc, ListaPartidas lp) {
		lp.mostrarPartidasActivas();
		
		if (lp.partidasActivas.size() == 0) // No hay ninguna partida que borrar
			return;
		
		System.out.println("\nElige que partida borrar (elige 0 para no volver atrás): ");
		int opc = selectOpc(sc, -1, lp.partidasActivas.size());
		
		if (opc != -1) 
			lp.partidasActivas.remove(opc); 
	}
	
	// Cargamos alguna partida activa
	public static void elegirPartida(Scanner sc, ListaPartidas lp) {
		lp.mostrarPartidasActivas();
		
		if (lp.partidasActivas.size() == 0) 
			return;
		
		System.out.println("\nElige que partida cargar (elige 0 para no volver atrás): ");
		int opc = selectOpc(sc, -1, lp.partidasActivas.size());
		
		if (opc == -1) 
			return;
		
		Partida p = lp.partidasActivas.get(opc);
		jugarPartida(p, lp, sc);
	}
	
	public static void jugarPartida(Partida p, ListaPartidas lp, Scanner sc) {
		p.jugar(sc);
		salirPartida(p, lp);
	}
	
	// Mira si ha salido de partida por empate o jaquemate, o si se puede continuar más tarde
	public static void salirPartida(Partida p, ListaPartidas lp) {
		if (p.gameStatus == 2) {
			System.out.println("Partida guardada");
		} else {
			lp.acabarPartida(p);
		}
	}
	
	public static void saveData(ListaPartidas lp) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.dat"));
			oos.writeObject(lp);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readData(ObjectInputStream ois, ListaPartidas lp) {
		try {
			lp = (ListaPartidas) ois.readObject();
		} catch (Exception e) {}
	}

}
