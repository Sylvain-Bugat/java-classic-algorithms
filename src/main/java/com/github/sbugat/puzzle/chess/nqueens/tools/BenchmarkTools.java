package com.github.sbugat.puzzle.chess.nqueens.tools;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.sbugat.puzzle.chess.nqueens.GenericNQueensSolver;

public class BenchmarkTools {

	public static long benchmark(final GenericNQueensSolver genericNQueensSolver, final int benchmarkNumber) throws InvalidSolutionsException {

		final List<Long> benckmark = new ArrayList<>();

		for (int runNumber = 0; runNumber < benchmarkNumber; runNumber++) {
			genericNQueensSolver.reset();
			final long startNanoTime = System.nanoTime();
			final long solutionCount = genericNQueensSolver.solve();
			final long endNanoTime = System.nanoTime();

			if (!SequenceTools.checkSolutionsFound(genericNQueensSolver.getPuzzleSize(), solutionCount)) {
				throw new InvalidSolutionsException(solutionCount, SequenceTools.getExpectedSolutions(genericNQueensSolver.getPuzzleSize()), genericNQueensSolver.getPuzzleSize());
			}

			benckmark.add(Long.valueOf(endNanoTime - startNanoTime));
		}

		// Exclude 20% slowest and 20% fastest benchmarks (example: for 5 benchmarks, exlude the fastest and slowest benchmark)
		final int numberExcludedRun = benchmarkNumber / 5;

		// Calculate and return the average time of the 60% mid-range
		BigInteger timeSum = BigInteger.valueOf(0L);
		Collections.sort(benckmark);
		int includedRunCount = 0;
		for (int runNumber = numberExcludedRun; runNumber < benchmarkNumber - numberExcludedRun; runNumber++) {
			timeSum = timeSum.add(BigInteger.valueOf(benckmark.get(runNumber).longValue()));

			includedRunCount++;
		}

		return timeSum.divide(BigInteger.valueOf(includedRunCount)).longValue();
	}
}
