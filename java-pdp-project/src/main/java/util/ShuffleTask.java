package util;

import lombok.Data;
import model.Node;
import model.Puzzle;

import java.util.Arrays;

@Data
public class ShuffleTask implements Runnable{

    private Puzzle value;
    private int[] move;
    private int level;

    private Node resultedNode = null;

    public ShuffleTask(Puzzle value, int[] move, int level){
        this.value = value;
        this.move = move;
        this.level = level;
    }

    @Override
    public void run() {
        var emptyPosition = value.getEmptyPosition();
        var line = emptyPosition[0] + move[0];
        var column = emptyPosition[1] + move[1];
        if(line < value.getSize() && line >= 0){
            if(column < value.getSize() && column >= 0){
                var puzzleCopy = Arrays.stream(value.getRepresentation())
                        .map(int[]::clone)
                        .toArray(int[][]::new);
                var aux = puzzleCopy[line][column];
                puzzleCopy[line][column] = -1;
                puzzleCopy[emptyPosition[0]][emptyPosition[1]] = aux;
                this.setResultedNode(Node.builder()
                        .level(level + 1)
                        .fValue(0)
                        .value(Puzzle.builder()
                                .representation(puzzleCopy)
                                .emptyPosition(new int[]{line, column})
                                .size(value.getSize())
                                .build())
                        .build());
            }
        }
    }
}
