# 15-Puzzle-Problem

##### Statement :
--
        Given a 4x4 Puzzle with numbers from 1 to 15 and an empty space, find the best solution for solving it by moving tiles from its composition to an empty designated place until all its tiles are orderd ascending and the empty tile is in the down-right corner.

##### Puzzle Example :
--
    ![alt text](https://upload.wikimedia.org/wikipedia/commons/f/ff/15-puzzle_magical.svg)
    
##### Solving Method :
--
    We represent the Puzzle as a 4x4 matrix and we denote the empty position as -1,
    then for every kind of move type :
        1.  UP      -> (1, 0)
        2.  DOWN    -> (-1, 0)
        3.  RIGHT   -> (0, 1)
        4.  LEFT    -> (0, 1)
    We generate a new matrix as a possible Node in a solving Tree.
    Using A* we determine the best path by applying Heuristic functions for denoting the rewarding value of a newly discovered path and for always choosing the best approach.

#### How we implemented threading/MPI :
    We grab the first 4 possible ways of solving the puzzle and for each of them we create a thread/worker and a Main/Master which will denote the first one to finish and stop and print the path which took the least amount of resources.

