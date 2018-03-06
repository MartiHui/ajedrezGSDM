package ajedrez;

import java.util.LinkedList;

public class ListaPartidas {
	public LinkedList<Partida> partidasActivas;
	private LinkedList<Partida> partidasAcabadas;
	
	public ListaPartidas() {
		this.partidasActivas = new LinkedList<Partida>();
		this.partidasAcabadas = new LinkedList<Partida>();
	}
	
	public void acabarPartida(Partida p) {
		this.partidasAcabadas.add(p);
		this.partidasActivas.remove(this.partidasActivas.indexOf(p));
	}
	
	public void mostrarPartidasAcabadas() {
		if (this.partidasAcabadas.size() == 0) System.out.println("No hay ninguna partida.");
		else 
			for (int i = this.partidasAcabadas.size(); i >= 0; i++) {
				System.out.println("Partida " + (i+1) + " - " + this.partidasAcabadas.get(i).estadoPartida());
			}
	}
	
	public void mostrarPartidasActivas() {
		if (this.partidasActivas.size() == 0) System.out.println("No hay ninguna partida.");
		else 
			for (int i = this.partidasActivas.size(); i >= 0; i++) {
				System.out.println("Partida " + (i+1) + " - " + this.partidasActivas.get(i).estadoPartida());
			}
	}
	
}
