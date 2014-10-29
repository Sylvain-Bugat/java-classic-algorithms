package com.github.sbugat.problems.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gnu.getopt.Getopt;

/**
 * Classic N chess queens on a size N chess board with multithreaded bits flags
 *
 * Time on Intel Q6600 CPU with 4 threads:
 * 8       9         10       11       12       13       14       15        16
 * 0m0.105s 0m0.105s 0m0.130s 0m0.136s 0m0.135s 0m0.134s 0m0.193s 0m0.645s 0m3.657s
 *
 * @author Sylvain Bugat
 *
 */
public class NQueensProblemCountMultiThreaded implements Runnable{

	/**Number of solution counter*/
	private long solutionCount;

	/**Size of the chess board*/
	private final int chessboardSize;
	private final int chessboardSizeMinusTwo;

	private final boolean printSolutions = false;

	private final int bitFlags;
	private final int [] bitFlagsStack;
	//private final int x;
	private int bitFlagsMask;
	private final int [] unusedColumnsStack;
	private final int [] unusedAscendingDiagonalsStack;
	private final int [] unusedDescendingDiagonalsStack;
	private int stacklevel=1;

	public NQueensProblemCountMultiThreaded( final int chessboardSizeArg, final int bitFlagsArg, final int [] bitFlagsStackArg, final int [] unusedColumnsStackArg, final int [] unusedAscendingDiagonalsStackArg, final int [] unusedDescendingDiagonalsStackArg ) {

		chessboardSize = chessboardSizeArg;
		chessboardSizeMinusTwo = chessboardSizeArg - 2;
		bitFlags = bitFlagsArg;

		bitFlagsStack = Arrays.copyOf( bitFlagsStackArg, bitFlagsStackArg.length );
		unusedColumnsStack = Arrays.copyOf( unusedColumnsStackArg, unusedColumnsStackArg.length );
		unusedAscendingDiagonalsStack = Arrays.copyOf( unusedAscendingDiagonalsStackArg, unusedAscendingDiagonalsStackArg.length );
		unusedDescendingDiagonalsStack = Arrays.copyOf( unusedDescendingDiagonalsStackArg, unusedDescendingDiagonalsStackArg.length );

		for(int i=0 ; i < chessboardSizeArg ; i++ ) {
			bitFlagsMask |= 1 << i;
		}
	}

	/**
	 * Find all solution with a fixed queen on the first line
	 */
	public void run() {

		solve( bitFlags );
	}

