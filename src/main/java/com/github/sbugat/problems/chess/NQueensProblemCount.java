package com.github.sbugat.problems.chess;

import gnu.getopt.Getopt;

import java.io.IOException;
import java.util.Arrays;

/**
 * Classic N chess queens on a size N chess board
 *
 * Time on Intel Q6600 CPU:
 * 8       9         10       11       12       13       14       15        16
 * 0m0.140s 0m0.112s 0m0.124s 0m0.133s 0m0.161s 0m0.411s 0m1.710s 0m11.115s 1m13.614s
 *
 * @author Sylvain Bugat
 *
 */
public class NQueensProblemCount {

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

	private final int [] stackx;
	private final int [] stackdiag1;
	private final int [] stackdiag2;

	private int stacklevel=1;

	public NQueensProblemCount( final int chessboardSizeArg ) throws IOException {

		chessboardSize = chessboardSizeArg;
		chessboardSizeMinusOne = chessboardSizeArg - 1;

		unusedColumns = new boolean[ chessboardSizeArg ];
		Arrays.fill( unusedColumns, true );
		unusedAscendingDiagonals = new boolean[ chessboardSizeArg * 2 - 1 ];
		Arrays.fill( unusedAscendingDiagonals, true );
		unusedDescendingDiagonals = new boolean[ chessboardSizeArg * 2 - 1 ];
		Arrays.fill( unusedDescendingDiagonals, true );

		stackx = new int[ chessboardSizeArg ];
		stackdiag1 = new int[ chessboardSizeArg ];
		stackdiag2 = new int[ chessboardSizeArg ];

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
			stackx[ 0 ] = x;
			stacklevel = 1;
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
			stacklevel = 1;
			stackx[ 0 ] = x;
			solve( 1 );

			unusedDescendingDiagonals[ diag2 ] = true;
			unusedAscendingDiagonals[ diag1 ] = true;
			unusedColumns[ x ] = true;
		}
	}

	/**
	 * Solving recursive method, do a depth-first/back-tracking algorithm
	 *
	 * @param y number of the line stating at 0
	 */
	private void solve( int y ) {

		int x=-1;
		while( stacklevel > 0 ) {
			//Test all square of the line
			x++;

			//if the row is not already blocked by another queen
			if( unusedColumns[x] ) {

				final int diag1 = x + stacklevel;
				final int diag2 = x + chessboardSizeMinusOne - stacklevel ;

				//if both diagonals are not already blocked by anothers queens
				if( unusedAscendingDiagonals[ diag1 ] && unusedDescendingDiagonals[ diag2 ] ) {

					unusedColumns[ x ] = false;
					unusedAscendingDiagonals[ diag1 ] = false;
					unusedDescendingDiagonals[ diag2 ] = false;

					stackx[ stacklevel ] = x;
					stackdiag1[ stacklevel ] = diag1;
					stackdiag2[ stacklevel ] = diag2;
					stacklevel ++;

					//All queens are sets on the chessboard then a solution is found!
					if( stacklevel >= chessboardSize ) {
						solutionCount++;

						x = chessboardSize - 1;
					}
					else {
						x = -1;
					}
				}
			}

			while( x >= chessboardSizeMinusOne ) {

				stacklevel --;
				if( stacklevel > 0 ) {
					unusedDescendingDiagonals[ stackdiag2[ stacklevel ] ] = true;
					unusedAscendingDiagonals[ stackdiag1[ stacklevel ] ] = true;
					x = stackx[ stacklevel ];
					unusedColumns[ x ] = true;
				}
				else {
					return;
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
