using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using FifteenPuzzleSolver.Models;
using FifteenPuzzleSolver.Services;
using FifteenPuzzleSolver.Services.MPI;
using FifteenPuzzleSolver.Utils;
using MPI;
using Environment = MPI.Environment;

namespace FifteenPuzzleSolver
{
    internal static class Program
    {
        public static async Task Main(string[] args)
        {
            //MpiRun(args);
            Console.WriteLine($"Puzzle size is {Constants.PuzzleSize}");
            Console.WriteLine($"Puzzle solving will be done using {Constants.RunType}");
            await ThreadedRun(args);
        }
        
        private static async Task ThreadedRun(string[] args)
        {
            var start = DateTime.Now;
            var root = Node.CreateInstance(Solver.StartPuzzle, 0, 0);
            var children = await root.GenerateChildrenAsync();
            var tasks = new List<Task<IEnumerable<Node>>>();
            children.Where(e => e != null)
                .ToList()
                .ForEach(e =>
                {
                    tasks.Add(new Task<IEnumerable<Node>>(() => Solver.CreateInstance().Resolve(e)));
                });
            tasks.ForEach(e => e.Start());
            var result = await Task.WhenAny(tasks).Result;
            var end = (DateTime.Now - start).Milliseconds;
            /*result.ToList()
                .ForEach(Solver.PrettyPrint);*/
            Console.WriteLine($"Thread implementation took {end} milliseconds ...");
        }

        private static void MpiRun(string[] args)
        {
            using (new Environment(ref args))
            {
                if (Communicator.world.Rank == 0)
                {
                    Console.WriteLine("Maser will start ...");
                    Master.StartWorkers();
                }
                else
                {
                    Console.WriteLine("Worker will start ...");
                    Worker.Execute();
                }
            }
        }
    }
}