package interfacemodeling.api.model;

import lombok.Builder;

import java.util.List;

@Builder
public record ExperimentResult(
        List<Double> result,
        Double middleValue
) {
}
