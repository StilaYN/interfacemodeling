package interfacemodeling.service;

import interfacemodeling.api.model.ModelParameterRequest;
import interfacemodeling.api.model.OneTaskModelParameterRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class ProcessingExperimentService {

    private final Random random = new Random();

    private final OneTaskProcessingExperimentService oneTaskProcessingExperimentService;

    public double doExperiment(ModelParameterRequest request) {
        List<Double> workTimes = new ArrayList<>();
        List<Double> waitTimes = new ArrayList<>();
        List<Double> repairTimes = new ArrayList<>();

        for (int i = 0; i < request.N(); i++) {
            Tuple<Double, List<Double>> oneCycleResult = doOneCycleExperiment(request);
            workTimes.add(oneCycleResult._1());
            waitTimes.addAll(oneCycleResult._2());
            repairTimes.add(oneTaskProcessingExperimentService.doSingleExperiment(
                            new OneTaskModelParameterRequest(1, request.routes1(), request.actions1())
                    ).usedTime()
            );
        }

        return calculateStatistic(
                workTimes,
                waitTimes,
                repairTimes,
                request.weightWaitTime(),
                request.weightRepairTime()
        );
    }

    private Tuple<Double, List<Double>> doOneCycleExperiment(ModelParameterRequest request) {
        double timeToBreak = generateNumberByExponentialDistribution(request.lambda2());
        double currentTime = 0;
        double serverFreeTime = 0;
        List<Double> list = new ArrayList<>();
        while (currentTime < timeToBreak) {
            currentTime += generateNumberByExponentialDistribution(request.lambda1());
            if (currentTime >= timeToBreak) {
                break;
            }
            double processingTime = oneTaskProcessingExperimentService.doSingleExperiment(
                    new OneTaskModelParameterRequest(1, request.routes1(), request.actions1())
            ).usedTime();
            double serviceStartTime = Math.max(currentTime, serverFreeTime);
            double completionTime = Math.min(serviceStartTime + processingTime, timeToBreak);
            serverFreeTime = completionTime;
            double sojournTime = completionTime - currentTime;
            list.add(sojournTime);
        }
        return new Tuple<>(timeToBreak, list);
    }

    private double generateNumberByExponentialDistribution(double lambda) {
        double u = random.nextDouble(); // u ∈ [0, 1)
        while (u == 0.0) {
            u = random.nextDouble();
        }
        return -Math.log(u) / lambda;
    }

    private double calculateStatistic(
            List<Double> workTime,
            List<Double> waitTime,
            List<Double> repairTime,
            double weightWaitTime,
            double weightRepairTime
    ) {
        return (weightWaitTime * waitTime.stream().mapToDouble(Double::doubleValue).sum()
                + weightRepairTime * repairTime.stream().mapToDouble(Double::doubleValue).sum())
                / (workTime.stream().mapToDouble(Double::doubleValue).sum()
                + repairTime.stream().mapToDouble(Double::doubleValue).sum());
    }
}
