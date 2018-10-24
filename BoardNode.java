package Øving2;

import java.util.ArrayList;

public class BoardNode {

	public int id, y, x, f, g, h, cost; 
	public char type; 
	public ArrayList<BoardNode> neighbour;
	public BoardNode parent;
	//board-size (x,y)
	int xSize;
	int ySize;

	//konstruktør for del 1, altså uten vekting av nodene
	public BoardNode(int id, int y, int x, char type, int xSize, int ySize) { 
		this.id = id; 
		this.y = y; this.x = x; 
		this.type = type;
		this.neighbour = new ArrayList<BoardNode>();
		this.f = 999; this.g = 999; this.h = 999; //999 for å komme utenfor "RANGE"
		this.xSize = xSize;
		this.ySize = ySize;
		this.cost = 1;
	}

	//konstruktør for del 2, vekting av noder
	public BoardNode(int id, int y, int x, char type, int xSize, int ySize, int cost) { 
		this.id = id; 
		this.y = y; this.x = x; 
		this.type = type;
		this.neighbour = new ArrayList<BoardNode>();
		this.f = 999; this.g = 999; this.h = 999; //999 for å komme utenfor "RANGE"
		this.xSize = xSize;
		this.ySize = ySize;
		this.cost = cost;
	}

	//regner ut den nåværende parent + this sin f-verdi, 
	//dette blir brukt for å sjekke om det finnes en ny/bedre parent
	public int eval(BoardNode c, BoardNode end) {
		int g = c.g + this.cost;
		//manhatten 
		int h = (Math.abs(this.getX() - end.getX()) + Math.abs(this.getY() - end.getY()));
		int f = g + h;
		return f;
	}

	//fester c/current-noden til den noden i nabolista som parent (holder styr på "stien")
	public void attach(BoardNode c) {
		this.parent = c;
	}

	//legger til naboer rundt noden i en liste (for gitt str.)
	public void addNeighbour(BoardNode[][] grid) {

		if (this.x-1 >= 0 && this.x-1 < this.xSize) {
			BoardNode a = grid[this.y][this.x-1];
			//			a.parent = this;
			this.neighbour.add(a);
		}
		if (this.y+1 < this.ySize && this.y+1 >= 0) {
			BoardNode b = grid[this.y+1][this.x];
			//			b.parent = this;
			this.neighbour.add(b);
		}
		if (this.x+1 < this.xSize && this.x+1 >= 0) {
			BoardNode c = grid[this.y][this.x+1];
			//			c.parent = this;
			this.neighbour.add(c);
		}
		if (this.y-1 >= 0 && this.y-1 < this.ySize) {
			BoardNode d = grid[this.y-1][this.x];
			//			d.parent = this;
			this.neighbour.add(d);
		}
	}


	public void setF(int f) {
		this.f = f;
	}
	public void setG(int g) {
		this.g = g;
	}
	public void setH(int h) {
		this.h = h;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public char getType() {
		return type;
	}
	public void setType(char status) {
		this.type = status;
	}
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}

}
