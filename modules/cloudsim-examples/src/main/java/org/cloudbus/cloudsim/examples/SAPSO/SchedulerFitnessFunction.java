package org.cloudbus.cloudsim.examples.SAPSO;

import net.sourceforge.jswarm_pso.FitnessFunction;
import org.cloudbus.cloudsim.examples.utils.GenerateMatrices;
import utils.Constants;


public class SchedulerFitnessFunction extends FitnessFunction {
    private static double[][] execMatrix, commMatrix;

    SchedulerFitnessFunction() {
        super(false);
        commMatrix = GenerateMatrices.getCommMatrix();
        execMatrix = GenerateMatrices.getExecMatrix();
    }

    @Override
    public double evaluate(double[] position) {
        double alpha = 0.3;
        return alpha * calcTotalTime(position) + (1 - alpha) * calcMakespan(position);
//        return calcMakespan(position);
    }

    public double calcTotalTime(double[] position) {
        double totalCost = 0;
        for (int i = 0; i < Constants.NO_OF_TASKS; i++) {
            int dcId = (int) position[i];
            totalCost += execMatrix[i][dcId] + commMatrix[i][dcId];
        }
        return totalCost;
    }

    public double calcMakespan(double[] position) {
        double makespan = 0;
        double[] dcWorkingTime = new double[Constants.NO_OF_VMS];

        for (int i = 0; i < Constants.NO_OF_TASKS; i++) {
            int dcId = (int) position[i];
            if(dcWorkingTime[dcId] != 0) --dcWorkingTime[dcId];
            dcWorkingTime[dcId] += execMatrix[i][dcId] + commMatrix[i][dcId];
            makespan = Math.max(makespan, dcWorkingTime[dcId]);
        }
        return makespan;
    }

    public  double calcLoadCost(double[] position)
    {
        double utilization = 0.0;
        for(int i =0;i<Constants.NO_OF_TASKS;i++)
        {
            int dcId = (int) position[i];
            double time = execMatrix[i][dcId];

        }
        return utilization;
    }
}
