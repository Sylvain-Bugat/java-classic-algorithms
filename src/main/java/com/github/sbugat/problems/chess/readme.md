# N Queens on a chess
***
Java programs to count and/or print solutions of the [N Queens puzzle on a NxN chess](http://en.wikipedia.org/wiki/Eight_queens_puzzle)

## Keys to solving this puzzle

Contraint programming is very efficient with this algorithm because lots of possibilities don't have to be tested.

### One Queen per line

Only one queen can be placed on each line so if a queen is placed, the algorithm must go on to the next line:

| L\C | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
| ------------- | ----------- | ------------- | ----------- | ------------- | ----------- | ------------- | ----------- | ----------- |
| 1 | :red_circle: | :red_circle: | :red_circle: | :black_large_square: | :red_circle: | :red_circle: | :red_circle: | :red_circle: |
| 2 |  |  | :red_circle: | :red_circle: | :red_circle: |  |  |  |
| 3 |  | :red_circle: |  | :red_circle: |  | :red_circle: |  |  |
| 4 | :red_circle: |  |  | :red_circle: |  |  | :red_circle: |  |
| 5 |  |  |  | :red_circle: |  |  |  | :red_circle: |
| 6 |  |  |  | :red_circle: |  |  |  |  |
| 7 |  |  |  | :red_circle: |  |  |  |  |
| 8 |  |  |  | :red_circle: |  |  |  |  |

### One Queen per column

With the previous placed Queen an array can be defined to mark already blocked column:

| 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
| ------------- | ----------- | ------------- | ----------- | ------------- | ----------- | ------------- | ----------- |
| :white_circle: | :white_circle: | :white_circle: | :red_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: |

### One Queen per diagonals

With the previous placed Queen 2 arrays can be defined to mark already blocked diagonals:

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 |
| ------------- | ----------- | ------------- | ----------- | ------------- | ----------- | ------------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- |
| :white_circle: | :white_circle: | :white_circle: | :red_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: |

With x and y going from 0 to 7, this code can be used:
```java
ascendingDiagnonal = x + y;
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 |
| ------------- | ----------- | ------------- | ----------- | ------------- | ----------- | ------------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- |
| :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :red_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: |

With x and y going from 0 to 7, this code can be used:
```java
descendingDiagnonal = x + 8 - 1 - y;
```

## with reccursive back-tracking algorithm
| Complexity::white_check_mark: | Efficiency::warning::warning: |
| ---------- | ---------- |
**The best trade between complexity and efficiency algorithm**
Useds positions and contraints are stored in the stack and in global arrays.

Core algorithm:
```java
	private void solve(final int y) {

		// Test all position on the line
		for (int x = 0; x < chessboardSize; x++) {

			// if the row is not already blocked by another queen
			if (unusedColumns[x]) {

				final int ascDiagonal = x + y;
				final int descDiagonal = x + chessboardSize - 1 - y;

				// if both diagonals are not already blocked by anothers queens
				if (unusedAscendingDiagonals[ascDiagonal] && unusedDescendingDiagonals[descDiagonal]) {

					// Add contraints for this a queen
					unusedColumns[x] = false;
					unusedAscendingDiagonals[ascDiagonal] = false;
					unusedDescendingDiagonals[descDiagonal] = false;

					// All queens are sets on the chessboard then a solution is found!
					if (y >= chessboardSizeMinusOne) {
						solutionCount++;
					}
					else {
						// Go on to the next line
						solve(y + 1);
					}

					// Unadd contraints for this a queen (back-tracking)
					unusedDescendingDiagonals[descDiagonal] = true;
					unusedAscendingDiagonals[ascDiagonal] = true;
					unusedColumns[x] = true;
				}
			}
		}
	}
```
This place a queen only if it can be done.

### possible optimisations
| Complexity | Efficiency improvements | description
| ---------- | ---------- | ---------- |
| Very easy | poor | the size of the chessboard minus one can be precalculated |
| Easy | average | only count found solutions if there is no need to print them, it improves speed if solutions are printed on the terminal |
| Average | very important | only half of the possibilities can be scanned by mirroring found solutions |

Optmized and complete algorithm can be found in this file: [NQueensProblemCountRecursive.java](NQueensProblemCountRecursive.java)

