package com.github.sbugat.problems.chess;

import gnu.getopt.Getopt;

import java.io.IOException;
import java.util.Arrays;

/**
 * Classic N chess queens on a size N chess board
 *
 * @author Sylvain Bugat
 *
 */
public class NQueensProblemCount {

	/**Array to mark already used columns*/
	private final boolean [] usedColumns;
	/**Array to mark already used ascending diagonals
	 * diagonal number = x + y */
	private final boolean [] usedAscendingDiagonals;
	/**Array to mark already used descending diagonals
	 * diagonal number = x + chess board size - 1 - y*/
	private final boolean [] usedDescendingDiagonals;
	/**Number of solution counter*/
	private long solutionCount;

	/**Size of the chess board*/
	private final int chessboardSize;

	public NQueensProblemCount( final int chessboardSizeArg ) throws IOException {

		chessboardSize = chessboardSizeArg;

		usedColumns = new boolean[ chessboardSizeArg ];
		Arrays.fill( usedColumns, true );
		usedAscendingDiagonals = new boolean[ chessboardSizeArg * 2 - 1 ];
		Arrays.fill( usedAscendingDiagonals, true );
		usedDescendingDiagonals = new boolean[ chessboardSizeArg * 2 - 1 ];
		Arrays.fill( usedDescendingDiagonals, true );

		//Start the algorithm at the fisrt line
		solve();

		//End of the algorithm print the total of solution(s) found
		System.out.println( "Total number of solution(s):" + solutionCount );
	}

	/**
	 * First line to divide by 2 explored tree
	 */
	private void solve() {

		//Test half square of the line
		for( int x=0 ; x < chessboardSize/2 ; x ++ ){

			final int diag1 = x;
			final int diag2 = x + chessboardSize - 1;

			usedColumns[ x ] = false;
			usedAscendingDiagonals[ diag1 ] = false;
			usedDescendingDiagonals[ diag2 ] = false;

			//Go on to the second line
			solve( 1 );

			usedDescendingDiagonals[ diag2 ] = true;
			usedAscendingDiagonals[ diag1 ] = true;
			usedColumns[ x ] = true;
		}

		//Multiply by 2 the solution count for the other half not calculated
		solutionCount *= 2;

		//If the cheesboard size is odd, test with a queen on the middle of the first line
		if( 0 != chessboardSize % 2 ) {

			final int x=chessboardSize/2;

			final int diag1 = x;
			final int diag2 = x + chessboardSize - 1;

			usedColumns[ x ] = false;
			usedAscendingDiagonals[ diag1 ] = false;
			usedDescendingDiagonals[ diag2 ] = false;

			//Go on to the second line
			solve( 1 );

			usedDescendingDiagonals[ diag2 ] = true;
			usedAscendingDiagonals[ diag1 ] = true;
			usedColumns[ x ] = true;
		}
	}

	/**
	 * Solving recursive method, do a depth-first/back-tracking algorithm
	 *
	 * @param y number of the line stating at 0
	 */
	private void solve( final int y ) {

		//Test all square of the line
		for( int x=0 ; x < chessboardSize ; x ++ ){

			//if the row is not already blocked by another queen
			if( usedColumns[x] ) {

				final int diag1 = x + y;
				final int diag2 = x + chessboardSize - 1 - y ;

				//if both diagonals are not already blocked by anothers queens
				if( usedAscendingDiagonals[ diag1 ] && usedDescendingDiagonals[ diag2 ] ) {

					usedColumns[ x ] = false;
					usedAscendingDiagonals[ diag1 ] = false;
					usedDescendingDiagonals[ diag2 ] = false;

					//All queens are sets on the chessboard then a solution is found!
					if( y + 1 >= chessboardSize ) {
						solutionCount++;
					}
					else {
						//Go on to the next line
						solve( y + 1 );
					}

					usedDescendingDiagonals[ diag2 ] = true;
					usedAscendingDiagonals[ diag1 ] = true;
					usedColumns[ x ] = true;
				}
			}
		}
	}

	/**
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main( final String args[] ) throws IOException {

		final Getopt getOpt = new Getopt( "NQueensProblem", args, ":n:" );
		getOpt.setOpterr( false );

		//Default chessboard size
		int chessBoardSize = 8;

		int c = getOpt.getopt();
		while( -1 != c )
		{
			switch(c)
			{
			case 'n':
				try {
					chessBoardSize = Integer.parseInt( getOpt.getOptarg() );

					if( chessBoardSize < 2 ) {
						System.err.println( "Usage: " + NQueensProblemCount.class.getSimpleName() + " [-n <size of the chessboard>]" );
						System.exit( 1 );
					}
				}
				catch( final NumberFormatException e ) {
					System.err.println( "Usage: " + NQueensProblemCount.class.getSimpleName() + " [-n <size of the chessboard>]" );
					System.exit( 1 );
				}
				break;

			case '?':
			default:
				System.err.println( "Usage: " + NQueensProblemCount.class.getSimpleName() + " [-n <size of the chessboard>]" );
				System.exit( 1 );
			}

			c = getOpt.getopt();
		}

		new NQueensProblemCount( chessBoardSize );
	}
}
