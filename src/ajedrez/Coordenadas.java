package ajedrez;

import java.util.Scanner;

public class Coordenadas {
	public int coorX; //Coordenada horizontal (A-H)
	public int coorY; //Coordenada vertical (1-8)
	
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
				+ " y luego el n�mero (ej.:A1): ");
		do {
			try {
				input = sc.nextLine().toUpperCase();
				if (input.length() != 2) throw new Exception("Más de dos "
						+ "valores.");
				coorX = Character.getNumericValue(input.charAt(0)) - 10;
				coorY = Character.getNumericValue(input.charAt(1) - 1);
				if (0 > coorX || coorX > 7 || 0 > coorY || coorY > 7) 
					throw new Exception("Fuera de rango.");
				break;
			} catch (Exception e) {
				System.out.println("Coordenadas fuera de rango. Vuelve a "
						+ "intentarlo: ");
			}
		} while (true);
		sc.close();
	}
	
	public Coordenadas addCoordenadas(Coordenadas c) {
		return new Coordenadas(this.coorX+c.coorX, this.coorY+c.coorY);
	}
	
	public Coordenadas addCoordenadas(int x, int y) {
		return this.addCoordenadas(new Coordenadas(x, y));
	}
	
	public boolean dentroTablero() {
		return (0 <= this.coorX && this.coorX <= 7 && 0 <= this.coorY &&
				this.coorY <= 7);
	}
	
	public boolean insideOf(Coordenadas[] coords) {
		for (Coordenadas c: coords) {
			if (this.coorX == c.coorX && this.coorY == c.coorY) return true;
		}
		
		return false;
	}
	
	public void setCoords(int x, int y) {
		this.coorX = x;
		this.coorY = y;
	}
	
	public void setCoords(Coordenadas c) {
		this.coorX = c.coorX;
		this.coorY = c.coorY;
	}
	
	public String toString() {
		return "[" + this.coorX + ", " + this.coorY + "]";
	}
}
