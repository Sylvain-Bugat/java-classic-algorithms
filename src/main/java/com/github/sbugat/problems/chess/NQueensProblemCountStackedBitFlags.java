package com.github.sbugat.problems.chess;

import gnu.getopt.Getopt;

/**
 * Classic N chess queens on a size N chess board with bits flags
 *
 * Time on Intel Q6600 CPU:
 * 8       9         10       11       12       13       14       15        16
 * 0m0.148s 0m0.101s 0m0.118s 0m0.132s 0m0.136s 0m0.180s 0m0.449s 0m2.325s 0m14.086s
 *
 * @author Sylvain Bugat
 *
 */
public class NQueensProblemCountStackedBitFlags {

	/**long bits flag to mark unused columns*/
	//private int unusedColumns;
	/**long bits flag to mark unused ascending diagonals, first */
	//private long unusedAscendingDiagonals;
	/**long bits flag to mark unused descending diagonals */
	//private long unusedDescendingDiagonals;
	/**Number of solution counter*/
	private long solutionCount;

	/**Size of the chess board*/
	private final int chessboardSize;
	private final int chessboardSizeMinusOne;

	private final int [] bitFlagsStack;
	private int bitFlagsMask;
	private final int [] stackx;
	private final int [] unusedColumnsStack;
	private final int [] unusedAscendingDiagonalsStack;
	private final int [] unusedDescendingDiagonalsStack;
	private int stacklevel=1;

	public NQueensProblemCountStackedBitFlags( final int chessboardSizeArg ) {

		chessboardSize = chessboardSizeArg;
		chessboardSizeMinusOne = chessboardSizeArg - 1;

		bitFlagsStack = new int[ chessboardSizeArg ];

		for(int i=0 ; i < chessboardSizeArg ; i++ ) {
			bitFlagsMask |= 1 << i;
		}

		stackx = new int[ chessboardSizeArg ];
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
			stackx[ 0 ] = x;
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
			stackx[ 0 ] = x;
			stacklevel = 0;
			solve( bitFlags );
		}
	}

	/**
	 * Solving recursive method, do a depth-first/back-tracking algorithm
	 */
	private void solve( int bitFlags ) {

		int x=-1;
		while( stacklevel > -1 ) {
			//Test all square of the line
			x++;

			int targetQueen = Integer.highestOneBit( ~( bitFlags ) & bitFlagsMask );
			//if the row is not already blocked by another queen and if both diagonals are not already blocked by anothers queens
			if( targetQueen != 0 ) {

				bitFlags |= targetQueen;

				//All queens are sets on the chessboard then a solution is found!
				if( stacklevel + 1 >= chessboardSizeMinusOne ) {
					solutionCount++;

					x = chessboardSizeMinusOne;
				}
				else {

					final int prevStacklevel = stacklevel++;
					stackx[ stacklevel ] = x;
					bitFlagsStack[ stacklevel ] = bitFlags;

					unusedColumnsStack[ stacklevel ] = unusedColumnsStack[ prevStacklevel ] | targetQueen;
					unusedAscendingDiagonalsStack[ stacklevel ] = ( unusedAscendingDiagonalsStack[ prevStacklevel ] | targetQueen ) << 1;
					unusedDescendingDiagonalsStack[ stacklevel ] = ( unusedDescendingDiagonalsStack[ prevStacklevel ] | targetQueen ) >> 1;
					bitFlags = bitFlagsMask & ( unusedColumnsStack[ stacklevel ] | unusedAscendingDiagonalsStack[ stacklevel ] | unusedDescendingDiagonalsStack[ stacklevel ] );
					x = -1;
				}
			}

			while( bitFlags == bitFlagsMask ) {

				if( stacklevel > 0 ) {
					x = stackx[ stacklevel ];
					bitFlags = bitFlagsStack[ stacklevel ];
					stacklevel --;
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
