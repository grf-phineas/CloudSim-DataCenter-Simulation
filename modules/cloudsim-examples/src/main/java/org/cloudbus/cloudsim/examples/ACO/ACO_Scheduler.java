package org.cloudbus.cloudsim.examples.ACO;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.utils.DatacenterCreator;
import org.cloudbus.cloudsim.examples.utils.GenerateMatrices;
import utils.Constants;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ACO_Scheduler
{
    private static List<Cloudlet> cloudletList = new LinkedList<>();
    private static List<Vm> vmList;
    //    private static Datacenter[] datacenter;
    private static Datacenter[] datacenter;
    private static ACO ACOSchedularInstance;
    private static double mapping[];
    private static double[][] commMatrix;
    private static double[][] execMatrix;

    public static void main(String[] args)
    {
        Log.printLine("Starting ACO Scheduler...");

        new GenerateMatrices();
        commMatrix = GenerateMatrices.getCommMatrix();
        execMatrix = GenerateMatrices.getExecMatrix();
//        ACOSchedularInstance = new ACO();
//        mapping = ACOSchedularInstance.run();

        try {
            String filePath = "E:\\CloudsimCode\\james_cloudsim-task-scheduling\\cloudsim-task-scheduling-master\\cloudlets.txt";
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            CloudSim.init(num_user, calendar, trace_flag);

            // Second step: Create Datacenter
            datacenter = new Datacenter[Constants.NO_OF_DATA_CENTERS];
            for (int i = 0; i < Constants.NO_OF_DATA_CENTERS; i++) {
                datacenter[i] = DatacenterCreator.createDatacenter("Datacenter_" + i);
            }

            //Third step: Create Broker
            ACODatacenterBroker broker = createBroker("Broker_0");
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them to broker
            vmList = createVM(brokerId, Constants.NO_OF_VMS);
            cloudletList = createCloudlet(brokerId, Constants.NO_OF_TASKS, 0);
//            createTasks(brokerId,filePath,Constants.NO_OF_TASKS);

            GenerateMatrices GM = new GenerateMatrices();
//            commMatrix = GM.getcommMatrix();
//            execMatrix = GM.getexecMatrix();
            broker.submitVmList(vmList);
//            broker.setMapping(mapping);
            broker.submitCloudletList(cloudletList);

            broker.RunACO(Constants.NO_OF_ANTS, Constants.NO_OF_GENERATIONS);
            // Fifth step: Starts the simulation
            CloudSim.startSimulation();

            List<Cloudlet> newList = broker.getCloudletReceivedList();

            CloudSim.stopSimulation();

//            printCloudletList(newList);
            PrintResults(newList);
            Log.printLine(ACO_Scheduler.class.getName() + " finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }

    }

    public static List<Vm> createVM(int userId, int vms) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<Vm> list = new LinkedList<Vm>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 500;
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

    public static void createTasks(int brokerId,String filePath, int taskNum)
    {
        try
        {
            @SuppressWarnings("resource")
            BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String data = null;
            int index = 0;

            //cloudlet properties.
            int pesNumber = 1;
            long fileSize = 1000;
            long outputSize = 1000;
            UtilizationModel utilizationModel = new UtilizationModelFull();

            while ((data = br.readLine()) != null)
            {
                System.out.println(data);
                String[] taskLength=data.split("\t");//tasklength[i]是任务执行的耗费（指令数量）
                for(int j=0;j<20;j++){
                    Cloudlet task=new Cloudlet(index+j, (long) Double.parseDouble(taskLength[j]), pesNumber, fileSize,
                            outputSize, utilizationModel, utilizationModel,
                            utilizationModel);
                    task.setUserId(brokerId);
                    cloudletList.add(task);
                    if(cloudletList.size()==taskNum)
                    {
                        br.close();
                        return;
                    }
                }
                //20 cloudlets each line in the file cloudlets.txt.
                index+=20;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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
            long length = (long) (1e3 * (commMatrix[i][0] + execMatrix[i][0]));
            cloudlet[i] = new Cloudlet(idShift + i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            // setting the owner of these Cloudlets
            cloudlet[i].setUserId(userId);
//            cloudlet[i].setVmId(dcId + 2);
            list.add(cloudlet[i]);
        }
        return list;
    }

    public static ACODatacenterBroker createBroker(String name) throws Exception {
        return new ACODatacenterBroker(name);
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
        Log.printLine("================ Execution Result Ends here ==================");
        Log.printLine(maxTime);

        double totalExecTime = 0;
        for (Cloudlet c : list) {
            totalExecTime += c.getExecStartTime();
        }
        double avgExecTime = totalExecTime / Constants.NO_OF_TASKS;
        System.out.println("response time:" + avgExecTime);
        return mxFinishTime;
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
