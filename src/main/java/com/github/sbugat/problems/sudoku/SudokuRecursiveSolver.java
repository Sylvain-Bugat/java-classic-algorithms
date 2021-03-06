package com.github.sbugat.problems.sudoku;

import gnu.getopt.Getopt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Sudoku solver class
 *
 * @author Sylvain Bugat
 */
public class SudokuRecursiveSolver {

	/**
	 * Sudoku grid
	 */
	private final int[][] grid;

	/**
	 * Contraint to check used symbols per lines
	 */
	private final boolean[][] lines;

	/**
	 * Contraint to check used symbols per columns
	 */
	private final boolean[][] columns;

	/**
	 * Contraint to check used symbols per blocks
	 */
	private final boolean[][] blocks;

	/**
	 * Number of solutions found
	 */
	private int solutionCount;

	/**
	 * Size of a sub-square of the sudoku grid (on a classic sudoku grid it's 3)
	 */
	private final int dimension;

	/**
	 * Total size of the sudoku grid (on a classic sudoku grid it's 9) and number of possible different symbol (on a classic sudoku grid it's 9: 1 2 3 4 5 6 7 8 9)
	 */
	private final int sudokuSize;

	/**
	 * Delta for read and print grid symbols
	 */
	private final int symbolDelta;

	public SudokuRecursiveSolver(final int dimensionArg, final int symbolDeltaArg) throws IOException {

		dimension = dimensionArg;
		sudokuSize = dimensionArg * dimensionArg;
		symbolDelta = symbolDeltaArg;

		final int arraySize = dimensionArg * dimensionArg;

		grid = new int[arraySize][arraySize];
		lines = new boolean[arraySize][arraySize];
		columns = new boolean[arraySize][arraySize];
		blocks = new boolean[arraySize][arraySize];
		for (int i = 0; i < arraySize; i++) {
			Arrays.fill(lines[i], true);
			Arrays.fill(columns[i], true);
			Arrays.fill(blocks[i], true);
		}

		read();
		print();

		solve(0, 0);
	}

	/**
	 * Solve Sudoku with depth-first/back-tracking algorithm
	 *
	 * @param x
	 * @param y
	 */
	public void solve(int x, int y) {

		// If a symbol is already in the grid at this position
		if (grid[y][x] >= 0) {
			x++;
			if (x >= sudokuSize) {
				x = 0;
				y++;

				if (y >= sudokuSize) {
					solutionCount++;
					System.out.println("solution number " + solutionCount + ":");
					print();
					return;
				}
			}

			solve(x, y);
			return;
		}

		for (int nb = 0; nb < sudokuSize; nb++) {

			if (columns[x][nb] && lines[y][nb]) {

				final int blocId = x / dimension + dimension * (y / dimension);
				if (blocks[blocId][nb]) {

					columns[x][nb] = false;
					lines[y][nb] = false;
					blocks[blocId][nb] = false;
					grid[y][x] = nb;

					int newX = x + 1;
					int newY = y;
					if (newX >= sudokuSize) {
						newX = 0;
						newY++;

						if (newY >= sudokuSize) {
							solutionCount++;
							System.out.println("solution number " + solutionCount + ":");
							print();
						}
						else {
							solve(newX, newY);
						}
					}
					else {
						solve(newX, newY);
					}

					columns[x][nb] = true;
					lines[y][nb] = true;
					blocks[blocId][nb] = true;
					grid[y][x] = -1;
				}
			}
		}
	}

	public void read() throws IOException {

		try (BufferedReader br = new BufferedReader(new FileReader("grid.txt"))) {

			int y = 0;

			while (y < sudokuSize) {

				final String line = br.readLine();
				if (null == line) {
					System.err.println("Not enough line");
					System.exit(1);
				}

				if (!line.isEmpty()) {

					final String[] symbols = line.split(" +");
					if (symbols.length < sudokuSize) {
						System.err.println("incorrect line (not enough numbers):" + line);
						System.exit(1);
					}

					for (int x = 0; x < sudokuSize; x++) {

						try {
							grid[y][x] = Integer.parseInt(symbols[x]) - symbolDelta;
						}
						catch (final NumberFormatException e) {
							grid[y][x] = -1;
						}

						if (grid[y][x] >= 0) {
							columns[x][grid[y][x]] = false;
							lines[y][grid[y][x]] = false;

							blocks[x / dimension + dimension * (y / dimension)][grid[y][x]] = false;
						}
					}

					y++;
				}
			}
		}
	}

	/**
	 * Print the current grid to display a solution
	 */
	public void print() {

		final int numberDigits = String.valueOf(sudokuSize + symbolDelta).length();

		for (int y = 0; y < sudokuSize; y++) {

			if (y % dimension == 0 && y > 0) {
				System.out.println();
				System.out.println();
			}

			for (int x = 0; x < sudokuSize; x++) {

				if (x % dimension == 0 && x > 0) {
					System.out.print(String.format("%" + numberDigits + "s ", ""));
				}

				if (-1 == grid[y][x]) {
					System.out.print(String.format("%" + numberDigits + "s ", "*"));
				}
				else {
					System.out.print(String.format("%" + numberDigits + "d ", grid[y][x] + symbolDelta));
				}
			}
			System.out.println();
		}
	}

	public static void main(String args[]) throws IOException {

		final String usage = "Usage: " + SudokuRecursiveSolver.class.getSimpleName() + " [-n <dimension of the sudoku>] [-f <input sudoku file>] [-s <symbol shifting>]";

		final Getopt getOpt = new Getopt( SudokuRecursiveSolver.class.getSimpleName(), args, ":n:f:s:" );
		getOpt.setOpterr( false );

		//Default sudoku size
		int sudokuSize = 3;

		//Default symbol shifting
		int symbolShifting = 1;

		int c = getOpt.getopt();
		while( -1 != c )
		{
			switch(c)
			{
			case 'n':
				try {
					sudokuSize = Integer.parseInt( getOpt.getOptarg() );

					if( sudokuSize < 3 ) {
						System.err.println( usage );
						System.exit( 1 );
					}
				}
				catch( final NumberFormatException e ) {
					System.err.println( usage );
					System.exit( 1 );
				}
				break;

			case 's':
				try {
					symbolShifting = Integer.parseInt( getOpt.getOptarg() );
				}
				catch( final NumberFormatException e ) {
					System.err.println( usage );
					System.exit( 1 );
				}
				break;

			case '?':
			default:
				System.err.println( usage );
				System.exit( 1 );
			}

			c = getOpt.getopt();
		}
		final SudokuRecursiveSolver solver = new SudokuRecursiveSolver(sudokuSize, symbolShifting);

		if (0 == solver.solutionCount) {

			System.out.println("No solution found");
			System.exit(1);
		}
		else if (solver.solutionCount > 1) {

			System.out.println("Multiple solutions found, this is not a true Soduku grid!");
			System.exit(1);
		}
	}
}