package interfacemodeling.api.model;

import lombok.Builder;

@Builder
public record SingleExperimentResult(
        Route route,
        Double usedTime
) {
}
