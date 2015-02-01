package com.github.sbugat.problems.magichexagon;

import gnu.getopt.Getopt;

import java.io.IOException;

/**
 * Magic hexagon basic solver.
 * 
 * @author Sylvain Bugat
 * 
 */
public class MagicHexagon {

	/** Hexagon used only to display a solution. */
	private final int[][] hexagon;
	/** Array to sum line numbers = y. */
	private final int[] sumLines;
	/** Array to sum ascending diagonals diagonal numbers = x + y/2. */
	private final int[] sumAscendingDiagonals;
	/** Array to mark unused ascending diagonals diagonal numbers = x - (y + 1)/2 + hexagonSize/2. */
	private final int[] sumDescendingDiagonals;

	/** Array to sum line numbers = y. */
	private final int[] remainingLineSymbols;
	/** Array to sum ascending diagonals diagonal numbers = x + y/2. */
	private final int[] remainingAscendingDiagonalSymbols;
	/** Array to mark unused ascending diagonals diagonal numbers = x - (y + 1)/2 + hexagonSize/2. */
	private final int[] remainingDescendingDiagonalSymbols;

	private final boolean[] usedSymbols;

	/** Number of solution counter. */
	private long solutionCount;

	/** Size of the hexagon. */
	private final int hexagonSize;

	private final int numberOfSymbols;
	private final int symbolDelta;
	/** Print solution flag. */
	private final boolean printSolution;

	int a = 0;

	public MagicHexagon(final int hexagonSizeArg, final int symbolDeltaArg, final boolean printSolutionArg) throws IOException {

		// Arguments copies
		hexagonSize = hexagonSizeArg;
		symbolDelta = symbolDeltaArg;
		printSolution = printSolutionArg;

		hexagon = new int[hexagonSizeArg * 2 - 1][hexagonSizeArg * 2 - 1];
		sumLines = new int[hexagonSizeArg * 2 - 1];
		sumAscendingDiagonals = new int[hexagonSizeArg * 2];
		sumDescendingDiagonals = new int[hexagonSizeArg * 2];
		remainingLineSymbols = new int[hexagonSizeArg * 2 - 1];
		remainingAscendingDiagonalSymbols = new int[hexagonSizeArg * 2];
		remainingDescendingDiagonalSymbols = new int[hexagonSizeArg * 2];

		int deltaY = 0;
		int calcultedNumberOfSymbols = (hexagonSizeArg * 2 - 1) * (hexagonSizeArg * 2 - 1);
		for (int y = hexagonSizeArg - 1; y >= 0; y--) {

			for (int x = 0; x < hexagon[y].length; x++) {

				if (x < (deltaY + 1) / 2) {
					hexagon[y][x] = Integer.MIN_VALUE;
					calcultedNumberOfSymbols--;
				}
				else if (x >= hexagon[y].length - deltaY / 2) {
					hexagon[y][x] = Integer.MIN_VALUE;
					calcultedNumberOfSymbols--;
				}
			}

			deltaY++;
		}
		deltaY = 0;
		for (int y = hexagonSizeArg - 1; y < hexagon.length; y++) {

			for (int x = 0; x < hexagon[y].length; x++) {

				if (x < (deltaY + 1) / 2) {
					hexagon[y][x] = Integer.MIN_VALUE;
					calcultedNumberOfSymbols--;
				}
				else if (x >= hexagon[y].length - deltaY / 2) {
					hexagon[y][x] = Integer.MIN_VALUE;
					calcultedNumberOfSymbols--;
				}
			}

			deltaY++;
		}
		numberOfSymbols = calcultedNumberOfSymbols;

		for (int y = 0; y < hexagon.length; y++) {

			for (int x = 0; x < hexagon[y].length; x++) {

				if (hexagon[y][x] != Integer.MIN_VALUE) {

					final int ascendingDiagonal = x + y / 2;
					final int descendingDiagonal = x - (y + 1) / 2 + hexagonSize / 2;

					remainingLineSymbols[y]++;
					remainingAscendingDiagonalSymbols[ascendingDiagonal]++;
					remainingDescendingDiagonalSymbols[descendingDiagonal]++;
				}
			}
		}

		usedSymbols = new boolean[numberOfSymbols + symbolDeltaArg];

		// Start the algorithm at the first line
		solve(0, 0);

		// End of the algorithm print the total of solution(s) found
		System.out.println("\nTotal number of solution(s):" + solutionCount);
	}

