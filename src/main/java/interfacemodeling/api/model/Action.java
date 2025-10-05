package interfacemodeling.api.model;

public record Action(
        int id,
        Time time,
        Error error
) {
}
