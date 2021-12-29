package model;

import lombok.Builder;
import lombok.Data;
import util.PuzzleChecker;
import util.PuzzleType;

import java.util.ArrayList;
import java.util.Random;

@Data
@Builder
public class Puzzle {
    private int[][] representation;
    private int[] emptyPosition;
    private int size;

    public static Puzzle getStartPuzzle(PuzzleType puzzleType) {
        switch (puzzleType){
            case DEFAULT ->  {
                var startPuzzle = new int[][] {
                        {2,  4,  8,  12},
                        {1, 7,  3,  14},
                        {-1,  6, 15, 11},
                        {5,  9,  13, 10},
                };
                var emptyPosition = new int[] {2, 0};
                var size = 4;
                return Puzzle.builder()
                        .size(size)
                        .emptyPosition(emptyPosition)
                        .representation(startPuzzle)
                        .build();
            }
            case GENERATED -> {
                var result = generateRandomPuzzle();
                while (!PuzzleChecker.isSolvable(result)){
                    result = generateRandomPuzzle();
                }
                return result;
            }
            default -> throw new RuntimeException();
        }
    }

    private static Puzzle generateRandomPuzzle(){
        var representationSize = (int) Math.round(Math.sqrt(15));
        var representation = new int[representationSize][representationSize];
        var random = new Random();

        var positionsArray = new ArrayList<int[]>();
        while (positionsArray.size() < 15) {
            var line = random.nextInt(representationSize);
            var column = random.nextInt(representationSize);
            if (positionsArray.stream().filter(e -> e[0] == line && e[1] == column).toList().size() == 0) {
                positionsArray.add(new int[]{line, column});
            }
        }
        for (int i = 0; i < 15; i++) {
            var currentPosition = positionsArray.get(i);
            representation[currentPosition[0]][currentPosition[1]] = i + 1;
        }
        var currentEmpty = new int[]{-1, -1};
        for (int i = 0; i < representationSize; i++) {
            for (int j = 0; j < representationSize; j++) {
                if (representation[i][j] == 0) {
                    currentEmpty[0] = i;
                    currentEmpty[1] = j;
                }
            }
        }
        representation[currentEmpty[0]][currentEmpty[1]] = -1;
        return Puzzle.builder()
                .representation(representation)
                .emptyPosition(currentEmpty)
                .size(representationSize)
                .build();
    }

    public static Puzzle getGoalPuzzle(){
        var goalMatrix = new int[][] {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, -1},
        };
        var emptyPosition = new int[] {3, 3};
        var size = 4;
        return Puzzle.builder()
                .size(size)
                .emptyPosition(emptyPosition)
                .representation(goalMatrix)
                .build();
    }
}
