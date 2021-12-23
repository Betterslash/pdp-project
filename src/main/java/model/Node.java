package model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
public class Node {
    private Puzzle value;
    private int fValue;
    private int level;

    public List<Node> generateChildren(){
        var moves = new int[][]{{1, 0},
                {0, 1},
                {-1, 0},
                {0, -1}};
        var children = new ArrayList<Node>();
        for (var move: moves) {
            var child = this.shuffle(value, move);
            if(child != null){
                children.add(child);
            }
        }
        return children;
    }

    private Node shuffle(Puzzle value, int[] move) {
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
                return Node.builder()
                        .level(level + 1)
                        .fValue(0)
                        .value(Puzzle.builder()
                                .representation(puzzleCopy)
                                .emptyPosition(new int[]{line, column})
                                .size(value.getSize())
                                .build())
                        .build();
            }
        }
        return null;
    }
}
