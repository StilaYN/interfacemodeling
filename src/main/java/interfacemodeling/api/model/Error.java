package interfacemodeling.api.model;

public record Error(
        ErrorAction action,
        Double prob
) {
}
