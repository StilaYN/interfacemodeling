package interfacemodeling.api.model;

import lombok.Builder;

import java.util.List;

@Builder
public record ExperimentResult(
        List<SingleExperimentResult> result,
        Double middleValue
) {
}
