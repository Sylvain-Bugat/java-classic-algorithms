package com.github.sbugat.puzzle.chess.nqueens.intro;

import gnu.getopt.Getopt;

import com.github.sbugat.puzzle.chess.nqueens.GenericNQueensSolver;
import com.github.sbugat.puzzle.chess.nqueens.tools.BenchmarkTools;
import com.github.sbugat.puzzle.chess.nqueens.tools.InvalidSolutionsException;
import com.github.sbugat.puzzle.chess.nqueens.tools.SequenceTools;

/**
 * Classic N chess queens on a size N chessboard.
 * 
 * @author Sylvain Bugat
 * 
 */
public final class BasicNQueensSolver extends GenericNQueensSolver {

	/** Chessboard used only to display a solution. */
	private final boolean[][] chessboard;

	public BasicNQueensSolver(final int chessboardSizeArg, final boolean printSolutionArg) {

		super(chessboardSizeArg, printSolutionArg);

		chessboard = new boolean[chessboardSizeArg][chessboardSizeArg];
	}

	@Override
	public long solve() {

		// Start the algorithm at the first line
		solve(0);

		// Return the number of solutions found
		return solutionCount;
	}

	/**
	 * Solving recursive method, do a depth-first/back-tracking algorithm.
	 * 
	 * @param y number of the line stating at 0
	 */
	private void solve(final int y) {

		// Test all square of the line
		for (int x = 0; x < chessboardSize; x++) {

			// if the target position is valid (not already blocked by another queen)
			if (checkValidChessboardPosition(x, y)) {

				chessboard[y][x] = true;

				// All queens are sets on the chessboard then a solution is found!
				if (y + 1 >= chessboardSize) {
					solutionCount++;
					print();
				} else {
					// Go on to the next line
					solve(y + 1);
				}

				chessboard[y][x] = false;
			}
		}
	}

	/**
	 * Check if a chessboard position can be used to place a new queen.
	 * 
	 * @param targetX target X position
	 * @param targetY target Y position
	 * @return true if the position is valid, false otherwise
	 */
	private boolean checkValidChessboardPosition(final int targetX, final int targetY) {

		if (chessboard[targetY][targetX]) {
			return false;
		}

		for (int i = 1; i < chessboardSize; i++) {

			if (targetX + i < chessboardSize) {

				if (chessboard[targetY][targetX + i]) {
					return false;
				}

				if (targetY + i < chessboardSize && chessboard[targetY + i][targetX + i]) {
					return false;
				}

				if (targetY - i >= 0 && chessboard[targetY - i][targetX + i]) {
					return false;
				}
			}

			if (targetX - i >= 0) {

				if (chessboard[targetY][targetX - i]) {
					return false;
				}

				if (targetY + i < chessboardSize && chessboard[targetY + i][targetX - i]) {
					return false;
				}

				if (targetY - i >= 0 && chessboard[targetY - i][targetX - i]) {
					return false;
				}
			}

			if (targetY + i < chessboardSize && chessboard[targetY + i][targetX]) {
				return false;
			}
			if (targetY - i >= 0 && chessboard[targetY - i][targetX]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Main program.
	 * 
	 * @param args options
	 * @throws InvalidSolutionsException
	 */
	public static void main(final String args[]) throws InvalidSolutionsException {

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
					System.err.println("Usage: " + BasicNQueensSolver.class.getSimpleName() + " [-n <size of the chessboard>] [-s]"); //$NON-NLS-1$ //$NON-NLS-2$
					System.exit(1);
				}
				break;

			case 's':
				printSolution = false;
				break;

			case '?':
			default:
				System.err.println("Usage: " + BasicNQueensSolver.class.getSimpleName() + " [-n <size of the chessboard>] [-s]"); //$NON-NLS-1$ //$NON-NLS-2$
				System.exit(1);
			}

			c = getOpt.getopt();
		}

		final GenericNQueensSolver genericNQueensSolver = new BasicNQueensSolver(chessboardSize, printSolution);
		final long solutionCount = genericNQueensSolver.solve();

		// End of the algorithm print the total of solution(s) found
		System.out.println("\nTotal number of solution(s):" + solutionCount); //$NON-NLS-1$

		if (!SequenceTools.checkSolutionsFound(chessboardSize, solutionCount)) {

			System.err.println("Invalid number of solutions found: " + solutionCount + " expected: " + SequenceTools.getExpectedSolutions(chessboardSize) + " check the algorithm."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		System.out.println(BenchmarkTools.benchmark(new BasicNQueensSolver(chessboardSize, false), 50));
	}

	@Override
	public void reset() {

		// Reinitialize the number of solutions found
		solutionCount = 0;
	}

	@Override
	public boolean getChessboardPosition(final int x, final int y) {
		return chessboard[y][x];
	}
}
