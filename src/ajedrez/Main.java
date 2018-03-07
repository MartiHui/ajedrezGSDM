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
		salirPartida(p, lp);
	}
	
	public static void borrarPartida(Scanner sc, ListaPartidas lp) {
		int opc = -2;
		
		lp.mostrarPartidasActivas();
		if (lp.partidasActivas.size() == 0) return;
		
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
	
	public static void elegirPartida(Scanner sc, ListaPartidas lp) {
		int opc = -2;
		Partida p;
		
		lp.mostrarPartidasActivas();
		if (lp.partidasActivas.size() == 0) return;
		
		System.out.println("\nElige que partida cargar (elige 0 para no volver atrás): ");
		do {
			try {
				opc = Integer.parseInt(sc.nextLine()) - 1;
				if (opc < -1 || opc > lp.partidasActivas.size()) throw new Exception();
			} catch (Exception e) {
				System.out.println("Opción no disponible.");
			}
		} while (opc < -1 || opc > lp.partidasActivas.size());
		
		if (opc == -1) return;
		
		p = lp.partidasActivas.get(opc);
		p.jugar(sc);
		salirPartida(p, lp);
	}
	
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
