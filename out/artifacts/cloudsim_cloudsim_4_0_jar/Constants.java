package utils;

public class Constants {
    public static int POPULATION_SIZE = 25;
    public static int NO_OF_Iterations = 50;
    //ACO
    public static int NO_OF_ANTS = 10;
    public static int NO_OF_GENERATIONS = 80;
    
    //Datacenter Parameters
    public static int NO_OF_DATACENTERS = 5;
    public static String ARCHITECTURE = "x86";
    public static String OS = "Linux";
    public static double TIME_ZONE = 5.5;
    public static double COST_PROCESSING = 3.0;
    public static double COST_MEMORY = 0.05;
    public static double COST_STORAGE = 0.001;
    public static double COST_BANDWIDTH = 0.1;

    //Host Parameters
    public static int NO_OF_HOSTS = 2;
    public static int STORAGE = 1000000;
    public static int HOST_RAM = 2048;
    public static int HOST_BANDWIDTH = 10000;
    public static int HOST_MIPS = 1000;

    //VM Parameters
    public static int NO_OF_VMS = 10;
    public static long VM_IMAGE_SIZE = 10000;
    public static int VM_RAM = 512;
    public static int[] VM_MIPS = {600, 500, 400, 300, 200, 100, 150, 250, 350, 450};
    public static long VM_BANDWIDTH = 1000;
    public static int VM_PES = 1;
    public static String VMM_NAME = "Topa";

    //Cloudlet Parameters
    public static int NO_OF_TASKS = 20;
    public static long FILE_SIZE = 300;
    public static long OUTPUT_SIZE = 300;
    public static int TASK_PES = 1;

    public static final int NO_OF_DATA_CENTERS = 5; // number of Datacenters;


}
