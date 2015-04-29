package com.github.sbugat.puzzle.chess.nqueens.intro;

import gnu.getopt.Getopt;

import java.util.Arrays;

import com.github.sbugat.puzzle.chess.nqueens.NQueensPuzzle;
import com.github.sbugat.puzzle.chess.nqueens.tools.BenchmarkTools;
import com.github.sbugat.puzzle.chess.nqueens.tools.SequenceTools;

/**
 * Classic N chess queens on a size N chessboard.
 * 
 * @author Sylvain Bugat
 * 
 */
public final class NQueensSolver implements NQueensPuzzle {

	/** Chessboard used only to display a solution. */
	private final boolean[][] chessboard;
	/** Array to mark unused columns. */
	private final boolean[] unusedColumns;
	/** Array to mark unused ascending diagonals diagonal number = x + y. */
	private final boolean[] unusedAscendingDiagonals;
	/** Array to mark unused descending diagonals diagonal number = x + chess board size - 1 - y. */
	private final boolean[] unusedDescendingDiagonals;

	/** Number of solution counter. */
	private long solutionCount;

	/** Size of the chess board. */
	private final int chessboardSize;
	/** Print solution flag. */
	private final boolean printSolution;

	public NQueensSolver(final int chessboardSizeArg, final boolean printSolutionArg) {

		chessboardSize = chessboardSizeArg;
		printSolution = printSolutionArg;

		chessboard = new boolean[chessboardSizeArg][chessboardSizeArg];
		unusedColumns = new boolean[chessboardSizeArg];
		Arrays.fill(unusedColumns, true);
		unusedAscendingDiagonals = new boolean[chessboardSizeArg * 2 - 1];
		Arrays.fill(unusedAscendingDiagonals, true);
		unusedDescendingDiagonals = new boolean[chessboardSizeArg * 2 - 1];
		Arrays.fill(unusedDescendingDiagonals, true);
	}

	@Override
	public long solve() {

		// Start the algorithm at the first line
		solve(0);

		// Return the number of solutions found
		return solutionCount;
	}

	@Override
	public void reset() {

		// Reinitialize the number of solutions found
		solutionCount = 0;
	}

	/**
	 * Solving recursive method, do a depth-first/back-tracking algorithm.
	 * 
	 * @param y number of the line stating at 0
	 */
	private void solve(final int y) {

		// Test all square of the line
		for (int x = 0; x < chessboardSize; x++) {

			// if the row is not already blocked by another queen
			if (unusedColumns[x]) {

				final int ascendingDiagonalNumber = x + y;
				final int descendingDiagonalNumber = x + chessboardSize - 1 - y;

				// if both diagonals are not already blocked by anothers queens
				if (unusedAscendingDiagonals[ascendingDiagonalNumber] && unusedDescendingDiagonals[descendingDiagonalNumber]) {

					chessboard[y][x] = true;
					unusedColumns[x] = false;
					unusedAscendingDiagonals[ascendingDiagonalNumber] = false;
					unusedDescendingDiagonals[descendingDiagonalNumber] = false;

					// All queens are sets on the chessboard then a solution is found!
					if (y + 1 >= chessboardSize) {
						solutionCount++;
						print();
					} else {
						// Go on to the next line
						solve(y + 1);
					}

					unusedDescendingDiagonals[descendingDiagonalNumber] = true;
					unusedAscendingDiagonals[ascendingDiagonalNumber] = true;
					unusedColumns[x] = true;
					chessboard[y][x] = false;
				}
			}
		}
	}

	/**
	 * Print the current chessboard.
	 */
	public void print() {

		if (printSolution) {
			System.out.println("\nsolution number " + solutionCount); //$NON-NLS-1$

			for (int y = 0; y < chessboardSize; y++) {

				for (int x = 0; x < chessboardSize; x++) {

					if (chessboard[y][x]) {
						System.out.print(1);
					} else {
						System.out.print(0);
					}
				}
				System.out.println();
			}
		}
	}

	/**
	 * Main program.
	 * 
	 * @param args options
	 */
	public static void main(final String args[]) {

		final Getopt getOpt = new Getopt("NQueensProblem", args, ":n:s"); //$NON-NLS-1$ //$NON-NLS-2$
		getOpt.setOpterr(false);

		// Default chessboard size
		int chessboardSize = 8;
		boolean printSolution = true;

		int c = getOpt.getopt();
		while (-1 != c) {

			switch (c) {

			case 'n':
				try {
					chessboardSize = Integer.parseInt(getOpt.getOptarg());
				} catch (final NumberFormatException e) {
					System.err.println("Usage: " + NQueensSolver.class.getSimpleName() + " [-n <size of the chessboard>] [-s]"); //$NON-NLS-1$ //$NON-NLS-2$
					System.exit(1);
				}
				break;

			case 's':
				printSolution = false;
				break;

			case '?':
			default:
				System.err.println("Usage: " + NQueensSolver.class.getSimpleName() + " [-n <size of the chessboard>] [-s]"); //$NON-NLS-1$ //$NON-NLS-2$
				System.exit(1);
			}

			c = getOpt.getopt();
		}

		final NQueensPuzzle nQueensPuzzle = new NQueensSolver(chessboardSize, printSolution);
		final long solutionCount = nQueensPuzzle.solve();

		// End of the algorithm print the total of solution(s) found
		System.out.println("\nTotal number of solution(s):" + solutionCount); //$NON-NLS-1$

		if (!SequenceTools.checkSolutionsFound(chessboardSize, solutionCount)) {

			System.err.println("Invalid number of solutions found: " + solutionCount + " expected: " + SequenceTools.getExpectedSolutions(chessboardSize) + " check the algorithm."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		BenchmarkTools.benchmark(new NQueensSolver(chessboardSize, false), 50);
	}
}
