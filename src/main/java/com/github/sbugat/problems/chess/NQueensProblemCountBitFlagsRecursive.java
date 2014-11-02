package com.github.sbugat.problems.chess;

import gnu.getopt.Getopt;

/**
 * Classic N chess queens on a size N chess board with bits flags
 *
 * Time on Intel Q6600 CPU:
 * 8       9         10       11       12       13       14       15        16
 * 0m0.112s 0m0.119s 0m0.109s 0m0.119s 0m0.159s 0m0.361s 0m1.500s 0m9.468s 1m1.047s
 *
 * @author Sylvain Bugat
 *
 */
public class NQueensProblemCountBitFlagsRecursive {

	/** long bits flag to mark unused columns */
	private int unusedColumns;
	/** long bits flag to mark unused ascending diagonals, first */
	private long unusedAscendingDiagonals;
	/** long bits flag to mark unused descending diagonals */
	private long unusedDescendingDiagonals;
	/** Number of solution counter */
	private long solutionCount;

	/** Size of the chess board */
	private final int chessboardSize;

	/** bits flag shifting for diagonals bits flags */
	private final int diagonalShifting;

	public NQueensProblemCountBitFlagsRecursive(final int chessboardSizeArg) {

		chessboardSize = chessboardSizeArg;
		diagonalShifting = 32 - chessboardSizeArg / 2;

		// Start the algorithm at the fisrt line
		solve();

		// End of the algorithm print the total of solution(s) found
		System.out.println("Total number of solution(s):" + solutionCount);
	}

	/**
	 * First line to divide by 2 explored tree
	 */
	private void solve() {

		// Test half square of the line
		for (int x = 0; x < chessboardSize / 2; x++) {

			unusedColumns = unusedColumns | (1 << x);
			unusedAscendingDiagonals = (unusedAscendingDiagonals | (1L << diagonalShifting + x)) << 1;
			unusedDescendingDiagonals = (unusedDescendingDiagonals | (1L << diagonalShifting + x)) >> 1;

			// Go on to the second line
			solve(1);

			unusedColumns = unusedColumns ^ (1 << x);
			unusedAscendingDiagonals = unusedAscendingDiagonals >> 1 ^ (1L << diagonalShifting + x);
			unusedDescendingDiagonals = unusedDescendingDiagonals << 1 ^ (1L << diagonalShifting + x);
		}

		// Multiply by 2 the solution count for the other half not calculated
		solutionCount *= 2;

		// If the cheesboard size is odd, test with a queen on the middle of the first line
		if (0 != chessboardSize % 2) {

			final int x = chessboardSize / 2;

			unusedColumns = unusedColumns | (1 << x);
			unusedAscendingDiagonals = (unusedAscendingDiagonals | (1L << diagonalShifting + x)) << 1;
			unusedDescendingDiagonals = (unusedDescendingDiagonals | (1L << diagonalShifting + x)) >> 1;

			// Go on to the second line
			solve(1);

			unusedColumns = unusedColumns ^ (1 << x);
			unusedAscendingDiagonals = unusedAscendingDiagonals >> 1 ^ (1L << diagonalShifting + x);
			unusedDescendingDiagonals = unusedDescendingDiagonals << 1 ^ (1L << diagonalShifting + x);
		}
	}

	/**
	 * Solving recursive method, do a depth-first/back-tracking algorithm with bit flags to mark used columns and diagonals
	 *
	 * @param y number of the line stating at 0
	 */
	private void solve(final int y) {

		// Test all position of the line
		for (int x = 0; x < chessboardSize; x++) {

			// if the row is not already blocked by another queen
			if ((unusedColumns & (1L << x)) == 0) {

				// if both diagonals are not already blocked by anothers queens
				if ((unusedAscendingDiagonals & (1L << diagonalShifting + x)) == 0 && (unusedDescendingDiagonals & (1L << diagonalShifting + x)) == 0) {

					unusedColumns = unusedColumns | (1 << x);
					unusedAscendingDiagonals = (unusedAscendingDiagonals | (1L << diagonalShifting + x)) << 1;
					unusedDescendingDiagonals = (unusedDescendingDiagonals | (1L << diagonalShifting + x)) >> 1;

					// All queens are sets on the chessboard then a solution is found!
					if (y + 1 >= chessboardSize) {
						solutionCount++;
					}
					else {
						// Go on to the next line
						solve(y + 1);
					}

					unusedColumns = unusedColumns ^ (1 << x);
					unusedAscendingDiagonals = unusedAscendingDiagonals >> 1 ^ (1L << diagonalShifting + x);
					unusedDescendingDiagonals = unusedDescendingDiagonals << 1 ^ (1L << diagonalShifting + x);
				}
			}
		}
	}

	/**
	 * Maisn program
	 *
	 * @param args
	 */
	public static void main(final String args[]) {

		final String usage = "Usage: " + NQueensProblemCountBitFlagsRecursive.class.getSimpleName() + " [-n <size of the chessboard>]";

		final Getopt getOpt = new Getopt(NQueensProblemCountBitFlagsRecursive.class.getSimpleName(), args, ":n:");
		getOpt.setOpterr(false);

		// Default chessboard size
		int chessBoardSize = 8;

		int c = getOpt.getopt();
		while (-1 != c) {
			switch (c) {
			case 'n':
				try {
					chessBoardSize = Integer.parseInt(getOpt.getOptarg());

					if (chessBoardSize < 2 || chessBoardSize > 31) {
						System.err.println(usage);
						System.exit(1);
					}
				}
				catch (final NumberFormatException e) {
					System.err.println(usage);
					System.exit(1);
				}
				break;

			case '?':
			default:
				System.err.println(usage);
				System.exit(1);
			}

			c = getOpt.getopt();
		}

		new NQueensProblemCountBitFlagsRecursive(chessBoardSize);
	}
}
