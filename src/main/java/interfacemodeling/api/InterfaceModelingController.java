package interfacemodeling.api;

import interfacemodeling.api.model.ExperimentResult;
import interfacemodeling.api.model.ModelParameterRequest;
import interfacemodeling.api.model.OneTaskModelParameterRequest;
import interfacemodeling.service.OneTaskProcessingExperimentService;
import interfacemodeling.service.ProcessingExperimentService;
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

    private final OneTaskProcessingExperimentService oneTaskProcessingExperimentService;

    private final ProcessingExperimentService processingExperimentService;

    @PostMapping(ApiPath.CALCULATE)
    public ExperimentResult doExperiment(@RequestBody OneTaskModelParameterRequest oneTaskModelParameterRequest){
        log.info("OneTaskModelParametersRequest: {}", oneTaskModelParameterRequest);
        return oneTaskProcessingExperimentService.doExperiment(oneTaskModelParameterRequest);
    }

    @PostMapping(ApiPath.SMO_CALCULATE)
    public double doSmoExperiment(@RequestBody ModelParameterRequest modelParametersRequest){
        log.info("ModelParametersRequest: {}", modelParametersRequest);
        return processingExperimentService.doExperiment(modelParametersRequest);
    }

}
