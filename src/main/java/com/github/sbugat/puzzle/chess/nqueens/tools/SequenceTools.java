package com.github.sbugat.puzzle.chess.nqueens.tools;

/**
 * Tools to check final solutions counts based on a fixed solution sequence. The sequence is based on <a href="http://oeis.org/A000170">OEIS A000170</a>.
 * 
 * @author Sylvain Bugat
 * 
 */
public final class SequenceTools {

	private static final long[] SOLUTION_SEQUENCE = { 0, 1, 0, 0, 2, 10, 4, 40, 92, 352, 724, 2680, 14200, 73712, 365596, 2279184, 14772512, 95815104, 666090624, 4968057848L, 39029188884L, 314666222712L, 2691008701644L, 24233937684440L, 227514171973736L, 2207893435808352L, 22317699616364044L };

	public static boolean checkSolutionsFound(final int chessboardSize, final long solutionsFound) {

		if (chessboardSize < 0 || chessboardSize > SOLUTION_SEQUENCE.length) {
			throw new IllegalArgumentException("Invalid chess size " + chessboardSize); //$NON-NLS-1$
		}

		return SOLUTION_SEQUENCE[chessboardSize] == solutionsFound;
	}

	public static long getExpectedSolutions(final int chessboardSize) {

		if (chessboardSize < 0 || chessboardSize > SOLUTION_SEQUENCE.length) {
			throw new IllegalArgumentException("Invalid chess size " + chessboardSize); //$NON-NLS-1$
		}

		return SOLUTION_SEQUENCE[chessboardSize];
	}
}
