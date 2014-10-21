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
public class NQueensProblem {

	/**Chessboard used only to display a solution*/
	private final boolean [][] chessboard;
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
	/**Print solution flag*/
	private final boolean printSolution;

	public NQueensProblem( final int chessboardSizeArg, final boolean printSolutionArg ) throws IOException {

		chessboardSize = chessboardSizeArg;
		printSolution = printSolutionArg;

		chessboard = new boolean[ chessboardSizeArg ][ chessboardSizeArg ];
		unusedColumns = new boolean[ chessboardSizeArg ];
		Arrays.fill( unusedColumns, true );
		unusedAscendingDiagonals = new boolean[ chessboardSizeArg * 2 - 1 ];
		Arrays.fill( unusedAscendingDiagonals, true );
		unusedDescendingDiagonals = new boolean[ chessboardSizeArg * 2 - 1 ];
		Arrays.fill( unusedDescendingDiagonals, true );

		//Start the algorithm at the fisrt line
		solve( 0 );

		//End of the algorithm print the total of solution(s) found
		System.out.println( "\nTotal number of solution(s):" + solutionCount );
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
			if( unusedColumns[x] ) {

				final int diag1 = x + y;
				final int diag2 = x + chessboardSize - 1 - y ;

				//if both diagonals are not already blocked by anothers queens
				if( unusedAscendingDiagonals[ diag1 ] && unusedDescendingDiagonals[ diag2 ] ) {

					chessboard[ y ][ x ] = true;
					unusedColumns[x] = false;
					unusedAscendingDiagonals[ diag1 ] = false;
					unusedDescendingDiagonals[ diag2 ] = false;

					//All queens are sets on the chessboard then a solution is found!
					if( y + 1 >= chessboardSize ) {
						solutionCount++;
						print();
					}
					else {
						//Go on to the next line
						solve( y + 1 );
					}

					unusedDescendingDiagonals[ diag2 ] = true;
					unusedAscendingDiagonals[ diag1 ] = true;
					unusedColumns[ x ] = true;
					chessboard[ y ][ x ] = false;
				}
			}
		}
	}

	/**
	 * Print the current chessboard
	 */
	public void print() {

		if( printSolution ) {
			System.out.println( "\nsolution number " + solutionCount );

			for( int y=0 ; y < chessboardSize ; y++ ) {

				for( int x=0 ; x < chessboardSize ; x++ ) {

					if( chessboard[ y ][ x ] ) {
						System.out.print( 1 );
					}
					else {
						System.out.print( 0 );
					}
				}
				System.out.println();
			}
		}
		else {

			if( 0 == solutionCount % 100 ) {
				System.out.println( "." );
			}
			else {
				System.out.print( "." );
			}
		}
	}

	/**
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main( final String args[] ) throws IOException {

		final Getopt getOpt = new Getopt( "NQueensProblem", args, ":n:s" );
		getOpt.setOpterr( false );

		//Default chessboard size
		int chessBoardSize = 8;
		boolean printSolution = true;

		int c = getOpt.getopt();
		while( -1 != c )
		{
			switch(c)
			{
			case 'n':
				try {
					chessBoardSize = Integer.parseInt( getOpt.getOptarg() );
				}
				catch( final NumberFormatException e ) {
					System.err.println( "Usage: " + NQueensProblem.class.getSimpleName() + " [-n <size of the chessboard>] [-s]" );
					System.exit( 1 );
				}
				break;

			case 's':
				printSolution = false;
				break;

			case '?':
			default:
				System.err.println( "Usage: " + NQueensProblem.class.getSimpleName() + " [-n <size of the chessboard>] [-s]" );
				System.exit( 1 );
			}

			c = getOpt.getopt();
		}

		new NQueensProblem( chessBoardSize, printSolution );
	}
}
