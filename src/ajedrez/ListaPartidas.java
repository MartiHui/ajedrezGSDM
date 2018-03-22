package ajedrez;

import java.io.Serializable;
import java.util.LinkedList;

public class ListaPartidas implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1530727306588135159L;
	public LinkedList<Partida> partidasActivas;
	public LinkedList<Partida> partidasAcabadas;
	/*
	 * Añadido para el examen. Marcador que lleva cuantas veces ganan las blancas y las negras.
	 *	
	 * El primer valor del array lleva el marcador de las blancas, el segundo, de las negras.
	 */
	public int[] marcador = new int[] {0, 0}; 
	
	public ListaPartidas() {
		this.partidasActivas = new LinkedList<Partida>();
		this.partidasAcabadas = new LinkedList<Partida>();
	}
	
	public void acabarPartida(Partida p) {
		if (p.gameStatus == -1) { // Si no han empatado
			if (p.ctrl.isWhiteTurn) // Cuando la partida acaba, es el turno del ganador 
				marcador[0]++;
			else 
				marcador[1]++;
		}
		
		this.partidasAcabadas.add(p);
		this.partidasActivas.remove(this.partidasActivas.indexOf(p));
	}
	
	public void mostrarPartidasAcabadas() {
		if (this.partidasAcabadas.size() == 0) 
			System.out.println("No hay ninguna partida.");
		else 
			for (int i = this.partidasAcabadas.size()-1; i >= 0; i--) {
				System.out.println("Partida " + (i+1) + " - " 
						+ this.partidasAcabadas.get(i).estadoPartida());
			}
	}
	
	public void mostrarPartidasActivas() {
		if (this.partidasActivas.size() == 0) 
			System.out.println("No hay ninguna partida.");
		else 
			System.out.println("Blancas " + marcador[0] + " - " + marcador[1] + " Negras");
			for (int i = this.partidasActivas.size()-1; i >= 0; i--) {
				System.out.println("Partida " + (i+1) + " - " 
						+ this.partidasActivas.get(i).estadoPartida());
			}
	}
	
}
