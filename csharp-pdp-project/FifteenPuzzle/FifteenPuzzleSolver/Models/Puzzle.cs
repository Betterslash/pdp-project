using System;

namespace FifteenPuzzleSolver.Models
{
    [Serializable]
    public class Puzzle
    {
        private Puzzle(int[][] puzzleRepresentation, int[] emptyPosition)
        {
            Representation = puzzleRepresentation;
            EmptyPosition = emptyPosition;
        }

        internal static Puzzle CreateInstance(int[][] puzzleRepresentation, int[] emptyPosition)
        {
            return new Puzzle(puzzleRepresentation, emptyPosition);
        }

        public int[][] Representation { get; }
        public int[] EmptyPosition { get; }

        public static Puzzle GetStartInstance()
        {
            var puzzleRepresentation = new[]
            {
                new[]{2,  4,  8,  12},
                new[]{1, 7,  3,  14},
                new[]{-1,  6, 15, 11},
                new[]{5,  9,  13, 10},
            };
            var emptyPosition = new[] { 2, 0 };
            return CreateInstance(puzzleRepresentation, emptyPosition);
        }

        public static Puzzle GetGoalInstance()
        {
            var goalPuzzle = new[]
            {
                new[]{1, 2, 3, 4},
                new[]{5, 6, 7, 8},
                new[]{9, 10, 11, 12},
                new[]{13, 14, 15, -1}
            };
            var emptyPosition = new[]{ -3, -3 };
            return CreateInstance(goalPuzzle, emptyPosition);
        }
    }
}