using System;
using System.Collections.Generic;
using System.Linq;
using FifteenPuzzleSolver.Models;
using FifteenPuzzleSolver.Utils;

namespace FifteenPuzzleSolver.Services
{
    public class Solver
    {
        private Solver(List<Node> open, List<Node> closed)
        {
            Open = open ?? throw new ArgumentNullException(nameof(open));
            Closed = closed ?? throw new ArgumentNullException(nameof(closed));
        }

        internal static Solver CreateInstance()
        {
            return new Solver(new List<Node>(), new List<Node>());
        }

        private List<Node> Open { get; set; }
        private List<Node> Closed { get; }
        
        public static readonly Puzzle StartPuzzle = Puzzle.GetStartInstance();
        private static readonly Puzzle GoalPuzzle = Puzzle.GetGoalInstance();

        private static int ComputeF(Node start)
        {
            return ComputeH(start.Value) + start.Level;
        }

        private static int ComputeH(Puzzle startValue)
        {
            var temporary = 0;
            for (var i = 0; i < Constants.PuzzleSize; i++)
            {
                for (var j = 0; j < Constants.PuzzleSize; j++)
                {
                    if (startValue.Representation[i][j] == GoalPuzzle.Representation[i][j]) continue;
                    if (startValue.Representation[i][j] != -1)
                    {
                        temporary += 1;
                    }
                }
            }

            return temporary;
        }

        public  IEnumerable<Node> Resolve(Node root)
        {
            root.FValue = ComputeF(root);
            Open.Add(root);
            var current = root;
            var result = new List<Node>();
            while (true)
            {
                result.Add(current);
                if (ComputeH(current.Value) == 0)
                {
                    break;
                }

                var generatedChildren =  current.GenerateChildren()
                    .Where(e => e != null)
                    .Where(e => !IsInClosed(e))
                    .ToList();
                generatedChildren
                    .ToList()
                    .ForEach(child =>
                {
                    child.FValue = ComputeF(child);
                    Open.Add(child);
                });
                Closed.Add(current);
                Open.Sort();
                if (generatedChildren.Count > 0)
                {
                    
                    generatedChildren.Sort();
                    current = generatedChildren.First();
                    Open = Open.Where(e => e.IsDifferent(current)).ToList();
                }
                else
                {
                    Open.RemoveAt(0);
                    var possibilities = Open.Where(e => e.Level == current.Level - 1)
                        .Where(q => !IsInClosed(q))
                        .ToList();
                    possibilities.Sort();
                    current = possibilities.First();
                }
            }
            return result;
        }

        private bool IsInClosed(Node node)
        {
            var result = false;
            Closed.ForEach(e =>
            {
                if (!e.IsDifferent(node))
                {
                    result = true;
                }
            });
            return result;
        }

        public static void PrettyPrint(Node current){
            Console.WriteLine();
            Console.WriteLine("  | ");
            Console.WriteLine("  | ");
            Console.WriteLine(" \\'/ \n");
            foreach (var i in
            current.Value.Representation) {
                foreach (var j in i) {
                    Console.Write(j + " ");
                }
                Console.WriteLine();
            }
        }
    }
}