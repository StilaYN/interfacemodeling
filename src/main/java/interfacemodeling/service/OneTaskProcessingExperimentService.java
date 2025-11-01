package interfacemodeling.service;

import interfacemodeling.api.model.Action;
import interfacemodeling.api.model.ErrorAction;
import interfacemodeling.api.model.ExperimentResult;
import interfacemodeling.api.model.OneTaskModelParameterRequest;
import interfacemodeling.api.model.Route;
import interfacemodeling.api.model.SingleExperimentResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OneTaskProcessingExperimentService {

    public ExperimentResult doExperiment(OneTaskModelParameterRequest parametersRequest){
        List<SingleExperimentResult> singleExperimentResult = new ArrayList<>();
        for (int i = 0; i < parametersRequest.N(); i++){
            singleExperimentResult.add(doSingleExperiment(parametersRequest));
        }
        return ExperimentResult.builder()
                .result(singleExperimentResult)
                .middleValue(singleExperimentResult.stream()
                        .mapToDouble(SingleExperimentResult::usedTime)
                        .average()
                        .orElse(-1)
                ).build();
    }

    public SingleExperimentResult doSingleExperiment(OneTaskModelParameterRequest parametersRequest){
        double usedTime = 0.0;
        Route currentRoute = chooseRout(parametersRequest.routes());
        Random random = new Random();
        for (int i = 0; i < currentRoute.path().size(); i++){
            Action currentAction = getActionByPlaceId(parametersRequest.actions(), currentRoute.path().get(i));
            boolean isError = checkError(currentAction.error().prob());
            usedTime += (currentAction.time().right() != null) ?
                    random.nextDouble(currentAction.time().left(), currentAction.time().right())
                    : currentAction.time().left();
            if (isError){
                i = processError(currentAction.error().action(), i);
            }
        }
        return SingleExperimentResult.builder()
                .route(currentRoute)
                .usedTime(usedTime)
                .build();
    }

    private boolean checkError(Double prob){
        Random random = new Random();
        return random.nextDouble(0, 1) <= prob;
    }

    private int processError(ErrorAction action, int currentIndex) {
        if(currentIndex == 0){
            return -1;
        }
        if (action == ErrorAction.REPEAT) {
            return currentIndex - 1;
        } else if (action == ErrorAction.BACK_A_STEP) {
            return currentIndex - 2;
        } else {
            return -1;
        }
    }

    private Route chooseRout(List<Route> routes){
        Random random = new Random();
        double value = random.nextDouble(0, 1);
        double currentProb = 0.0;
        for (Route route : routes){
            if(value >= currentProb){
                return route;
            }
            currentProb += route.prob();
        }
        throw new RuntimeException("суммарная вероятность != 1");
    }

    private Action getActionByPlaceId(List<Action> actions, int id){
        return actions.stream()
                .filter(action -> action.id() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("нет поля с таким id"));
    }


}
