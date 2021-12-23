import model.Node;
import model.PuzzleMatrix;
import model.PuzzleSize;
import model.Tree;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        var puzzle = PuzzleMatrix.getRandomInstance(PuzzleSize.SIXTEEN);
        var tree = Tree.builder()
                .root(Node.builder()
                        .children(new ArrayList<>())
                        .siblings(new ArrayList<>())
                        .value(puzzle)
                        .build())
                .build();
        tree.getRoot().generateNext();
        System.out.println(tree);
    }
}
