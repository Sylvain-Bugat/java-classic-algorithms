package com.github.sbugat.puzzle.chess.nqueens;

public abstract class GenericNQueensSolver {

	/** Size of the chess board. */
	protected final int chessboardSize;

	/** Number of solution counter. */
	protected long solutionCount;

	/** Print solution flag. */
	private final boolean printSolution;

	protected GenericNQueensSolver(final int chessboardSizeArg, final boolean printSolutionArg) {
		chessboardSize = chessboardSizeArg;
		printSolution = printSolutionArg;
	}

	/**
	 * Print the current chessboard.
	 */
	public void print() {

		if (printSolution) {
			System.out.println("\nsolution number " + solutionCount); //$NON-NLS-1$

			for (int y = 0; y < chessboardSize; y++) {

				for (int x = 0; x < chessboardSize; x++) {

					if (getChessboardPosition(y, x)) {
						System.out.print(1);
					} else {
						System.out.print(0);
					}
				}
				System.out.println();
			}
		}
	}

	public int getPuzzleSize() {
		return chessboardSize;
	}

	public abstract long solve();

	public abstract void reset();

	public abstract boolean getChessboardPosition(final int x, final int y);
}
