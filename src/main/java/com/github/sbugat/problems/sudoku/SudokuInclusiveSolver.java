package com.github.sbugat.problems.sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Sudoku solver class
 *
 * @author Sylvain Bugat
 */
public class SudokuInclusiveSolver {

	/**
	 * Sudoku grid
	 */
	private final int[][] grid;

	/**
	 * Contraint to check used symbols per position
	 */
	private final boolean[][][] possibleSymbols;

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

	private int placedSymbols;

	private SudokuInclusiveSolver(final int dimensionArg, final int symbolDeltaArg) throws IOException {

		dimension = dimensionArg;
		sudokuSize = dimensionArg * dimensionArg;
		symbolDelta = symbolDeltaArg;

		grid = new int[sudokuSize][sudokuSize];
		possibleSymbols = new boolean[sudokuSize][sudokuSize][sudokuSize];
		for (int i = 0; i < sudokuSize; i++) {
			for (int j = 0; j < sudokuSize; j++) {
				Arrays.fill(possibleSymbols[i][j], true);
			}
		}

		read();
		print();

		solve();
	}

	/**
	 * Solve Sudoku with inclusive algorithm
	 */
	private void solve() {

		int loopSymbolPlaced;
		do {

			loopSymbolPlaced = 0;

			for (int y = 0; y < sudokuSize; y++) {

				for (int x = 0; x < sudokuSize; x++) {

					if (grid[y][x] < 0) {
						int targetSymbol = -1;
						int possibleSymbolCount = 0;
						for (int nb = 0; nb < sudokuSize; nb++) {

							if (possibleSymbols[y][x][nb]) {
								targetSymbol = nb;
								possibleSymbolCount++;
								if (possibleSymbolCount > 1) {
									break;
								}
							}
						}

						if (1 == possibleSymbolCount) {
							loopSymbolPlaced++;
							placeSymbol(x, y, targetSymbol);
						}
					}
				}
			}
		}
		while (placedSymbols < sudokuSize * sudokuSize && loopSymbolPlaced > 0);

		if (placedSymbols >= sudokuSize * sudokuSize) {
			solutionCount++;
			System.out.println("solution number " + solutionCount + ":");
			print();
		}
		else {
			System.out.println("current grid:");
			print();
		}
	}

	private void placeSymbol(final int x, final int y, final int symbol) {

		placedSymbols++;

		grid[y][x] = symbol;

		final int blockId = x / dimension + dimension * (y / dimension);

		for (int i = 0; i < sudokuSize; i++) {

			possibleSymbols[y][i][symbol] = false;
			possibleSymbols[i][x][symbol] = false;
		}

		for (int j = 0; j < sudokuSize; j++) {

			final int dimY = dimension * (j / dimension);

			for (int i = 0; i < sudokuSize; i++) {

				int bId = dimY + i / dimension;

				if (blockId == bId) {

					possibleSymbols[j][i][symbol] = false;
				}
			}
		}
	}

	private void read() throws IOException {

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

						int symbol;
						try {
							symbol = Integer.parseInt(symbols[x]) - symbolDelta;
						}
						catch (final NumberFormatException e) {
							symbol = -1;
						}

						if (symbol >= 0) {

							placeSymbol(x, y, symbol);
						}
						else {
							grid[y][x] = symbol;
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
	private void print() {

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

		System.out.println();
	}

	public static void main(String args[]) throws IOException {

		final SudokuInclusiveSolver solver = new SudokuInclusiveSolver(3, 1);

		if (0 == solver.solutionCount) {

			System.out.println("No solution found with this algorithm");
			System.exit(1);
		}
	}
}