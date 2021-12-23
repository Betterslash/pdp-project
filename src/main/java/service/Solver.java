package service;

import lombok.Builder;
import lombok.Data;
import model.Node;
import model.Puzzle;

import java.util.List;

@Data
@Builder
public class Solver {
    private List<Node> open;
    private List<Node> closed;

    private final static Puzzle startPuzzle = Puzzle.getStartPuzzle();
    private final static Puzzle goalPuzzle = Puzzle.getGoalPuzzle();

    //private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public int computeF(Node start, Puzzle goal){
        return this.computeH(start.getValue(), goal) + start.getLevel();
    }

    private int computeH(Puzzle value, Puzzle goal) {
        var temporary = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(value.getRepresentation()[i][j] != goal.getRepresentation()[i][j]){
                    if(value.getRepresentation()[i][j] != -1){
                        temporary += 1;
                    }
                }
            }
        }
        return temporary;
    }

    public void resolve() {
        var root = Node.builder()
                .value(startPuzzle)
                .fValue(0)
                .level(0)
                .build();
        root.setFValue(this.computeF(root, goalPuzzle));
        this.open.add(root);
        while (true){
            var current = this.open.get(0);
            System.out.println();
            System.out.println("  | ");
            System.out.println("  | ");
            System.out.println(" \\'/ \n");
            for (var i:
                 current.getValue().getRepresentation()) {
                for (var j: i) {
                    System.out.print(j + " ");
                }
                System.out.println();
            }
            if(this.computeH(current.getValue(), goalPuzzle) == 0){
                break;
            }
            //executorService.submit(() -> {
                var generatedChildren = current.generateChildren();
                for (var child:
                        generatedChildren) {
                    child.setFValue(this.computeF(child, goalPuzzle));
                    this.open.add(child);
                }
                this.closed.add(current);
                this.open.remove(0);

            //executorService.awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}
