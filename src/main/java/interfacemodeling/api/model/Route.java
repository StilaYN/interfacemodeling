package interfacemodeling.api.model;

import java.util.List;

public record Route(
        Double prob,
        List<Integer> path
) {
}
