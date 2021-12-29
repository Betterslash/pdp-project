using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using FifteenPuzzleSolver.Models;
using FifteenPuzzleSolver.Services;

namespace FifteenPuzzleSolver
{
    internal static class Program
    {
        private static async Task Main(string[] args)
        {
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
            (await Task.WhenAny(tasks)).Result.ToList()
                .ForEach(Solver.PrettyPrint);
        }
    }
}