package util;

import model.Puzzle;

import java.util.ArrayList;

public class PuzzleChecker {
    public static int getInverseCount(Puzzle puzzle){
        var arr = new ArrayList<Integer>();
        for (var elem: puzzle.getRepresentation()) {
            for (var e: elem) {
                arr.add(e);
            }
        }

        var result = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = i + 1; j < 16; j++) {
                if((arr.get(i) != -1 && arr.get(j) != -1) && arr.get(i) > arr.get(j)){
                    result += 1;
                }
            }
        }
        return result;
    }

    public static int findXPosition(Puzzle puzzle){
        var result = -1;
        for (int i = 3; i >= 0; i--) {
            for (int j = 3; j >= 0; j--) {
                if(puzzle.getRepresentation()[i][j] == -1){
                    result = 4 - i;
                    return result;
                }
            }
        }
        return result;
    }

    public static boolean isSolvable(Puzzle puzzle){
        var inverseCount = getInverseCount(puzzle);
        var position = findXPosition(puzzle);
        if(position % 2 == 1){
            return inverseCount % 2 == 0;
        }else {
            return inverseCount % 2 != 0;
        }
    }
}
