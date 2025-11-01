package interfacemodeling.api.model;

import java.util.List;

public record ModelParameterRequest(
        int N,
        double lambda1,
        double lambda2,
        List<Route> routes1,
        List<Route> routes2,
        List<Action> actions1,
        List<Action> actions2,
        double weightWaitTime,
        double weightRepairTime
) {
}
