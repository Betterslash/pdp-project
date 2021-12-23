package model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Node {
    private List<Node> children;
    private final PuzzleMatrix value;
    private List<Node> siblings;

    public void generateNext(){
        var puzzleValueCopy = this.value;
        var children = new ArrayList<Node>();
        for (var direction: MovingDirection.values()) {
            PuzzleMatrix.move(puzzleValueCopy, direction);
            children.add(Node.builder()
                    .children(new ArrayList<>())
                    .siblings(new ArrayList<>())
                    .value(puzzleValueCopy).build());
        }
        this.setChildren(children);
    }
}
