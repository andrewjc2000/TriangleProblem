# Brute-Force Algorithm to crack Cracker Barrel's Triangular Peg Puzzle

This is a program which treats the described Peg Puzzle as a graph, where each node represents a slot on the puzzle board.

Each Node is of type Marker, which can take on 2 states, filled and empty, and has an integer ID based on its position on the board.

The program computes all possibilities by considering all of the valid moves at a given board state, and then exploring each of those
moves recursively, with each recursive call finding yet again the set of valid moves, until there are no moves left.  The moves are
added to a list that is passed down the recursion chain.  If the list of moves results in a finished puzzle, it is noted, and
one can choose to exit the program at that point.  Regardless, afterwards the move is undone and removed from the list, and the program moves back up the recursion chain to explore other possible moves.

Note: it is helpful to exit the program as soon as one solution is found, especially if this program is to be optimized for higher row numbers.  There are no solutions to any peg boards containing 0-4 rows, but a peg board with 5 rows has 29,760 different valid solutions
of its possible 1,293,180 ending states (about 2% of all games result in a peg board with only 1 peg left).  I have not yet explored higher row numbers in much detail, though at least one solution exists when the program is run with 6 rows.
