package interfacemodeling.api.model;

import java.util.List;

public record ModelParametersRequest(
        int N,
        List<Route> routes,
        List<Action> actions
) {
}
