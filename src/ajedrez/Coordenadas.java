package ajedrez;

import java.util.Scanner;

public class Coordenadas {
	public int coorX;
	public int coorY;
	
	public Coordenadas(int coorX, int coorY) {
		this.coorX = coorX;
		this.coorY = coorY;
	}
	
	public Coordenadas() {
		this.coorX = 0;
		this.coorY = 0;
		getCoordenadas();
	}
	
	public void getCoordenadas() {
		Scanner sc = new Scanner(System.in);
		String input;
		
		System.out.println("Introduce las coordenadas. Primero la letra"
				+ " y luego el número (ej.:A1): ");
		do {
			try {
				input = sc.nextLine().toUpperCase();
				if (input.length() != 2) throw new Exception("Más de dos valores.");
				coorX = Character.getNumericValue(input.charAt(0)) - 10;
				coorY = Character.getNumericValue(input.charAt(1) - 1);
				if (0 > coorX || coorX > 7 || 0 > coorY || coorY > 7) 
					throw new Exception("Fuera de rango.");
				break;
			} catch (Exception e) {
				System.out.println("Coordenadas fuera de rango. Vuelve a intentarlo: ");
			}
		} while (true);
		sc.close();
	}
	
	public Coordenadas addCoordenadas(Coordenadas c) {
		Coordenadas total = new Coordenadas(this.coorX+c.coorX, this.coorY+c.coorY);
		if (total.dentroTablero()) return total;
		else return null;
	}
	
	public boolean dentroTablero() {
		return (0 <= this.coorX && this.coorX <= 7 && 0 <= this.coorY && this.coorY <= 7);
	}
}
