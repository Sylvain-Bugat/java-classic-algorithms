package com.github.sbugat.problems.chess;

import gnu.getopt.Getopt;

import java.util.Arrays;

/**
 * Classic N chess queens on a size N chess board
 *
 * Time on Intel Q6600 CPU:
 * 8       9         10       11       12       13       14       15        16
 * 0m0.101s 0m0.118s 0m0.118s 0m0.128s 0m0.174s 0m0.506s 0m2.130s 0m15.820s 1m30.699s
 *
 * @author Sylvain Bugat
 *
 */
public class NQueensProblemCountRecursive {

	/**Array to mark unused columns*/
	private final boolean [] unusedColumns;
	/**Array to mark unused ascending diagonals
	 * diagonal number = x + y */
	private final boolean [] unusedAscendingDiagonals;
	/**Array to mark unused descending diagonals
	 * diagonal number = x + chess board size - 1 - y*/
	private final boolean [] unusedDescendingDiagonals;
	/**Number of solution counter*/
	private long solutionCount;

	/**Size of the chess board*/
	private final int chessboardSize;
	private final int chessboardSizeMinusOne;

	public NQueensProblemCountRecursive( final int chessboardSizeArg ) {

		chessboardSize = chessboardSizeArg;
		chessboardSizeMinusOne = chessboardSizeArg - 1;

		unusedColumns = new boolean[ chessboardSizeArg ];
		Arrays.fill( unusedColumns, true );
		unusedAscendingDiagonals = new boolean[ chessboardSizeArg * 2 - 1 ];
		Arrays.fill( unusedAscendingDiagonals, true );
		unusedDescendingDiagonals = new boolean[ chessboardSizeArg * 2 - 1 ];
		Arrays.fill( unusedDescendingDiagonals, true );

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
			final int diag2 = x + chessboardSizeMinusOne;

			unusedColumns[ x ] = false;
			unusedAscendingDiagonals[ diag1 ] = false;
			unusedDescendingDiagonals[ diag2 ] = false;

			//Go on to the second line
			solve( 1 );

			unusedDescendingDiagonals[ diag2 ] = true;
			unusedAscendingDiagonals[ diag1 ] = true;
			unusedColumns[ x ] = true;
		}

		//Multiply by 2 the solution count for the other half not calculated
		solutionCount *= 2;

		//If the cheesboard size is odd, test with a queen on the middle of the first line
		if( 0 != chessboardSize % 2 ) {

			final int x=chessboardSize/2;

			final int diag1 = x;
			final int diag2 = x + chessboardSizeMinusOne;

			unusedColumns[ x ] = false;
			unusedAscendingDiagonals[ diag1 ] = false;
			unusedDescendingDiagonals[ diag2 ] = false;

			//Go on to the second line
			solve( 1 );

			unusedDescendingDiagonals[ diag2 ] = true;
			unusedAscendingDiagonals[ diag1 ] = true;
			unusedColumns[ x ] = true;
		}
	}

	/**
	 * Solving using recursive method, do a depth-first/back-tracking algorithm
	 *
	 * @param y number of the line stating at 0
	 */
	private void solve( final int y ) {

		//Test all position on the line
		for( int x=0 ; x < chessboardSize ; x ++ ){

			//if the row is not already blocked by another queen
			if( unusedColumns[x] ) {

				final int diag1 = x + y;
				final int diag2 = x + chessboardSizeMinusOne - y ;

				//if both diagonals are not already blocked by anothers queens
				if( unusedAscendingDiagonals[ diag1 ] && unusedDescendingDiagonals[ diag2 ] ) {

					unusedColumns[ x ] = false;
					unusedAscendingDiagonals[ diag1 ] = false;
					unusedDescendingDiagonals[ diag2 ] = false;

					//All queens are sets on the chessboard then a solution is found!
					if( y >= chessboardSizeMinusOne ) {
						solutionCount++;
					}
					else {
						//Go on to the next line
						solve( y + 1 );
					}

					unusedDescendingDiagonals[ diag2 ] = true;
					unusedAscendingDiagonals[ diag1 ] = true;
					unusedColumns[ x ] = true;
				}
			}
		}
	}

	/**
	 * main program
	 *
	 * @param args
	 */
	public static void main( final String args[] ) {

		final Getopt getOpt = new Getopt( NQueensProblemCountRecursive.class.getSimpleName(), args, ":n:" );
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
						System.err.println( "Usage: " + NQueensProblemCountRecursive.class.getSimpleName() + " [-n <size of the chessboard>]" );
						System.exit( 1 );
					}
				}
				catch( final NumberFormatException e ) {
					System.err.println( "Usage: " + NQueensProblemCountRecursive.class.getSimpleName() + " [-n <size of the chessboard>]" );
					System.exit( 1 );
				}
				break;

			case '?':
			default:
				System.err.println( "Usage: " + NQueensProblemCountRecursive.class.getSimpleName() + " [-n <size of the chessboard>]" );
				System.exit( 1 );
			}

			c = getOpt.getopt();
		}

		new NQueensProblemCountRecursive( chessBoardSize );
	}
}
