package interfacemodeling.service;

import interfacemodeling.api.model.Action;
import interfacemodeling.api.model.ErrorAction;
import interfacemodeling.api.model.ExperimentResult;
import interfacemodeling.api.model.ModelParametersRequest;
import interfacemodeling.api.model.Rout;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ExperimentService {

    public ExperimentResult doExperiment(ModelParametersRequest parametersRequest){
        List<Double> singleExperimentResult = new ArrayList<>();
        for (int i = 0; i < parametersRequest.N(); i++){
            singleExperimentResult.add(doSingleExperiment(parametersRequest));
        }
        return ExperimentResult.builder()
                .result(singleExperimentResult)
                .middleValue(singleExperimentResult.stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(-1)
                ).build();
    }

    private Double doSingleExperiment(ModelParametersRequest parametersRequest){
        Double usedTime = 0.0;
        Rout currentRout = chooseRout(parametersRequest.routs());
        Random random = new Random();
        for (int i = 0; i < currentRout.path().size(); i++){
            Action currentAction = getActionByPlaceId(parametersRequest.actions(),currentRout.path().get(i));
            boolean isError = checkError(currentAction.error().prob());
            usedTime += (currentAction.time().right() != null) ?
                    random.nextDouble(currentAction.time().left(), currentAction.time().right())
                    : currentAction.time().left();
            if (isError){
                i = processError(currentAction.error().action(), i);
            }
        }
        return usedTime;
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

    private Rout chooseRout(List<Rout> routs){
        Random random = new Random();
        double value = random.nextDouble(0, 1);
        double currentProb = 0.0;
        for (Rout rout: routs){
            if(value >= currentProb){
                return rout;
            }
            currentProb += rout.prob();
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
