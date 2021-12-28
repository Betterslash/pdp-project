package service;

import lombok.Builder;
import lombok.Data;
import model.Node;
import model.Puzzle;
import util.PuzzleType;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Data
@Builder
public class Solver {
    private List<Node> open;
    private List<Node> closed;
    public final static Puzzle startPuzzle = Puzzle.getStartPuzzle(PuzzleType.DEFAULT);
    public final static Puzzle goalPuzzle = Puzzle.getGoalPuzzle();


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

    public void resolve(Node root) {
        root.setFValue(this.computeF(root, goalPuzzle));
        this.open.add(root);
        var current = this.open.get(0);
        while (true){
            Solver.prettyPrint(current);
            if(this.computeH(current.getValue(), goalPuzzle) == 0){
                break;
            }
            var generatedChildren = current.generateChildren()
                    .stream().filter(e -> !this.isInClosed(e))
                    .collect(Collectors.toList());

            for (var child:
                    generatedChildren) {
                child.setFValue(this.computeF(child, goalPuzzle));
                this.open.add(child);
            }
            this.closed.add(current);
            Collections.sort(this.open);
            this.open.remove(0);
            if(generatedChildren.size() > 0){
                Collections.sort(generatedChildren);
                current = generatedChildren.get(0);
            }else{
                var finalCurrent = current;
                var possibilities = this.open.stream().filter(e -> e.getLevel() == finalCurrent.getLevel() - 1).sorted().toList();
                current = possibilities.get(0);
            }
        }
        System.out.println(current.getLevel());
    }

    public static void prettyPrint(Node current){
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
    }

    public boolean isInClosed(Node e){
        var result = new AtomicBoolean(false);
        this.closed.forEach(q -> {
            if(!q.isDifferent(e)){
                result.set(true);
            }
        });
        return result.get();
    }
}
