package com.github.sbugat.problems.chess;

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

	public NQueensProblem( final int chessboardSizeArg ) throws IOException {

		chessboardSize = chessboardSizeArg;

		chessboard = new boolean[ chessboardSizeArg ][ chessboardSizeArg ];
		usedColumns = new boolean[ chessboardSizeArg ];
		Arrays.fill( usedColumns, true );
		usedAscendingDiagonals = new boolean[ chessboardSizeArg * 2 - 1 ];
		Arrays.fill( usedAscendingDiagonals, true );
		usedDescendingDiagonals = new boolean[ chessboardSizeArg * 2 - 1 ];
		Arrays.fill( usedDescendingDiagonals, true );

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
			if( usedColumns[x] ) {

				final int diag1 = x + y;
				final int diag2 = x + chessboardSize - 1 - y ;

				//if both diagonals are not already blocked by anothers queens
				if( usedAscendingDiagonals[ diag1 ] && usedDescendingDiagonals[ diag2 ] ) {

					chessboard[ y ][ x ] = true;
					usedColumns[x] = false;
					usedAscendingDiagonals[ diag1 ] = false;
					usedDescendingDiagonals[ diag2 ] = false;

					//All queens are sets on the chessboard then a solution is found!
					if( y + 1 >= chessboardSize ) {
						solutionCount++;
						System.out.println( "\nsolution nunmber " + solutionCount );
						print();
					}
					else {
						//Go on to the next line
						solve( y + 1 );
					}

					usedDescendingDiagonals[ diag2 ] = true;
					usedAscendingDiagonals[ diag1 ] = true;
					usedColumns[ x ] = true;
					chessboard[ y ][ x ] = false;
				}
			}
		}
	}

	/**
	 * Print the current chessboard
	 */
	public void print() {

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

	/**
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main( final String args[] ) throws IOException {

		try {
			//If no argument, resolve the problem on a standard chessboard (8x8 board)
			if( 0 == args.length ) {
				new NQueensProblem( 8 );
			}
			//
			else if( 1 == args.length && Integer.parseInt( args[ 0 ] ) > 0 ){
				new NQueensProblem( Integer.parseInt( args[ 0 ] ) );
			}
			else {
				System.err.println( "Usage: " + NQueensProblem.class.toString() + " [<size of the chessboard>]" );
			}
		}
		catch( final NumberFormatException e ) {
			System.err.println( "Usage: " + NQueensProblem.class.toString() + " [<size of the chessboard>]" );
		}
	}
}
