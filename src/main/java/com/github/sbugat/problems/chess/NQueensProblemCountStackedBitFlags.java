package com.github.sbugat.problems.chess;

import gnu.getopt.Getopt;

/**
 * Classic N chess queens on a size N chess board with bits flags
 *
 * Time on Intel Q6600 CPU:
 * 8       9         10       11       12       13       14       15        16
 * 0m0.151s 0m0.114s 0m0.112s 0m0.121s 0m0.127s 0m0.151s 0m0.328s 0m1.549s 0m8.948s
 *
 * @author Sylvain Bugat
 *
 */
public class NQueensProblemCountStackedBitFlags {

	/**Number of solution counter*/
	private long solutionCount;

	/**Size of the chess board*/
	private final int chessboardSize;
	private final int chessboardSizeMinusTwo;

	private final int [] bitFlagsStack;
	private int bitFlagsMask;
	private final int [] unusedColumnsStack;
	private final int [] unusedAscendingDiagonalsStack;
	private final int [] unusedDescendingDiagonalsStack;
	private int stacklevel=1;

	public NQueensProblemCountStackedBitFlags( final int chessboardSizeArg ) {

		chessboardSize = chessboardSizeArg;
		chessboardSizeMinusTwo = chessboardSizeArg - 2;

		bitFlagsStack = new int[ chessboardSizeArg ];

		for(int i=0 ; i < chessboardSizeArg ; i++ ) {
			bitFlagsMask |= 1 << i;
		}

		unusedColumnsStack = new int[ chessboardSizeArg ];
		unusedAscendingDiagonalsStack = new int[ chessboardSizeArg ];
		unusedDescendingDiagonalsStack = new int[ chessboardSizeArg ];

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

			unusedColumnsStack[ 0 ] = 1 << x;
			unusedAscendingDiagonalsStack[ 0 ] = ( 0b10 << x );
			unusedDescendingDiagonalsStack[ 0 ] = ( 1 << x - 1 );
			final int bitFlags = bitFlagsMask & ( unusedColumnsStack[ 0 ] | unusedAscendingDiagonalsStack[ 0 ] | unusedDescendingDiagonalsStack[ 0 ] );
			bitFlagsStack[ 0 ] = bitFlags;

			//Go on to the second line
			stacklevel = 0;
			solve( bitFlags );
		}

		//Multiply by 2 the solution count for the other half not calculated
		solutionCount *= 2;

		//If the cheesboard size is odd, test with a queen on the middle of the first line
		if( 0 != chessboardSize % 2 ) {

			final int x=chessboardSize/2;

			unusedColumnsStack[ 0 ] = 1 << x;
			unusedAscendingDiagonalsStack[ 0 ] = ( 0b10 << x );
			unusedDescendingDiagonalsStack[ 0 ] = ( 1 << x - 1 );

			final int bitFlags = bitFlagsMask & ( unusedColumnsStack[ 0 ] | unusedAscendingDiagonalsStack[ 0 ] | unusedDescendingDiagonalsStack[ 0 ] );
			bitFlagsStack[ 0 ] = bitFlags;

			//Go on to the second line
			stacklevel = 0;
			solve( bitFlags );
		}
	}

	/**
	 * Solving with iterative/stacking method by using bit flags, do a depth-first/back-tracking algorithm
	 *
	 * @param initial bitFlag with a single queen on the first line
	 */
	private void solve( int bitFlags ) {

		int prevStacklevel = stacklevel - 1;
		//Infinite loop, exit condition is tested when unstacking a queen
		while( true ) {

			//Test first possible queen of the line using direct inlining(manual code copy) of this method call: Integer.lowestOneBit( ~( bitFlags ) & bitFlagsMask );
			int targetQueen = -( ~( bitFlags ) & bitFlagsMask ) & ( ~( bitFlags ) & bitFlagsMask );
			//if the row is not already blocked by another queen and if both diagonals are not already blocked by anothers queens
			if( targetQueen != 0 ) {

				//Mark the current target queen as tested for this stack level
				bitFlags |= targetQueen;

				//All queens are sets on the chessboard then a solution is found!
				//Test with the board size minus 2 because the targeted queen is not placed yet
				if( stacklevel >= chessboardSizeMinusTwo ) {
					solutionCount++;
				}
				else {

					prevStacklevel = stacklevel++;
					bitFlagsStack[ stacklevel ] = bitFlags;

					//unusedColumnsStack[ stacklevel ] = unusedColumnsStack[ prevStacklevel ] | targetQueen;
					//unusedAscendingDiagonalsStack[ stacklevel ] = ( unusedAscendingDiagonalsStack[ prevStacklevel ] | targetQueen ) << 1;
					//unusedDescendingDiagonalsStack[ stacklevel ] = ( unusedDescendingDiagonalsStack[ prevStacklevel ] | targetQueen ) >> 1;
					//bitFlags = bitFlagsMask & ( unusedColumnsStack[ stacklevel ] | unusedAscendingDiagonalsStack[ stacklevel ] | unusedDescendingDiagonalsStack[ stacklevel ] );

					//Update bit flags and do 3 stacks updates (4 previous commented lines in 1)
					bitFlags = bitFlagsMask & ( ( unusedColumnsStack[ stacklevel ] = unusedColumnsStack[ prevStacklevel ] | targetQueen )
												| ( unusedAscendingDiagonalsStack[ stacklevel ] = ( unusedAscendingDiagonalsStack[ prevStacklevel ] | targetQueen ) << 1 )
												| ( unusedDescendingDiagonalsStack[ stacklevel ] = ( unusedDescendingDiagonalsStack[ prevStacklevel ] | targetQueen ) >> 1 )
											);
				}
			}

			//If all positions have been tested or are already blocked by a column or a diagonal
			while( bitFlags >= bitFlagsMask ) {

				//If there is still something to unstack
				if( stacklevel > 0 ) {
					//Backtrace process
					bitFlags = bitFlagsStack[ stacklevel ];
					stacklevel --;
				}
				//Exit if all possibilities are tested
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

		final Getopt getOpt = new Getopt( NQueensProblemCountStackedBitFlags.class.getSimpleName(), args, ":n:" );
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
						System.err.println( "Usage: " + NQueensProblemCountStackedBitFlags.class.getSimpleName() + " [-n <size of the chessboard>]" );
						System.exit( 1 );
					}
				}
				catch( final NumberFormatException e ) {
					System.err.println( "Usage: " + NQueensProblemCountStackedBitFlags.class.getSimpleName() + " [-n <size of the chessboard>]" );
					System.exit( 1 );
				}
				break;

			case '?':
			default:
				System.err.println( "Usage: " + NQueensProblemCountStackedBitFlags.class.getSimpleName() + " [-n <size of the chessboard>]" );
				System.exit( 1 );
			}

			c = getOpt.getopt();
		}

		new NQueensProblemCountStackedBitFlags( chessBoardSize );
	}
}
