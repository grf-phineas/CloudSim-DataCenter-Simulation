package org.cloudbus.cloudsim.examples.PSO;

import net.sourceforge.jswarm_pso.Swarm;
import utils.Constants;

public class PSO {
    private static Swarm swarm;
    private static SchedulerParticle particles[];
    private static SchedulerFitnessFunction ff = new SchedulerFitnessFunction();

    public PSO() {
        initParticles();
    }


    public double[] run() {
        swarm = new Swarm(Constants.POPULATION_SIZE, new SchedulerParticle(), ff);

        swarm.setMinPosition(0);
        swarm.setMaxPosition(Constants.NO_OF_VMS - 1);
        swarm.setMaxMinVelocity(0.5);
//        double[] velocity = new double[Constants.NO_OF_VMS];
//        double[] min_velocity = new double[Constants.NO_OF_VMS];
//        for(int i=0; i<Constants.NO_OF_VMS; i++){
//            velocity[i] = 1;
//            min_velocity[i] = 0;
//        }
//
//        swarm.setMaxVelocity(velocity);
//        swarm.setMinVelocity(min_velocity);

        swarm.setParticles(particles);
        swarm.setParticleUpdate(new SchedulerParticleUpdate(new SchedulerParticle()));

        for (int i = 0; i < Constants.NO_OF_Iterations; i++) {
            swarm.evolve();
            if (i % 10 == 0) {
                System.out.printf("Gloabl best at iteration (%d): %f\n", i, swarm.getBestFitness());   //最短的最大完工时间
            }
//            swarm.setInertia();
        }
        System.out.println("\nThe best fitness value: " + swarm.getBestFitness() + "\nBest makespan: " + ff.calcMakespan(swarm.getBestParticle().getBestPosition()));

        System.out.println("The best solution is: ");
        SchedulerParticle bestParticle = (SchedulerParticle) swarm.getBestParticle();
        System.out.println(bestParticle.toString());

        return swarm.getBestPosition();       //最佳任务调度策略
    }

    private static void initParticles() {
        particles = new SchedulerParticle[Constants.POPULATION_SIZE];
        for (int i = 0; i < Constants.POPULATION_SIZE; ++i)
            particles[i] = new SchedulerParticle();
    }

    public void printBestFitness() {
        System.out.println("\nBest fitness value: " + swarm.getBestFitness() +
                "\nBest makespan: " + ff.calcMakespan(swarm.getBestParticle().getBestPosition()));
    }
}
