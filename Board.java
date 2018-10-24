package Øving2;


import java.io.File; 
import java.util.ArrayList; 
import java.util.Scanner;


public class Board {

	//OPPRETTELSE AV BRETT

	//lager grid
	public static BoardNode[][] grid;

	public BoardNode start;
	public BoardNode end;
	int ySize;
	int xSize;

	//henter verdier fra .txt filer og oppretter grid ved bruk av scanner
	public void initBoard(String boardname, int xSize, int ySize) throws Exception{
		
		grid = new BoardNode[ySize][xSize]; 
		this.xSize = xSize;
		this.ySize = ySize;
		File board = new File("C:\\Users\\Øyvind\\Desktop\\boards\\"+boardname+".txt"); 
		Scanner sc = new Scanner(board);
		for (int y=0; y<ySize; y++) {
			String str = sc.nextLine(); 
			for (int x=0; x<xSize; x++) {
				//setter noden til det som er i tekststrengen (start/slutt/type)
				grid[y][x] = new BoardNode((y*xSize+x), y, x, str.charAt(x), this.xSize, this.ySize);
				if (grid[y][x].type == 'A') { 
					this.start = new BoardNode((y*xSize+x), y, x, str.charAt(x), this.xSize, this.ySize);
				}
				if (grid[y][x].type == 'B') {
					this.end = new BoardNode((y*xSize+x), y, x, str.charAt(x), this.xSize, this.ySize);
				}
				if (grid[y][x].type== 'w') {
					grid[y][x] = new BoardNode((y*xSize+x), y, x, str.charAt(x), this.xSize, this.ySize, 100);
				}
				if (grid[y][x].type== 'm') {
					grid[y][x] = new BoardNode((y*xSize+x), y, x, str.charAt(x), this.xSize, this.ySize, 50);
				}
				if (grid[y][x].type== 'f') {
					grid[y][x] = new BoardNode((y*xSize+x), y, x, str.charAt(x), this.xSize, this.ySize, 10);
				}
				if (grid[y][x].type== 'g') {
					grid[y][x] = new BoardNode((y*xSize+x), y, x, str.charAt(x), this.xSize, this.ySize, 5);
				}
				if (grid[y][x].type== 'r') {
					grid[y][x] = new BoardNode((y*xSize+x), y, x, str.charAt(x), this.xSize, this.ySize, 1);
				}
			}
		}

		sc.close();	
	}

	//Kalkulering

	//Bruker Manhattan-distance til å regne ut f, g, h
	public static void calculate(BoardNode c, BoardNode start, BoardNode end) {

		// g - avstand gått
		if(c.parent != null) {
			c.g = c.parent.g + c.cost;
		} else {
			c.g = (int) (Math.abs(c.getX() - start.getX()) + Math.abs(c.getY() - start.getY()));
		}

		//h - estimert avstand til mål
		c.h = (int) (Math.abs(c.getX() - end.getX()) + Math.abs(c.getY() - end.getY()));

		//f = summen av g og h, den verdien man avgjør hvilken som er den neste (beste)
		//som skal besøkes i OPEN
		c.f = c.g + c.h;
	}
	
	//"re-tracer" tilbake stien og markerer den optimale ruten
	public void calculateWinnerRoute(BoardNode c) {
		if (c.h == 0) {
			c.type = 'X';
			start.type = 'X';

			System.out.println("WINNER");

			//går tilbake gjennom alle foreldre til vinnernoden, og setter de til 'O'
			while(c.parent != null) {
				c.parent.type = 'O';
				c = c.parent;
			}
		}
	}

	//AGENDA LOOP

	ArrayList<BoardNode> open = new ArrayList<BoardNode>(); 
	ArrayList<BoardNode> closed = new ArrayList<BoardNode>();
	
	int loopCount = 0;
	
	public void AstarAlgorithm() {
		System.out.println("starter A*");

		//legger startnoden til i open-lista
		open.add(start);
		
		//setter current => c og regner ut f,g(kostnad for å komme hit),h(estimert kostnad til mål)
		//mens open ikke er tom, så vil løkken fortsette. 
		while (!open.isEmpty()) {
			loopCount++;
			System.out.println(" - - Ny iterasjon i while "+loopCount+"");
			BoardNode c = open.get(0);
			calculate(c,start,end);
			//går gjennom lista med noder i open. 
			for(BoardNode node: open) {
				
				//hvis c.h = 0, hopper ut av for-loop, og finner "vinnerveien"
				if(c.h == 0) {
					break;
				}
				
				calculate(node, start, end);
				//hvis en på open-lista er mindre, blir denne gjort om til current. Den gamle ryddes opp
				if(c.f > node.f) {
					//					node.parent = c;
					c = node;
					System.out.println("node på OPEN var mindre enn c, c oppdatert");
					System.out.println("CURRENT:"+c.x+","+c.y+"");
				}
			}
			
			//flytter c fra open, regner ut f,g,h
			open.remove(c);
			calculate(c,start,end);

			//genererer en liste med naboer/successors
			c.addNeighbour(grid);
			closed.add(c);
			
			//hvis vinner, regn ut ruten
			if(c.h == 0) {
				calculateWinnerRoute(c);
			}

			// går gjennom listen med naboer
			for(BoardNode node : c.neighbour) {
				System.out.println("går gjennom liste med naboer");
				if (node.type == '#' || closed.contains(node)) {
					System.out.println("har # / er i closed");
					System.out.println(+node.x+","+node.y+"");
					continue;
				}

				//hvis node ikke er i en liste, legges til i OPEN og c blir dens parent.
				else if(!open.contains(node)) {
					System.out.println("la til node i OPEN");
					open.add(node);
					node.attach(c);

				} 

				//regner ut f med denne c, og sammenligner med forrige f,
				//hvis c er en bedre parent (ift f),
				//så oppdateres node sin parent seg og node legges til i OPEN.
				else if (node.eval(c, end) < node.f && closed.contains(node)) {
					System.out.println("evaluerer om nye rute er bedre");
					open.add(node);
					closed.remove(node);
					node.attach(c);
				}
			}
		}	
	}


	//OUTPUT

	//printer brettet
	public void printBoard(int xSize, int ySize) {

		for (int l = 0; l < ySize; l++) {
			for (int m = 0; m < xSize; m++) {
				System.out.print(grid[l][m].type + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws Exception {
		Board test = new Board();
		test.initBoard("board-1-1", 20, 7);
		test.printBoard(20, 7);
		test.AstarAlgorithm();
		test.printBoard(20, 7);

	}
}


