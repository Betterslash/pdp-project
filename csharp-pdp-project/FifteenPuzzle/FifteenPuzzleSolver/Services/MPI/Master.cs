using System;
using System.Collections.Generic;
using System.Linq;
using FifteenPuzzleSolver.Models;
using MPI;

namespace FifteenPuzzleSolver.Services.MPI
{
    public static class Master
    {
        public static void Start()
        {
            var dateTime = new DateTime();
            var worldSize = Communicator.world.Size;
            var root = Node.CreateInstance(Solver.StartPuzzle, 0, 0);
            var children = root.GenerateChildren()
                .Where(e => e != null);
            var index = 1;
            foreach (var child in children)
            {
                Console.WriteLine($"Sent {child} to worker with id {index} ...");
                Communicator.world.Send(child, index, 0);
                index++;
            }

            var result = Communicator.world.Receive<List<Node>>(Communicator.anySource, 0);
            var time = (DateTime.Now - dateTime).Milliseconds;
            Console.WriteLine("MPI Execution Finished: " + result + "\n" + "TIME: " + time + " milliseconds");
            result.ForEach(Solver.PrettyPrint);
        }
    }
}