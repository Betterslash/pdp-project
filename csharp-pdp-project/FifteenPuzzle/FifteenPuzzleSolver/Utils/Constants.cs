using System;
using System.IO;
using Microsoft.Extensions.Configuration;

namespace FifteenPuzzleSolver.Utils
{
    public static class Constants
    {
        private static readonly IConfiguration Config = InstantiateConfig();
        public static readonly int PuzzleSize = Convert.ToInt32(Config.GetSection(nameof(PuzzleSize)).Value);
        public static readonly string RunType = Config.GetSection(nameof(RunType)).Value;

        public static readonly int[][] Moves = {
            new[]{1, 0}, 
            new[]{0, 1},
            new[]{-1, 0},
            new[]{0, -1}
        };

        private static IConfigurationRoot InstantiateConfig()
        {
            var builder = new ConfigurationBuilder()
                .SetBasePath(Directory.GetCurrentDirectory())
                .AddJsonFile("appsettings.json", false, true);
            return builder.Build();
        }
    }
}