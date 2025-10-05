package interfacemodeling.api.model;

import java.util.List;

public record ModelParametersRequest(
        int N,
        List<Rout> routs,
        List<Action> actions
) {
}
