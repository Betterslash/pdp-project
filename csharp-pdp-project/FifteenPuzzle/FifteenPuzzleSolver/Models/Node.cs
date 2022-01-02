using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using FifteenPuzzleSolver.Utils;

namespace FifteenPuzzleSolver.Models
{
    [Serializable]
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
            var threads = Constants.Moves
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
            return Constants.Moves
                .Select(Shuffle);
        }

        private Node Shuffle(IReadOnlyList<int> move)
        {
            var line = Value.EmptyPosition[0] + move[0];
            var column = Value.EmptyPosition[1] + move[1];
            if (line >= Constants.PuzzleSize || line < 0) return null;
            if (column >= Constants.PuzzleSize || column < 0) return null;
            var puzzleCopy = Value.Representation.Select(a => a.ToArray()).ToArray();
            var aux = puzzleCopy[line][column];
            puzzleCopy[line][column] = -1;
            puzzleCopy[Value.EmptyPosition[0]][Value.EmptyPosition[1]] = aux;
            var puzzle = Puzzle.CreateInstance(puzzleCopy, new[] { line, column });
            return CreateInstance(puzzle, Level + 1, 0);
        }

        private async Task<Node> ShuffleAsync(IReadOnlyList<int> move)
        {
            return await new Task<Node>(() => Shuffle(move));
        }

        public async Task<Node[]> GenerateChildrenAsyncWithAsyncShuffle()
        {
            var tasks = new List<Task<Node>>();
            tasks.AddRange(Constants.Moves.ToList().Select(ShuffleAsync));
            return await Task.WhenAll(tasks.ToList());
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