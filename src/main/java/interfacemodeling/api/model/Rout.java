package interfacemodeling.api.model;

import java.util.List;

public record Rout(
        Double prob,
        List<Integer> path
) {
}
