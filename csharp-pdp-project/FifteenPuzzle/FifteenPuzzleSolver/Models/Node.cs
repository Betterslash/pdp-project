using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FifteenPuzzleSolver.Models
{
    public class Node : IComparable<Node>
    {

        private Node(Puzzle puzzle, int level, int fValue)
        {
            Value = puzzle;
            Level = level;
            FValue = fValue;
        }

        public Puzzle Value { get; }
        public int FValue { get; set; }
        public int Level { get; }


        public async Task<Node[]> GenerateChildrenAsync()
        {
            var moves = new[]
            {
                new[]{1, 0}, 
                new[]{0, 1},
                new[]{-1, 0},
                new[]{0, -1}
            };
            var threads = moves
                .Select(e => new Task<Node>(() => Shuffle(e)))
                .ToArray();
            foreach (var thread in threads)
            {
                thread.Start();
            }
            return await Task.WhenAll(threads);
        }
        
        public IEnumerable<Node> GenerateChildren()
        {
            var moves = new[]
            {
                new[]{1, 0}, 
                new[]{0, 1},
                new[]{-1, 0},
                new[]{0, -1}
            };
            return moves
                .Select(Shuffle);
        }

        private Node Shuffle(IReadOnlyList<int> move)
        {
            var line = Value.EmptyPosition[0] + move[0];
            var column = Value.EmptyPosition[1] + move[1];
            if (line is >= Constants.PuzzleSize or < 0) return null;
            if (column is >= Constants.PuzzleSize or < 0) return null;
            var puzzleCopy = Value.Representation.Select(a => a.ToArray()).ToArray();
            var aux = puzzleCopy[line][column];
            puzzleCopy[line][column] = -1;
            puzzleCopy[Value.EmptyPosition[0]][Value.EmptyPosition[1]] = aux;
            var puzzle = Puzzle.CreateInstance(puzzleCopy, new[] { line, column });
            return CreateInstance(puzzle, Level + 1, 0);
        }

        public static Node CreateInstance(Puzzle puzzle, int level, int fValue)
        {
            return new Node(puzzle, level, fValue);
        }
        
        public bool IsDifferent(Node node){
            var isDifferent = false;
            for (var i = 0; i < Constants.PuzzleSize; i++) {
                for (var j = 0; j < Constants.PuzzleSize; j++)
                {
                    if (node.Value.Representation[i][j] == Value.Representation[i][j]) continue;
                    isDifferent = true;
                    break;
                }
            }
            return isDifferent;
        }

        public int CompareTo(Node other)
        {
            return FValue - other.FValue;
        }
    }
}