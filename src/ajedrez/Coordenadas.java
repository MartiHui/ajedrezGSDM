package ajedrez;

import java.io.Serializable;
import java.util.Scanner;

public class Coordenadas implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1010018348824770712L;
	/**
	 * 
	 */
	public int coorX; //Coordenada horizontal (A-H)
	public int coorY; //Coordenada vertical (1-8)
	
	public Coordenadas(int coorX, int coorY) {
		this.coorX = coorX;
		this.coorY = coorY;
	}
	
	public Coordenadas(Scanner sc) {
		this.coorX = 0;
		this.coorY = 0;
		getCoordenadas(sc);
	}
	
	public void getCoordenadas(Scanner sc) {
		
		do {
			try {
				String input = sc.nextLine();
				
				if (input.length() == 0) {
					this.coorX = -1;
					this.coorY = -1;
					return;
				}
				
				if (input.length() != 2) throw new Exception();
				
				this.coorX = Character.getNumericValue(input.charAt(0)) - 10;
				this.coorY = Character.getNumericValue(input.charAt(1) - 1);
				
				if (0 > this.coorX || this.coorX > 7 || 0 > this.coorY || this.coorY > 7) throw new Exception();
				
				return;
			} catch (Exception e) {
				System.out.println("Formato incorrecto.");
			}
		} while (true);
	}
	
	public Coordenadas addCoordenadas(Coordenadas c) {
		return new Coordenadas(this.coorX+c.coorX, this.coorY+c.coorY);
	}
	
	public Coordenadas addCoordenadas(int x, int y) {
		return this.addCoordenadas(new Coordenadas(x, y));
	}
	
	public Coordenadas distanceCasilla(Coordenadas c) {
		return new Coordenadas(c.coorX-this.coorX, c.coorY-this.coorY);
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
		this.setCoords(c.coorX, c.coorY);
	}
	
	public String toString() {
		return "[" + this.coorX + ", " + this.coorY + "]";
	}
	
	public boolean isEmpty() {
		return (this.coorX == -1 && this.coorY == -1);
	}
	
	public boolean equals(Coordenadas c) {
		return (this.coorX == c.coorX && this.coorY == c.coorY);
	}
	
}
