package com.github.sbugat.puzzle.chess.nqueens.tools;

public class InvalidSolutionsException extends Exception {

	private static final long serialVersionUID = -2918035232656432720L;

	private final long solutionFound;

	private final long solutionExpected;

	private final int puzzleSize;

	public InvalidSolutionsException(final long solutionFoundArg, final long solutionExpectedArg, final int puzzleSizeArg) {

		solutionFound = solutionFoundArg;
		solutionExpected = solutionExpectedArg;
		puzzleSize = puzzleSizeArg;
	}

}
