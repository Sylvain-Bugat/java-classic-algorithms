# N Queens on a chess
***
Java programs to count and/or print solutions of the [N Queens puzzle on a NxN chess](http://en.wikipedia.org/wiki/Eight_queens_puzzle)

## Keys

Contraint programming is very efficient with this algorithm because lots of possibilities don't have to best tested.

### One Queen per line

Only one queen can be placed on on line (or column) so if a queen is placed, the algorithm must go on to the next line:
| 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
| - | - | - | - | - | - | - | - |
| :red_circle: | :red_circle: | :red_circle: | :black_large_square: | :red_circle: | :red_circle: | :red_circle: | :red_circle: |
|  |  | :red_circle: | :red_circle: | :red_circle: |  |  |  |
|  | :red_circle: |  | :red_circle: |  | :red_circle: |  |  |
| :red_circle: |  |  | :red_circle: |  |  | :red_circle: |  |
|  |  |  | :red_circle: |  |  |  | :red_circle: |
|  |  |  | :red_circle: |  |  |  |  |
|  |  |  | :red_circle: |  |  |  |  |
|  |  |  | :red_circle: |  |  |  |  |

## with reccursive back-tracking algorithm
| Complexity::white_check_mark: | Efficiency::warning: |
| ---------- | ---------- |
**The best complexity/efficiency method**:
```java

```

