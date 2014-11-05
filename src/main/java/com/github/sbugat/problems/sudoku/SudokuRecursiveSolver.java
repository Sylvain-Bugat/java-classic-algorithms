package com.github.sbugat.problems.sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class SudokuRecursiveSolver {

	private final int[][] grid;
	private final boolean[][] lines;
	private final boolean[][] columns;
	private final boolean[][] blocks;

	private int solutionCount;

	private final int dimension;

	private final int sudokuSize;

	public SudokuRecursiveSolver(final int dimensionArg) throws IOException {

		dimension = dimensionArg;
		sudokuSize = dimensionArg * 3;

		final int arraySize = dimensionArg * dimensionArg + 1;

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
					solutionCount++;
					System.out.println("solution number " + solutionCount + ":");
					print();
					return;
				}
			}

			solve(x, y);
			return;
		}

		for (int nb = 1; nb <= sudokuSize; nb++) {

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
						columns[x][grid[y][x]] = false;
						lines[y][grid[y][x]] = false;

						blocks[x / 3 + 3 * (y / 3)][grid[y][x]] = false;
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

		SudokuRecursiveSolver solver = new SudokuRecursiveSolver(3);

		if (0 == solver.solutionCount) {

			System.out.println("No solution found");
			System.exit(1);
		}
		else if ( solver.solutionCount > 1 ) {

			System.out.println("Multiple solutions found, this is not a true Soduku grid!");
			System.exit(1);
		}
	}
}