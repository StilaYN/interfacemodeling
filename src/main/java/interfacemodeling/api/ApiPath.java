package interfacemodeling.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiPath {
    public static final String CALCULATE = "/api/calculate";

    public static final String SMO_CALCULATE = "/api/smo/calculate";
}