	/**
	 * Solving with iterative/stacking method by using bit flags, do a depth-first/back-tracking algorithm
	 * a queen must me placed on the first line
	 *
	 * @param initial bitFlag with a single queen on the first line
	 */
	private void solve( int bitFlags ) {

		int prevStacklevel = stacklevel - 1;
		int targetQueen;
		//Infinite loop, exit condition is tested when unstacking a queen
		while( true ) {

			//Test first possible queen of the line using direct inlining(manual code copy) of this method call: Integer.lowestOneBit( bitFlags );
			//if the row is not already blocked by another queen and if both diagonals are not already blocked by anothers queens
			//Don't need to test if targetQueen is not 0 because bitFlags has not been unstacked at the end of the loop (=contain at least one 0)
			targetQueen = -( bitFlags ) & ( bitFlags );

			//All queens are sets on the chessboard then a solution is found!
			//Test with the board size minus 2 because the targeted queen is not placed yet
			if( stacklevel >= chessboardSizeMinusTwo ) {
				solutionCount++;

				//Uncomment to print all solutions
				/*if( printSolutions ) {
					print( targetQueen );
				}*/

				bitFlags ^= targetQueen;
			}
			else {

				//Go on to the next line
				prevStacklevel = stacklevel++;
				//Mark the current target queen as tested for this stack level
				bitFlagsStack[ stacklevel ] = bitFlags ^ targetQueen;

				//unusedColumnsStack[ stacklevel ] = unusedColumnsStack[ prevStacklevel ] | targetQueen;
				//unusedAscendingDiagonalsStack[ stacklevel ] = ( unusedAscendingDiagonalsStack[ prevStacklevel ] | targetQueen ) << 1;
				//unusedDescendingDiagonalsStack[ stacklevel ] = ( unusedDescendingDiagonalsStack[ prevStacklevel ] | targetQueen ) >> 1;
				//bitFlags = bitFlagsMask & ( unusedColumnsStack[ stacklevel ] | unusedAscendingDiagonalsStack[ stacklevel ] | unusedDescendingDiagonalsStack[ stacklevel ] );

				//Update bit flags and do 3 stacks updates (4 previous commented lines in 1)
				bitFlags = bitFlagsMask & ~( ( unusedColumnsStack[ stacklevel ] = unusedColumnsStack[ prevStacklevel ] | targetQueen )
						| ( unusedAscendingDiagonalsStack[ stacklevel ] = ( unusedAscendingDiagonalsStack[ prevStacklevel ] | targetQueen ) << 1 )
						| ( unusedDescendingDiagonalsStack[ stacklevel ] = ( unusedDescendingDiagonalsStack[ prevStacklevel ] | targetQueen ) >> 1 )
						);
			}

			//If all positions have been tested or are already blocked by a column or a diagonal
			while( bitFlags == 0 ) {

				//If there is still something to unstack
				if( stacklevel > 1 ) {
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
	 * Print a solution and mirror version
	 *
	 * @param targetQueen
	 */
	private void print( final int targetQueen ) {

		System.out.println( "\nSolution number " + solutionCount + " and " + solutionCount + "bis :" );

		String line = String.format( "%" + chessboardSize + "s", Integer.toBinaryString( unusedColumnsStack[ 0 ] ) ).replace( ' ', '0' );
		System.out.println( line + "    " + StringUtils.reverse( line ) );

		for( int i=1 ; i <= chessboardSizeMinusTwo ; i++ ) {

			line = String.format( "%" + chessboardSize + "s", Integer.toBinaryString( unusedColumnsStack[i] - unusedColumnsStack[i - 1 ] ) ).replace( ' ', '0' );
			System.out.println( line + "    " + StringUtils.reverse( line ) );
		}

		line = String.format( "%" + chessboardSize + "s", Integer.toBinaryString( targetQueen ) ).replace( ' ', '0' );
		System.out.println( line + "    " + StringUtils.reverse( line ) );
	}


	private static void initSolve( final List<NQueensProblemCountMultiThreaded> threadList, final int chessboardSize) {

		final int [] bitFlagsStack = new int[ chessboardSize ];
		int bitFlagsMask = 0;
		final int [] unusedColumnsStack = new int[ chessboardSize ];;
		final int [] unusedAscendingDiagonalsStack = new int[ chessboardSize ];
		final int [] unusedDescendingDiagonalsStack = new int[ chessboardSize ];

		for(int i=0 ; i < chessboardSize ; i++ ) {
			bitFlagsMask |= 1 << i;
		}

		//Test half square of the line
		for( int x=0 ; x < chessboardSize/2 ; x ++ ){

			unusedColumnsStack[ 0 ] = 1 << x;
			unusedAscendingDiagonalsStack[ 0 ] = ( 1 << x ) << 1 ;
			unusedDescendingDiagonalsStack[ 0 ] = ( 1 << x ) >> 1;
			final int bitFlags = bitFlagsMask & ~( unusedColumnsStack[ 0 ] | unusedAscendingDiagonalsStack[ 0 ] | unusedDescendingDiagonalsStack[ 0 ] );
			bitFlagsStack[ 0 ] = bitFlags;

			//Go on to the second line
			initSolve( threadList, bitFlags, 0, chessboardSize - 2, bitFlagsMask, bitFlagsStack, unusedColumnsStack, unusedAscendingDiagonalsStack, unusedDescendingDiagonalsStack );
		}

		//If the cheesboard size is odd, test with a queen on the middle of the first line
		if( 0 != chessboardSize % 2 ) {

			final int x=chessboardSize/2;

			unusedColumnsStack[ 0 ] = 1 << x;
			unusedAscendingDiagonalsStack[ 0 ] = ( 1 << x ) << 1;
			unusedDescendingDiagonalsStack[ 0 ] = ( 1 << x ) >> 1;

			//just test next line half of possible position because or mirroring
			int bitFlags = 0; //bitFlagsMask & ~( unusedColumnsStack[ 0 ] | unusedAscendingDiagonalsStack[ 0 ] | unusedDescendingDiagonalsStack[ 0 ] );
			for(int i=0 ; i < x - 1 ; i++ ) {
				bitFlags ^= 1 << i;
			}
			bitFlagsStack[ 0 ] = bitFlags;

			//Go on to the second line
			initSolve( threadList, bitFlags, 0, chessboardSize - 2, bitFlagsMask, bitFlagsStack, unusedColumnsStack, unusedAscendingDiagonalsStack, unusedDescendingDiagonalsStack );
		}
	}

	/**
	 * Solving with iterative/stacking method by using bit flags, do a depth-first/back-tracking algorithm
	 * a queen must me placed on the first line
	 *
	 * @param initial bitFlag with a single queen on the first line
	 */
	private static void initSolve( final List<NQueensProblemCountMultiThreaded> threadList, int bitFlags, int stacklevel, final int chessboardSizeMinusTwo, final int bitFlagsMask, final int [] bitFlagsStack, final int [] unusedColumnsStack, final int [] unusedAscendingDiagonalsStack, final int [] unusedDescendingDiagonalsStack ) {

		int prevStacklevel = stacklevel - 1;
		int targetQueen;
		//Infinite loop, exit condition is tested when unstacking a queen
		while( true ) {

			//Test first possible queen of the line using direct inlining(manual code copy) of this method call: Integer.lowestOneBit( bitFlags );
			//if the row is not already blocked by another queen and if both diagonals are not already blocked by anothers queens
			//Don't need to test if targetQueen is not 0 because bitFlags has not been unstacked at the end of the loop (=contain at least one 0)
			targetQueen = -( bitFlags ) & ( bitFlags );

			//All queens are sets on the chessboard then a solution is found!
			//Test with the board size minus 2 because the targeted queen is not placed yet
			if( stacklevel >= 0 ) {

				//Go on to the next line
				prevStacklevel = stacklevel++;
				//Mark the current target queen as tested for this stack level
				bitFlagsStack[ stacklevel ] = bitFlags ^ targetQueen;

				//unusedColumnsStack[ stacklevel ] = unusedColumnsStack[ prevStacklevel ] | targetQueen;
				//unusedAscendingDiagonalsStack[ stacklevel ] = ( unusedAscendingDiagonalsStack[ prevStacklevel ] | targetQueen ) << 1;
				//unusedDescendingDiagonalsStack[ stacklevel ] = ( unusedDescendingDiagonalsStack[ prevStacklevel ] | targetQueen ) >> 1;
				//bitFlags = bitFlagsMask & ( unusedColumnsStack[ stacklevel ] | unusedAscendingDiagonalsStack[ stacklevel ] | unusedDescendingDiagonalsStack[ stacklevel ] );

				//Update bit flags and do 3 stacks updates (4 previous commented lines in 1)
				int nextBitFlags = bitFlagsMask & ~( ( unusedColumnsStack[ stacklevel ] = unusedColumnsStack[ prevStacklevel ] | targetQueen )
						| ( unusedAscendingDiagonalsStack[ stacklevel ] = ( unusedAscendingDiagonalsStack[ prevStacklevel ] | targetQueen ) << 1 )
						| ( unusedDescendingDiagonalsStack[ stacklevel ] = ( unusedDescendingDiagonalsStack[ prevStacklevel ] | targetQueen ) >> 1 )
						);

				threadList.add( new NQueensProblemCountMultiThreaded( bitFlagsStack.length, nextBitFlags, bitFlagsStack, unusedColumnsStack, unusedAscendingDiagonalsStack, unusedDescendingDiagonalsStack ) );

				stacklevel--;

				bitFlags ^= targetQueen;
			}
			else {

				//Go on to the next line
				prevStacklevel = stacklevel++;
				//Mark the current target queen as tested for this stack level
				bitFlagsStack[ stacklevel ] = bitFlags ^ targetQueen;

				//unusedColumnsStack[ stacklevel ] = unusedColumnsStack[ prevStacklevel ] | targetQueen;
				//unusedAscendingDiagonalsStack[ stacklevel ] = ( unusedAscendingDiagonalsStack[ prevStacklevel ] | targetQueen ) << 1;
				//unusedDescendingDiagonalsStack[ stacklevel ] = ( unusedDescendingDiagonalsStack[ prevStacklevel ] | targetQueen ) >> 1;
				//bitFlags = bitFlagsMask & ( unusedColumnsStack[ stacklevel ] | unusedAscendingDiagonalsStack[ stacklevel ] | unusedDescendingDiagonalsStack[ stacklevel ] );

				//Update bit flags and do 3 stacks updates (4 previous commented lines in 1)
				bitFlags = bitFlagsMask & ~( ( unusedColumnsStack[ stacklevel ] = unusedColumnsStack[ prevStacklevel ] | targetQueen )
						| ( unusedAscendingDiagonalsStack[ stacklevel ] = ( unusedAscendingDiagonalsStack[ prevStacklevel ] | targetQueen ) << 1 )
						| ( unusedDescendingDiagonalsStack[ stacklevel ] = ( unusedDescendingDiagonalsStack[ prevStacklevel ] | targetQueen ) >> 1 )
						);
			}

			//If all positions have been tested or are already blocked by a column or a diagonal
			while( bitFlags == 0 ) {

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
	 * Prepare and run all thread with a fixed thread pool
	 *
	 * @param chessBoardSize size of the board
	 * @param printSolutions
	 * @param threadNumbers number of simultaneous thread to launch
	 */
	public static void launchMultiThread( final int chessBoardSize, final boolean printSolutions, final int threadNumber ) {

		//Prepare a thread for each possible possition on the first half of the first line
		initSolve( ThreadLauncher.threadList, chessBoardSize );

		final List<NQueensProblemCountMultiThreaded> threadList = new ArrayList<>( ThreadLauncher.threadList );

		final List<Thread> launchedThreadList = new ArrayList<>();
		System.out.println( ThreadLauncher.threadList.size() );
		for( int i=0 ; i< threadNumber ; i++ ) {

			final Thread thread = new Thread( new ThreadLauncher() );
			thread.start();

			launchedThreadList.add( thread );
		}

		for( final Thread thread : launchedThreadList ) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		int solutionCount = 0;
		for( final NQueensProblemCountMultiThreaded thread : threadList ) {

			solutionCount += thread.solutionCount;
		}

		//Multiply by 2 the solution count for the other half not calculated
		solutionCount *= 2;

		System.out.println( "Total number of solution(s):" + solutionCount );
	}

	/**
	 * Main program
	 *
	 * @param args
	 */
	public static void main( final String args[] ) {

		final String usage = "Usage: " + NQueensProblemCountMultiThreaded.class.getSimpleName() + " [-n <size of the chessboard>] [-p(print desactivated)] [-t <number of threads>]";

		final Getopt getOpt = new Getopt( NQueensProblemCountMultiThreaded.class.getSimpleName(), args, ":n:pt:" );
		getOpt.setOpterr( false );

		//Default chessboard size
		int chessBoardSize = 8;

		boolean printSolutions = false;

		int threadNumber = 2;

		int c = getOpt.getopt();
		while( -1 != c )
		{
			switch(c)
			{
			case 'n':
				try {
					chessBoardSize = Integer.parseInt( getOpt.getOptarg() );

					if( chessBoardSize < 2 || chessBoardSize > 31 ) {
						System.err.println( usage );
						System.exit( 1 );
					}
				}
				catch( final NumberFormatException e ) {
					System.err.println( usage );
					System.exit( 1 );
				}
				break;

			case 'p':
				printSolutions = true;
				break;

			case 't':
				try {
					threadNumber = Integer.parseInt( getOpt.getOptarg() );

					if( threadNumber < 1 ) {
						System.err.println( usage );
						System.exit( 1 );
					}
				}
				catch( final NumberFormatException e ) {
					System.err.println( usage );
					System.exit( 1 );
				}
				break;

			case '?':
			default:
				System.err.println( usage );
				System.exit( 1 );
			}

			c = getOpt.getopt();
		}

		launchMultiThread( chessBoardSize, printSolutions, threadNumber );
	}

	private static class ThreadLauncher implements Runnable {

		static final List<NQueensProblemCountMultiThreaded> threadList = new ArrayList<>();

		@Override
		public void run() {

			Runnable thread = getNextThread();
			while( null != thread) {

				final Thread launchedThread = new Thread( thread );
				launchedThread.start();

				try {
					launchedThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				thread = getNextThread();
			}
		}

		public static synchronized Runnable getNextThread() {

			if( ! threadList.isEmpty() ) {
				final Runnable nextThread = threadList.get( 0 );

				threadList.remove( 0 );
				return nextThread;
			}

			return null;
		}
	}
}
