using System;
using System.Collections.Generic;
using System.Linq;
using FifteenPuzzleSolver.Models;
using MPI;

namespace FifteenPuzzleSolver.Services.MPI
{
    public static class Master
    {
        public static void StartWorkers()
        {
            var dateTime = new DateTime();
            var worldSize = Communicator.world.Size;
            var root = Node.CreateInstance(Solver.StartPuzzle, 0, 0);
            var children = root.GenerateChildren();
            var index = 1;
            foreach (var child in children)
            {
                Console.WriteLine($"Sent {child} to worker with id {index} ...");
                Communicator.world.Send(child, index, 0);
                index++;
            }

            var results = new List<List<Node>>();
            for (var i = 1; i < worldSize; i++)
                results[i - 1] = Communicator.world.Receive<List<Node>>(i, 0);
            var time = (DateTime.Now - dateTime).Milliseconds;
            Console.WriteLine("MPI Execution Finished: " + GetBestResult(results) + "\n" + "TIME: " + time + " milliseconds");
        }

        private static List<Node> GetBestResult(List<List<Node>> results)
        {
            var min = long.MaxValue;
            var position = -1;
            var index = 0;
            results.ForEach(e => {
                if (e.Count < min)
                {
                    min = e.Count;
                    position = index;
                }
                index++;
            });
            return results.ElementAt(position);
        }
    }
}