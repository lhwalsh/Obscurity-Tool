// Walsh, Luke {010733580}
// CS 141 01
// Project #1 : Obscurity Tool

/*	This program will read in GPG, ASCII armored cyphertext filling in, 
	up to, a 64x64 array of chars. Once obtained, the program will flip the 
	data horizontally, vertically, or rotate it 90 degrees. */

import java.util.Scanner;
import java.io.*;

public class project1 {
	public static void main( String args[] ) throws FileNotFoundException, InterruptedException {
		File	 someFile    = new File(args[1]);
		int	 length	     = squareSize(someFile);
		char[][] square	     = new char[length][length];
		char[]	 commands    = args[0].toCharArray();
		int startingPosition = fillSquare(square, someFile);
		executeCommands(square, commands);
		char[][] doc = createFile(square, someFile);
		combineFiles(doc, square, startingPosition);
		display(doc);
	}
	
	// determines the largest possible size square we can create
	// cannot include = or be larger than 65
	// doesn't start counting until it passes a line of blanks
	public static int squareSize(File someFile) throws FileNotFoundException {
		Scanner	sc     = new Scanner(someFile);
		int answer     = 0;
		String temp    = sc.nextLine();
		boolean status = true;
		while (temp.isEmpty() == false) {
			temp = sc.nextLine();
		}
		while (status) {
			if (valid(sc.nextLine()) && answer < 65) {
				answer++;
			} else {
				status = false;
			}
			if (sc.hasNext() == false) {
				status = false;
			}
		}
		return answer;
	}

	//checks to make sure the line is complete and doesn't have an '=' sign
	public static boolean valid(String line) {
		boolean answer = true;
		char[] lines   = line.toCharArray();
		for (int i = 0; i < lines.length; i++) {
			if (lines[i] == 61) {
				answer = false;
			}
		}
		return answer;
	}
	
	// fills the square with values from the file
	// also determines where the file begins the encryption
	public static int fillSquare(char[][] square, File someFile) throws FileNotFoundException {
		Scanner	 sc          = new Scanner(someFile);
		int startingPosition = 0;
		String firstLine     = "";
		String temp          = sc.nextLine();
		while (temp.isEmpty() == false) {
			temp 		  = sc.nextLine();
			startingPosition += 1;
		}
		for (int x = 0; x < square.length; x++) {
			if (sc.hasNext() == true) {
				firstLine = sc.nextLine();
			}
			char[] currentLine = firstLine.toCharArray();
			for (int y = 0; y < square.length; y++) {
				square[x][y] = currentLine[y];
			}
		}
		return startingPosition + 1;
	}

	// execute the different commands like flipping vertically, horizontally, or rotating
	// executes all the commands in one call
	public static void executeCommands(char[][] square, char[] commands) throws InterruptedException {
		for (int i = 0; i < commands.length; i++) {
			commands[i] = makeLowerCase(commands[i]);
			if (commands[i] == 118) {
				flipVertically(square);
			} else if (commands[i] == 104) {
				flipHorizontally(square);
			} else if (commands[i] == 114) {
				rotate(square);
			}
		}
	}
	
	// turns all characters lower case
	public static char makeLowerCase(char command) {
		if (command < 91) {
			command = (char)(command + 32);
		}
		return command;
	}

	// flips the square horizontally
	public static void flipHorizontally(char[][] square) {
		int endX = square.length - 1;
		char tempX;
		for (int x = 0; x < square.length/2; x++) {
			for (int y = 0; y < square[x].length; y++) {
				tempX           = square[x][y];
				square[x][y]    = square[endX][y];
				square[endX][y] = tempX;
			}
			endX -= 1;
		}
	}

	// flips the square vertically
	public static void flipVertically(char[][] square) {
		char tempY;
		int endY = (square[0].length - 1);
		for (int x = 0; x < square.length/2; x++) {
			for (int y = 0; y < square[x].length; y++) {
				tempY           = square[y][x];
				square[y][x]    = square[y][endY];
				square[y][endY] = tempY;
			}
			endY -= 1;
		}
	}

	// rotates the square
	public static void rotate(char[][] square) throws InterruptedException{
		int endY     = square[0].length - 1;
		int endY1    = square[0].length - 2;
		int endX     = square.length - 1;
		int endX1    = square.length - 2;
		int x	     = 0;
		int y 	     = 0;
		int x1	     = 1;
		int y1	     = 1;
		int counter  = 0;
		int counter2 = 0;
		char temp;
		for (int i = 0; i < square.length/2; i++) {
			for (int w = 0; w < ((square[0].length-1) - counter2); w++) {
				temp			= square[x][y1];
				square[x][y1]		= square[x1][endY];
				square[x1][endY]	= square[endX][endY1];
				square[endX][endY1]	= square[endX1][y];
				square[endX1][y]	= temp;
	
				endX1 -= 1;
				endY1 -= 1;
				x1    += 1;
				y1    += 1;
				System.out.println((char)27 + "[2J");
			}
			counter2 += 2;
			counter  += 1;
			endY 	 -= 1;
			endX	 -= 1;
			y	 += 1;
			x	 += 1;
			endX1	  = (square.length - 2) - counter;
			endY1	  = (square[i].length - 2) - counter;
			x1	  = 1 + counter;
			y1	  = 1 + counter;
		}
	}

	//creates a 2d array of the entire document
	public static char[][] createFile(char[][] square, File someFile) throws FileNotFoundException {
		Scanner sc  = new Scanner(someFile);
		Scanner sc1 = new Scanner(someFile);
		int counter = 0;
		while (sc.hasNext()) {
			counter += 1;
			sc.nextLine();
		}
		char[][] doc     = new char[counter][65];
		String firstLine = "";
		for (int x = 0; x < doc.length; x++) {
			if (sc1.hasNextLine() == true) {
				firstLine = sc1.nextLine();
			}
			char[] currentLine = firstLine.toCharArray();
			for (int y = 0; y < currentLine.length; y++) {
				doc[x][y] = currentLine[y];
			}
		}
		return doc;
	}

	//applies the changes made to the document
	public static void combineFiles(char[][] doc, char[][] square, int startingPosition) {
		for (int x = 0; x < square.length; x++) {
			for (int y = 0; y < square[x].length; y++) {
				doc[startingPosition][y] = square[x][y];
			}
			startingPosition += 1;
		}
	}

	//displays the modified square to the screen
	public static void display(char[][] square) {
		for (int y = 0; y < square.length; y++) {
			for (int x = 0; x < square[y].length; x++) {
				System.out.print(square[y][x]);
			}
			System.out.println();
		}
	}
}
