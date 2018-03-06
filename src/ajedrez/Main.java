package ajedrez;

import java.util.Scanner;

public class Main {
	
	//Fuente: DeJavu Sans Mono

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Partida part = new Partida(sc);
		part.jugar(sc);
	}

}
