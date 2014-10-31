# N Queens on a chess
***
Java programs to count and/or print solutions of the [N Queens puzzle on a NxN chess](http://en.wikipedia.org/wiki/Eight_queens_puzzle)

## Keys

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

| 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 |
| ------------- | ----------- | ------------- | ----------- | ------------- | ----------- | ------------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- |
| :white_circle: | :white_circle: | :white_circle: | :red_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: |

With x and y going from 0 to 7, this code can be used:
```java
descendingDiagnonal = x + y;
```

| 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 |
| ------------- | ----------- | ------------- | ----------- | ------------- | ----------- | ------------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- |
| :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: | :red_circle: | :white_circle: | :white_circle: | :white_circle: | :white_circle: |

With x and y going from 0 to 7, this code can be used:
```java
ascendingDiagnonal = x + 8 - 1 - y;
```

## with reccursive back-tracking algorithm
| Complexity::white_check_mark: | Efficiency::warning: |
| ---------- | ---------- |
**The best complexity/efficiency method**:
```java

```

