package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Tree {
    private final Node root;
}
