package interfacemodeling.api.model;

import java.util.List;

public record Route(
        Integer id,
        Double prob,
        List<Integer> path
) {
}
