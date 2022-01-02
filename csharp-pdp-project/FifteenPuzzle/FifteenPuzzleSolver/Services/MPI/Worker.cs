using System;
using FifteenPuzzleSolver.Models;
using MPI;

namespace FifteenPuzzleSolver.Services.MPI
{
    public static class Worker
    {
        public static void Execute()
        {
            var solver = Solver.CreateInstance();
            var resource = Communicator.world.Receive<Node>(0, 0);
            Console.WriteLine($"Worker with id received ${resource}");
            var result = solver.Resolve(resource);
            Communicator.world.Send(result, 0, 0);
            Console.WriteLine($"Worker responded to master with {result}");
        }
    }
}