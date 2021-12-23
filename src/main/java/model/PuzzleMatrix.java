package model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Random;

@Data
@Builder
public class PuzzleMatrix {
    private final int[][] representation;
    private int[] emptyPosition;
    private final int size;

    public static PuzzleMatrix getRandomInstance(PuzzleSize puzzleSize) {
        var representationSize = (int) Math.round(Math.sqrt(puzzleSize.getValue()));
        var representation = new int[representationSize][representationSize];
        var random = new Random();

        var positionsArray = new ArrayList<int[]>();
        while (positionsArray.size() < puzzleSize.getValue()) {
            var line = random.nextInt(representationSize);
            var column = random.nextInt(representationSize);
            if (positionsArray.stream().filter(e -> e[0] == line && e[1] == column).toList().size() == 0) {
                positionsArray.add(new int[]{line, column});
            }
        }
        for (int i = 0; i < puzzleSize.getValue(); i++) {
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
        return PuzzleMatrix.builder()
                .representation(representation)
                .emptyPosition(currentEmpty)
                .size(representationSize)
                .build();
    }

    public static void move(PuzzleMatrix puzzleMatrix, MovingDirection movingDirection){
        switch (movingDirection){
            case UP -> moveUp(puzzleMatrix);
            case DOWN -> moveDown(puzzleMatrix);
            case LEFT -> moveLeft(puzzleMatrix);
            case RIGHT -> moveRight(puzzleMatrix);
            default -> throw new RuntimeException();
        }
    }

    private static void moveRight(PuzzleMatrix puzzleMatrix) {
        var line = puzzleMatrix.emptyPosition[0];
        var column = puzzleMatrix.emptyPosition[1];
        if(puzzleMatrix.emptyPosition[1] - 1 > 0){
            var aux = puzzleMatrix.representation[line][column - 1];
            puzzleMatrix.representation[line][column] = aux;
            puzzleMatrix.representation[line][column - 1] = -1;
            puzzleMatrix.emptyPosition = new int[]{line, column - 1};
        }
    }

    private static void moveLeft(PuzzleMatrix puzzleMatrix) {
        var line = puzzleMatrix.emptyPosition[0];
        var column = puzzleMatrix.emptyPosition[1];
        if(puzzleMatrix.emptyPosition[1] + 1 < puzzleMatrix.size){
            var aux = puzzleMatrix.representation[line][column + 1];
            puzzleMatrix.representation[line][column] = aux;
            puzzleMatrix.representation[line][column + 1] = -1;
            puzzleMatrix.emptyPosition = new int[]{line, column + 1};
        }
    }

    private static void moveDown(PuzzleMatrix puzzleMatrix) {
        var line = puzzleMatrix.emptyPosition[0];
        var column = puzzleMatrix.emptyPosition[1];
        if(puzzleMatrix.emptyPosition[0] + 1 < puzzleMatrix.size){
            var aux = puzzleMatrix.representation[line + 1][column];
            puzzleMatrix.representation[line][column] = aux;
            puzzleMatrix.representation[line + 1][column] = -1;
            puzzleMatrix.emptyPosition = new int[]{line + 1, column};
        }
    }

    private static void moveUp(PuzzleMatrix puzzleMatrix) {
        var line = puzzleMatrix.emptyPosition[0];
        var column = puzzleMatrix.emptyPosition[1];
        if(puzzleMatrix.emptyPosition[0] - 1 > 0){
            var aux = puzzleMatrix.representation[line - 1][column];
            puzzleMatrix.representation[line][column] = aux;
            puzzleMatrix.representation[line - 1][column] = -1;
            puzzleMatrix.emptyPosition = new int[]{line - 1, column};
        }
    }
}
