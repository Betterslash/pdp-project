package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PuzzleSize {
    EIGHT(8),
    SIXTEEN(15);

    private final int value;
}
