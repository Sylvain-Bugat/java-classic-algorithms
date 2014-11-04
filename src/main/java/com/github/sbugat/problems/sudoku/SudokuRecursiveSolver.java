package com.github.sbugat.problems.sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SudokuRecursiveSolver {

	private final int[][] grid;
	private final int[][] lines;
	private final int[][] columns;
	private final int[][] blocks;

	boolean solved;

	private final int dimension;

	private final int sudokuSize;

	public SudokuRecursiveSolver(final int dimensionArg) throws IOException {

		dimension = dimensionArg;
		sudokuSize = dimensionArg * 3;

		final int arraySize = dimensionArg * dimensionArg + 1;

		grid = new int[arraySize][arraySize];
		lines = new int[arraySize][arraySize];
		columns = new int[arraySize][10];
		blocks = new int[arraySize][arraySize];

		read();

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
		if (grid[y][x] > 0) {
			x++;
			if (x >= sudokuSize) {
				x = 0;
				y++;

				if (y >= sudokuSize) {
					System.out.println("solved");
					solved = true;
					print();
					// System.exit( 0 );
					return;
				}
			}

			solve(x, y);
			return;
		}

		for (int nb = 1; nb <= sudokuSize; nb++) {

			if (0 == columns[x][nb] && 0 == lines[y][nb]) {

				final int blocId = x / dimension + dimension * (y / dimension);
				if (blocks[blocId][nb] == 0) {

					columns[x][nb] = 1;
					lines[y][nb] = 1;
					blocks[blocId][nb] = 1;
					grid[y][x] = nb;

					int newX = x + 1;
					int newY = y;
					if (newX >= sudokuSize) {
						newX = 0;
						newY++;

						if (newY >= sudokuSize) {
							System.out.println("solved");
							solved = true;
							print();
							// System.exit( 0 );
							return;
						}
					}

					solve(newX, newY);

					columns[x][nb] = 0;
					lines[y][nb] = 0;
					blocks[blocId][nb] = 0;
					grid[y][x] = 0;
				}
			}
		}
	}

	public void read() throws IOException {

		try (BufferedReader br = new BufferedReader(new FileReader("grid.txt"))) {

			for (int y = 0; y < 9; y++) {

				char[] ligne = br.readLine().toCharArray();

				for (int x = 0; x < 9; x++) {

					grid[y][x] = ligne[x] - '0';

					if (grid[y][x] > 0) {
						columns[x][grid[y][x]] = 1;
						lines[y][grid[y][x]] = 1;

						blocks[x / 3 + 3 * (y / 3)][grid[y][x]] = 1;
					}
				}
			}
		}
	}

	public void print() {

		for (int y = 0; y < sudokuSize; y++) {

			if (y % dimension == 0) {
				System.out.println();
			}

			for (int x = 0; x < sudokuSize; x++) {

				if (x % dimension == 0) {
					System.out.print(" ");
				}
				System.out.print(grid[y][x]);
			}
			System.out.println();
		}
	}

	public static void main(String args[]) throws IOException {

		new SudokuRecursiveSolver(3);
	}
}