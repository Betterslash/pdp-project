package model;

import lombok.Builder;
import lombok.Data;
import util.ShuffleTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Data
@Builder
public class Node implements Comparable<Node>{
    private Puzzle value;
    private int fValue;
    private int level;

    public List<Node> generateChildren(){
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        List<ShuffleTask> tasks = new ArrayList<>();
        var moves = new int[][]{{1, 0},
                {0, 1},
                {-1, 0},
                {0, -1}};
        var children = new ArrayList<Node>();
        for (var move: moves) {
            ShuffleTask task = new ShuffleTask(value, move, this.getLevel());
            tasks.add(task);
            executor.execute(task);

        }

        executor.shutdown();
        try {
            executor.awaitTermination(50, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (ShuffleTask task: tasks){
            var child = task.getResultedNode();
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

    @Override
    public int compareTo(Node o) {
        return this.getFValue() - o.getFValue();
    }

    public boolean isDifferent(Node node){
        var isDifferent = false;
        for (int i = 0; i < this.value.getSize(); i++) {
            for (int j = 0; j < this.value.getSize(); j++) {
                if (node.getValue().getRepresentation()[i][j] != this.value.getRepresentation()[i][j]) {
                    isDifferent = true;
                    break;
                }
            }
        }
        return isDifferent;
    }
}
