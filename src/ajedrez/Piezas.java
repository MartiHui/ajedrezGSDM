package ajedrez;

import java.util.LinkedList;

public abstract class Piezas {
	public boolean alive;
	public boolean isWhite;
	public boolean limitedMove;
	
	public Piezas(boolean isWhite, boolean limitedMove) {
		this.alive = true;
		this.isWhite = isWhite;
		this.limitedMove = limitedMove;
	}
	
	public abstract int[][] allowedMove();
	
	public abstract char draw();
}

class Peon extends Piezas {
	public boolean canAttack;
	public boolean firstMove;
	
	public Peon(boolean isBlanca) {
		super(isBlanca, true);
		this.canAttack = false;
		this.firstMove = true;
	}

	@Override
	public int[][] allowedMove() {
		int coorY = this.isWhite?1:-1;
		LinkedList<int[]> m = new LinkedList<int[]>();
		
		m.add(new int[] {0, coorY});
		if (this.firstMove) m.add(new int[] {0, coorY*2});
		if (this.canAttack) {
			m.add(new int[] {-1, coorY});
			m.add(new int[] {1, coorY});
		}
		
		return m.toArray(new int[m.size()][]);
	}

	@Override
	public char draw() {
		return this.isWhite?'\u2659':'\u265F';
	}
}

class Torre extends Piezas {
	public Torre(boolean isBlanca) {
		super(isBlanca, false);
	}
	
	@Override
	public int[][] allowedMove() {
		return new int[][] {{1, 0}, {0, 1}, {0, 1}, {0, -1}};
	}

	@Override
	public char draw() {
		return this.isWhite?'\u2656':'\u265C';
	}
}

class Caballo extends Piezas {
	public Caballo(boolean isBlanca) {
		super(isBlanca, true);
	}

	@Override
	public int[][] allowedMove() {
		return new int[][] {{2, 1}, {2, -1}, {-2, 1}, {-2, -1},
			{-1, 2}, {1, 2}, {-1, -2}, {-1, 1}};
	}

	@Override
	public char draw() {
		return this.isWhite?'\u2658':'\u265E';
	}
}
	
class Alfil extends Piezas {
	public Alfil(boolean isBlanca) {
		super(isBlanca, false);
	}
	
	@Override
	public int[][] allowedMove() {
		return new int[][] {{-1, 1}, {1, 1}, {1, -1}, {-1, -1}}; 
	}

	@Override
	public char draw() {
		return this.isWhite?'\u2657':'\u265D';
	}
}

class Reina extends Piezas {
	public Reina(boolean isBlanca) {
		super(isBlanca, false);
	}

	@Override
	public int[][] allowedMove() {
		int[][] m = new int[8][2];
		int i = 0;
		
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				m[i][0] = x;
				m[i][1] = y;
				i++;
			}
		}
		
		return m;
	}

	@Override
	public char draw() {
		return this.isWhite?'\u2655':'\u265B';
	}
}

class Rey extends Piezas {
	public Rey(boolean isBlanca) {
		super(isBlanca, true);
	}

	@Override
	public int[][] allowedMove() {
		int[][] m = new int[8][2];
		int i = 0;
		
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				m[i][0] = x;
				m[i][1] = y;
				i++;
			}
		}
		
		return m;
	}

	@Override
	public char draw() {
		return this.isWhite?'\u2654':'\u265A';
	}
}

class Move extends Piezas {
	public Move() {
		super(true, true);
	}

	@Override
	public int[][] allowedMove() {
		return null;
	}

	@Override
	public char draw() {
		return 'X';
	}
	
}