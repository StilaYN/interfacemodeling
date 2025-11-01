package interfacemodeling.api.model;

import java.util.List;

public record OneTaskModelParameterRequest(
        int N,
        List<Route> routes,
        List<Action> actions
) {
}
