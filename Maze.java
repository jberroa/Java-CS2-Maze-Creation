//Jorge Berroa 
//COP3503
//Maze Assignment 
//3354457




import java.util.*;


// Credits to Sean Szumlanski for providing some pseudo code for disjoint set 

public class Maze {
	// creating my static width and hight variables labeled w for width  and h for height 
	//also creating static  walls , parent , and rank  array. the walls array will hold my wall number in my maze 2d array 
	// and my parent and rank array is for my disjoint set implimentations
	public static int w, h;
	public static int walls[];
	public static int parent[];
	public static int rank[];

	public static char[][] create(int width, int height) {
		//here i calculate how many walls there will be in my maze 
		// with total walls and sections i can calculate how many many walls there is in my maze and create my walls array 
		int totalwalls = ((width - 1) * 2) + 1;
		int sections = (((height * 2) - 1) / 2);
		walls = new int[totalwalls * sections + (width - 1)];
		int m = 0;
		for (int x : walls) { // here i initialize my array to their correct values
			walls[m] = m;
			++m;

		}
		w = width;
		h = height;
		// adjusting the char array to the new 2d char array by adjusting width
		// and height to match pdf specs.
		width = (width * 2) + 1;
		height = (height * 2) + 1;

		char nmaze[][] = new char[height][width];
		// double for loop to create maze
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// if i is even put a wall
				if (i % 2 == 0) {

					nmaze[i][j] = '#';
				}
				// if i is odd and j is not even put a space else put a wall
				else if (j % 2 != 0)
					nmaze[i][j] = ' ';
				// adding wall to maze and arraylist
				else {
					nmaze[i][j] = '#';
				}
			}
		}
		//make maze  remove walls that by checking if their in disjoint sets 
		makemaze(walls, nmaze);
		return nmaze;
	}

	public static int i_coordinate(int cell, int width) {
		// finds the i coordinate for the cell wall
		return ((cell / width) * 2) + 1;
	}

	public static int j_coordinate(int cell, int width) {
		// finds the j coordinate for the cell 
		return ((cell % width) * 2) + 1;
	}
	
	// prints the maze by double for loop 

	public static void printMaze(char[][] maze) {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++)
				System.out.print(maze[i][j]);
			System.out.println();
		}

		System.out.println();
	}
	
	// this method checks if the cell this wall separates  are in disjoint sets 

	public static boolean check_disjoint(int wall_num) {
		int width = w - 1;
		int totalwalls = (width * 2) + 1;
		int cell1, cell2;
		
		// formula for finding the cells this wall separates 
		if (wall_num - width < 0)
			cell1 = wall_num;
		else
			cell1 = wall_num - (width - (((wall_num - width) / totalwalls) * (-1 * width)));
		cell2 = wall_num + (1 + ((wall_num / totalwalls) * (-1 * width)));
		
		// if the cells belong to the same set return false else true 
		if (findset(cell1) == findset(cell2))
			return false;
		else
			return true;

	}

	public static void erasemaze(int wall_num, char[][] maze) {
		int width = w - 1;
		int totalwalls = (width * 2) + 1;
		int cell1, cell2;

		// finds the cell number of the wall it divides 
		if (wall_num - width < 0)
			cell1 = wall_num;
		else
			cell1 = wall_num
					- (width - (((wall_num - width) / totalwalls) * (-1 * width)));
		cell2 = wall_num + (1 + ((wall_num / totalwalls) * (-1 * width)));

		// the method takes in the cell number and erases the wall between them  
		erase_wall(cell1, cell2, maze);
		// uninon this cells to the same set 
		union(cell1, cell2);

	}

	public static void makemaze(int walls[], char[][] maze) {
		// make disjoint set of cells in maze 
		makeset(w * h);
		// make an array list with the same values as the array so i can utilize Collections shuffle method and i can remove
		// each wall exactly once by traversing through the array once it is shuffled 
		shuffleArray(walls);
		int i = 0;
		int k;
		// while i have not checked every wall check if cells are in disjoint set if they are not erase wall and union the cells 
		while (i != walls.length) {
			k = walls[i];
			if (check_disjoint(k)) 
				erasemaze(k, maze);
			
			++i;

		}
		// add marks to the maze the s and e character are added to the top left corner and lower right corner
		add_marks(maze);
	}
	public static void add_marks(char [][]maze)
	{
		//adds s and e to the maze 
		maze[i_coordinate(0, w)][j_coordinate(0, w)]='s';
		maze[i_coordinate((w*h)-1, w)][j_coordinate((w*h)-1, w)]='e';
	}
	// initializes parent and rank array to correct values 
	public static void makeset(int n) {
		parent = new int[n];
		rank = new int[n];
		for (int i = 0; i < n; i++) {
			// in this representation, the root is its own parent
			parent[i] = i;

			// the height of a tree with a single node is 0
			rank[i] = 0;
		}
	}

	// recursive emethid to find the parent of the cell
	public static int findset(int x) {
		if (parent[x] == x)
			return x;

		parent[x] = findset(parent[x]);
		return parent[x];
	}

	// this unions the cells by finding the parents and cheks the rank of their parent and whether setx should be the parent of sety 
	// or vice-versa if they have the same rank then increase the rank of that parent
	public static void union(int x, int y) {
		int setx = findset(x);
		int sety = findset(y);

		if (rank[setx] < rank[sety]) {
			parent[setx] = sety;

		} else if (rank[sety] < rank[setx]) {

			parent[sety] = setx;

		} else {

			parent[sety] = setx;
			rank[setx]++;
		}
	}

	// finds i and j coordinates to cells and erase the wall that separates it 
	public static void erase_wall(int x, int y, char[][] maze) {
		int i = i_coordinate(x, w);
		int j = j_coordinate(x, w);
		int k = i_coordinate(y, w);
		int l = j_coordinate(y, w);

		if (i == k)
			maze[i][++j] = ' ';
		else if (j == l)
			maze[++i][j] = ' ';

	}
	// shuffle the array to have random values and just traverse through the array getting random values
	 public static void shuffleArray(int[] ar)
	  {
	    Random rnd = new Random();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }
	public static double difficultyRating(){
		return 2.88;
	}
	public static double hoursSpent(){
		return 20.3;
	}



}