	/**
	 * Solving recursive method, do a depth-first/back-tracking algorithm
	 * 
	 * @param y number of the line stating at 0
	 */
	private void solve(final int x, final int y) {

		if (hexagon[y][x] != Integer.MIN_VALUE) {
			// Test all usable symbols
			for (int s = symbolDelta; s < usedSymbols.length; s++) {

				if (!usedSymbols[s]) {

					if (remainingLineSymbols[y] == 1 && sumLines[y] + s == 38 || remainingLineSymbols[y] > 1 && sumLines[y] + s <= 38) {

						final int ascendingDiagonal = x + y / 2;
						final int descendingDiagonal = x - (y + 1) / 2 + hexagonSize / 2;

						if (remainingAscendingDiagonalSymbols[ascendingDiagonal] == 1 && sumAscendingDiagonals[ascendingDiagonal] + s == 38 || remainingAscendingDiagonalSymbols[ascendingDiagonal] > 1 && sumAscendingDiagonals[ascendingDiagonal] + s <= 38) {

							if (remainingDescendingDiagonalSymbols[descendingDiagonal] == 1 && sumDescendingDiagonals[descendingDiagonal] + s == 38 || remainingDescendingDiagonalSymbols[descendingDiagonal] > 1 && sumDescendingDiagonals[descendingDiagonal] + s <= 38) {

								sumAscendingDiagonals[ascendingDiagonal] += s;
								sumDescendingDiagonals[descendingDiagonal] += s;
								sumLines[y] += s;
								usedSymbols[s] = true;
								remainingLineSymbols[y]--;
								remainingAscendingDiagonalSymbols[ascendingDiagonal]--;
								remainingDescendingDiagonalSymbols[descendingDiagonal]--;
								hexagon[y][x] = s;

								a++;
								/*
								 * if (a % 1000000 == 0) { print(); }
								 */
								// System.out.println(s + " on " + x + "/" + y);

								if (x + 1 >= hexagon.length) {

									if (y + 1 >= hexagon.length) {

									}
									else {

										solve(0, y + 1);
									}
								}
								else {
									solve(x + 1, y);
								}

								hexagon[y][x] = 0;
								remainingAscendingDiagonalSymbols[ascendingDiagonal]++;
								remainingDescendingDiagonalSymbols[descendingDiagonal]++;
								remainingLineSymbols[y]++;
								usedSymbols[s] = false;
								sumAscendingDiagonals[ascendingDiagonal] -= s;
								sumDescendingDiagonals[descendingDiagonal] -= s;
								sumLines[y] -= s;
							}
						}
					}
				}
			}
		}
		else {
			if (x + 1 >= hexagon.length) {

				if (y + 1 >= hexagon.length) {

					System.out.println("ending");
					solutionCount++;
					print();
					return;
				}
				else {
					solve(0, y + 1);
				}
			}
			else {
				solve(x + 1, y);
			}
		}
	}

	/**
	 * Print the current haxagon
	 */
	public void print() {

		if (printSolution) {
			System.out.println("\nsolution number " + solutionCount);

			for (int y = 0; y < hexagon.length; y++) {

				final boolean odd = y % 2 == 0;
				boolean start = true;
				for (int x = 0; x < hexagon[y].length; x++) {

					if (hexagon[y][x] != Integer.MIN_VALUE) {
						System.out.print(String.format("%02d  ", hexagon[y][x]));
						start = false;
					}
					else if (start && !odd) {
						System.out.print("  ");
					}
					else {
						System.out.print("    ");
					}
				}
				System.out.println();
			}
		}
		else {

			if (0 == solutionCount % 100) {
				System.out.println(".");
			}
			else {
				System.out.print(".");
			}
		}
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String args[]) throws IOException {

		final Getopt getOpt = new Getopt("MagicHexagon", args, ":n:s");
		getOpt.setOpterr(false);

		// Default hexagon border size
		int hexagonSize = 3;
		boolean printSolution = true;

		int c = getOpt.getopt();
		while (-1 != c) {
			switch (c) {
			case 'n':
				try {
					hexagonSize = Integer.parseInt(getOpt.getOptarg());
				}
				catch (final NumberFormatException e) {
					System.err.println("Usage: " + MagicHexagon.class.getSimpleName() + " [-n <size of the chessboard>] [-s]");
					System.exit(1);
				}
				break;

			case 's':
				printSolution = false;
				break;

			case '?':
			default:
				System.err.println("Usage: " + MagicHexagon.class.getSimpleName() + " [-n <size of the chessboard>] [-s]");
				System.exit(1);
			}

			c = getOpt.getopt();
		}

		new MagicHexagon(hexagonSize, 1, printSolution);
	}
}
