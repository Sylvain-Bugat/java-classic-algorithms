package com.github.sbugat.problems.chess;

import gnu.getopt.Getopt;

/**
 * Classic N chess queens on a size N chess board with bits flags
 *
 * Time on Intel Q6600 CPU:
 * 8       9         10       11       12       13       14       15        16
 * 0m0.134s 0m0.117s 0m0.124s 0m0.128s 0m0.142s 0m0.314s 0m1.253s 0m8.072s 0m50.754s
 *
 * @author Sylvain Bugat
 *
 */
public class NQueensProblemCountBitFlagsIterative {

	/**long bits flag to mark unused columns*/
	private int unusedColumns;
	/**long bits flag to mark unused ascending diagonals, first */
	private long unusedAscendingDiagonals;
	/**long bits flag to mark unused descending diagonals */
	private long unusedDescendingDiagonals;
	/**Number of solution counter*/
	private long solutionCount;

	/**Size of the chess board*/
	private final int chessboardSize;
	private final int chessboardSizeMinusOne;

	/**bits flag shifting for diagonals bits flags*/
	private final int diagonalShifting;

	private final int [] stackx;
	private int stacklevel=1;

	public NQueensProblemCountBitFlagsIterative( final int chessboardSizeArg ) {

		chessboardSize = chessboardSizeArg;
		chessboardSizeMinusOne = chessboardSizeArg - 1;
		diagonalShifting = 32 - chessboardSizeArg / 2;

		stackx = new int[ chessboardSizeArg ];

		//Start the algorithm at the fisrt line
		firstSolve();

		//End of the algorithm print the total of solution(s) found
		System.out.println( "Total number of solution(s):" + solutionCount );
	}

	/**
	 * First line to divide by 2 explored tree
	 */
	private void firstSolve() {

		//Test half square of the line
		for( int x=0 ; x < chessboardSize/2 ; x ++ ){

			unusedColumns = unusedColumns | ( 1 << x );
			unusedAscendingDiagonals = ( unusedAscendingDiagonals | ( 1L << diagonalShifting + x ) ) << 1;
			unusedDescendingDiagonals = ( unusedDescendingDiagonals | ( 1L << diagonalShifting + x ) ) >> 1;

			//Go on to the second line
			stackx[ 0 ] = x;
			stacklevel = 1;
			solve();

			unusedColumns = unusedColumns ^ ( 1 << x );
			unusedAscendingDiagonals = unusedAscendingDiagonals >> 1 ^ ( 1L << diagonalShifting + x );
			unusedDescendingDiagonals = unusedDescendingDiagonals << 1 ^ ( 1L << diagonalShifting + x );
		}

		//Multiply by 2 the solution count for the other half not calculated
		solutionCount *= 2;

		//If the cheesboard size is odd, test with a queen on the middle of the first line
		if( 0 != chessboardSize % 2 ) {

			final int x=chessboardSize/2;

			unusedColumns = unusedColumns | ( 1 << x );
			unusedAscendingDiagonals = ( unusedAscendingDiagonals | ( 1L << diagonalShifting + x ) ) << 1;
			unusedDescendingDiagonals = ( unusedDescendingDiagonals | ( 1L << diagonalShifting + x ) ) >> 1;

			//Go on to the second line
			stackx[ 0 ] = x;
			stacklevel = 1;
			solve();

			unusedColumns = unusedColumns ^ ( 1 << x );
			unusedAscendingDiagonals = unusedAscendingDiagonals >> 1 ^ ( 1L << diagonalShifting + x );
			unusedDescendingDiagonals = unusedDescendingDiagonals << 1 ^ ( 1L << diagonalShifting + x );
		}
	}

	/**
	 * Solving recursive method, do a depth-first/back-tracking algorithm
	 */
	private void solve() {

		int x=-1;
		while( stacklevel > 0 ) {
			//Test all square of the line
			x++;

			//if the row is not already blocked by another queen and if both diagonals are not already blocked by anothers queens
			if( ( unusedColumns & ( 1 << x ) ) + ( unusedAscendingDiagonals & ( 1L << diagonalShifting + x ) ) + ( unusedDescendingDiagonals & ( 1L << diagonalShifting + x ) ) == 0 ) {

				stackx[ stacklevel ] = x;

				unusedColumns = unusedColumns | ( 1 << x );
				unusedAscendingDiagonals = ( unusedAscendingDiagonals | ( 1L << diagonalShifting + x ) ) << 1;
				unusedDescendingDiagonals = ( unusedDescendingDiagonals | ( 1L << diagonalShifting + x ) ) >> 1;

				stacklevel ++;

				//All queens are sets on the chessboard then a solution is found!
				if( stacklevel >= chessboardSize ) {
					solutionCount++;

					x = chessboardSizeMinusOne;
				}
				else {
					x = -1;
				}
			}

			while( x >= chessboardSizeMinusOne ) {

				stacklevel --;
				if( stacklevel > 0 ) {
					x = stackx[ stacklevel ];
					unusedColumns = unusedColumns ^ ( 1 << x );
					unusedAscendingDiagonals = unusedAscendingDiagonals >> 1 ^ ( 1L << diagonalShifting + x );
					unusedDescendingDiagonals = unusedDescendingDiagonals << 1 ^ ( 1L << diagonalShifting + x );
				}
				else {
					return;
				}
			}
		}
	}

	/**
	 * Maisn program
	 *
	 * @param args
	 */
	public static void main( final String args[] ) {

		final Getopt getOpt = new Getopt( NQueensProblemCountBitFlagsIterative.class.getSimpleName(), args, ":n:" );
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

					if( chessBoardSize < 2 || chessBoardSize > 31 ) {
						System.err.println( "Usage: " + NQueensProblemCountBitFlagsIterative.class.getSimpleName() + " [-n <size of the chessboard>]" );
						System.exit( 1 );
					}
				}
				catch( final NumberFormatException e ) {
					System.err.println( "Usage: " + NQueensProblemCountBitFlagsIterative.class.getSimpleName() + " [-n <size of the chessboard>]" );
					System.exit( 1 );
				}
				break;

			case '?':
			default:
				System.err.println( "Usage: " + NQueensProblemCountBitFlagsIterative.class.getSimpleName() + " [-n <size of the chessboard>]" );
				System.exit( 1 );
			}

			c = getOpt.getopt();
		}

		new NQueensProblemCountBitFlagsIterative( chessBoardSize );
	}
}
