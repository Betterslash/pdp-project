import service.Solver;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var solver = Solver.builder()
                .closed(new ArrayList<>())
                .open(new ArrayList<>())
                .build();

        solver.resolve();
        System.out.println(solver.getClosed());

    }
}
