package org.cloudbus.cloudsim.examples.SJF;


import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.utils.DatacenterCreator;
import org.cloudbus.cloudsim.examples.utils.GenerateMatrices;
import sun.misc.VM;
import utils.Constants;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class SJF_Scheduler {

    private static List<Cloudlet> cloudletList;
    private static List<Vm> vmList;
    private static Datacenter[] datacenter;
    private static double[][] commMatrix;
    private static double[][] execMatrix;
    private static final int num = 0;

    public static List<Vm> createVM(int userId, int vms) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<Vm> list = new LinkedList<Vm>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
//        int[] mips = {500, 500, 500, 500, 500};
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        Vm[] vm = new Vm[vms];

        for (int i = 0; i < vms; i++) {
            vm[i] = new Vm(i, userId, Constants.VM_MIPS[i], pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
            list.add(vm[i]);
        }

        return list;
    }

    public static List<Cloudlet> createCloudlet(int userId, int cloudlets, int idShift) {
        // Creates a container to store Cloudlets
        LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

        //cloudlet parameters
        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        Cloudlet[] cloudlet = new Cloudlet[cloudlets];

        for (int i = 0; i < cloudlets; i++) {
            int dcId = (int) (Math.random() * Constants.NO_OF_DATA_CENTERS);
            long length = (long) (1e3 * (commMatrix[i][num] + execMatrix[i][num]));
            cloudlet[i] = new Cloudlet(idShift + i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            // setting the owner of these Cloudlets
            cloudlet[i].setUserId(userId);
            // cloudlet[i].setVmId(dcId + 2);
            list.add(cloudlet[i]);
//            System.err.println(cloudlet[i].getCloudletId() + " " + cloudlet[i].getCloudletLength() + " " + cloudlet[i].getVmId());
        }

        return list;
    }

    public static void main(String[] args) {
        Log.printLine("Starting SJF Scheduler...");

        new GenerateMatrices();
        execMatrix = GenerateMatrices.getExecMatrix();
        commMatrix = GenerateMatrices.getCommMatrix();

        try {
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            CloudSim.init(num_user, calendar, trace_flag);

            // Second step: Create Datacenters
            datacenter = new Datacenter[Constants.NO_OF_DATA_CENTERS];
            for (int i = 0; i < Constants.NO_OF_DATA_CENTERS; i++) {
                datacenter[i] = DatacenterCreator.createDatacenter("Datacenter_" + i);
            }

            //Third step: Create Broker
            SJFDatacenterBroker broker = createBroker("Broker_0");
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them to broker
            vmList = createVM(brokerId, Constants.NO_OF_VMS);
            cloudletList = createCloudlet(brokerId, Constants.NO_OF_TASKS, 0);
            broker.submitVmList(vmList);
            broker.submitCloudletList(cloudletList);

            broker.bindCloudletsToVmsSimple();
//            broker.bindCloudletsToVmsTimeAward();

            // Fifth step: Starts the simulation
            CloudSim.startSimulation();

            // Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            //newList.addAll(globalBroker.getBroker().getCloudletReceivedList());

            CloudSim.stopSimulation();

//            printCloudletList(newList);
            PrintResults(newList);
            getUtilization(datacenter);

            Log.printLine(SJF_Scheduler.class.getName() + " finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
    }

    public static void getUtilization(Datacenter[] datacenters){
        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine(indent + "Data center ID" +
                indent + indent + "Host ID" +
                indent + "CPU UTILIZATION" +
                indent + "RAM UTILIZATION");
        DecimalFormat dft = new DecimalFormat("###.##");
        dft.setMinimumFractionDigits(4);

        for(Datacenter dc: datacenters){
            for(Host host : dc.getHostList()) {
                Log.printLine(indent + indent + dc.getId() +
                        indent + indent + host.getId() +
                        indent + indent + dft.format(host.getCpuUtilization()) +
                        indent + indent + indent + dft.format(host.getRamUtilization()));
            }
        }
    }

    public static SJFDatacenterBroker createBroker(String name) throws Exception {
        return new SJFDatacenterBroker(name);
    }

    /**
     * Prints the Cloudlet objects
     *
     * @param list list of Cloudlets
     */
    public static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" +
                indent + "Data center ID" +
                indent + "VM ID" +
                indent + indent + "Time" +
                indent + "Start Time" +
                indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        dft.setMinimumIntegerDigits(2);
        double maxTime = 0;
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + dft.format(cloudlet.getCloudletId()) + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + dft.format(cloudlet.getResourceId()) +
                        indent + indent + indent + dft.format(cloudlet.getVmId()) +
                        indent + indent + dft.format(cloudlet.getActualCPUTime()) +
                        indent + indent + dft.format(cloudlet.getExecStartTime()) +
                        indent + indent + indent + dft.format(cloudlet.getFinishTime()));
                if(cloudlet.getFinishTime()>maxTime){
                    maxTime = cloudlet.getFinishTime();
                }
            }
        }
        double totalExecTime = 0;
        for (Cloudlet c : list) {
            totalExecTime += c.getExecStartTime();
        }
        double avgExecTime = totalExecTime / Constants.NO_OF_TASKS;
        System.out.println("response time:" + avgExecTime);
        System.out.println("total time:" + maxTime);
//        double makespan = calcMakespan(list);
//        Log.printLine("Makespan using SJF: " + makespan);
    }

    public static double PrintResults(List<Cloudlet> list)
    {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("================ Execution Result ==================");
        Log.printLine("No."+indent +"Cloudlet ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent+"VM mips"+ indent +"CloudletLength"+indent+ "Time"
                + indent + "Start Time" + indent + "Finish Time");
        double mxFinishTime = 0;
        DecimalFormat dft = new DecimalFormat("###.##");
        double maxTime = 0;
        for (int i = 0; i < size; i++)
        {
            cloudlet = list.get(i);
            Log.print(i+1+indent+indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getStatus()== Cloudlet.SUCCESS)
            {
                Log.print("SUCCESS");

                Log.printLine(indent +indent + indent + cloudlet.getResourceId()
                        + indent + indent + indent + cloudlet.getVmId()
                        + indent + indent + getVmById(cloudlet.getVmId()).getMips()
                        + indent + indent + cloudlet.getCloudletLength()
                        + indent + indent+ indent + indent
                        + dft.format(cloudlet.getActualCPUTime()) + indent
                        + indent + dft.format(cloudlet.getExecStartTime())
                        + indent + indent
                        + dft.format(cloudlet.getFinishTime()));
                if(cloudlet.getFinishTime()>maxTime){
                    maxTime = cloudlet.getFinishTime();
                }
            }
        }
        double totalExecTime = 0;
        for (Cloudlet c : list) {
            totalExecTime += c.getExecStartTime();
        }
        double avgExecTime = totalExecTime / Constants.NO_OF_TASKS;
        System.out.println("response time:" + avgExecTime);
        System.out.println("total time:" + maxTime);
        return maxTime;
    }

    public static Vm getVmById(int vmId)
    {
        for(Vm v:vmList)
        {
            if(v.getId()==vmId)
                return v;
        }
        return null;
    }


}
