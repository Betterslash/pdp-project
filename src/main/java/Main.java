import model.Node;
import service.Solver;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        var root = Node.builder()
                .value(Solver.startPuzzle)
                .fValue(0)
                .level(0)
                .build();
        var solver = Solver.builder()
                .closed(new ArrayList<>())
                .open(new ArrayList<>())
                .build();
        solver.resolve(root);
    }
}
