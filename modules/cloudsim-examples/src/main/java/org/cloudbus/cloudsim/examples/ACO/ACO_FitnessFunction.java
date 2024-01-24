package org.cloudbus.cloudsim.examples.ACO;


import org.cloudbus.cloudsim.examples.utils.GenerateMatrices;
import utils.Constants;


public class ACO_FitnessFunction {

    private static double[][] execMatrix, commMatrix;

    public ACO_FitnessFunction()
    {
        commMatrix = GenerateMatrices.getCommMatrix();
        execMatrix = GenerateMatrices.getExecMatrix();
    }

    public double calcTotalTime(double[] position) {
        double totalCost = 0;
        for (int i = 0; i < Constants.NO_OF_TASKS; i++) {
            int dcId = (int) position[i];
            totalCost += execMatrix[i][dcId] + commMatrix[i][dcId];
        }
        return totalCost;
    }

}
