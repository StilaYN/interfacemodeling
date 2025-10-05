package interfacemodeling.api;

import interfacemodeling.api.model.ExperimentResult;
import interfacemodeling.api.model.ModelParametersRequest;
import interfacemodeling.service.ExperimentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5000")
public class InterfaceModelingController {
    private final ExperimentService experimentService;
    @PostMapping(ApiPath.CALCULATE)
    public ExperimentResult doExperiment(@RequestBody ModelParametersRequest modelParametersRequest){
        log.info("ModelParametersRequest: {}", modelParametersRequest);
        return experimentService.doExperiment(modelParametersRequest);
    }

}
