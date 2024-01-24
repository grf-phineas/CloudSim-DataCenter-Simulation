package org.cloudbus.cloudsim.examples;


import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.ACO.ACODatacenterBroker;
import org.cloudbus.cloudsim.examples.ACO.ACO_Scheduler;
import org.cloudbus.cloudsim.examples.FCFS.FCFSDatacenterBroker;
import org.cloudbus.cloudsim.examples.FCFS.FCFS_Scheduler;
import org.cloudbus.cloudsim.examples.PSO.PSO;
import org.cloudbus.cloudsim.examples.PSO.PSODatacenterBroker;
import org.cloudbus.cloudsim.examples.PSO.PSO_Scheduler;
import org.cloudbus.cloudsim.examples.SJF.SJFDatacenterBroker;
import org.cloudbus.cloudsim.examples.SJF.SJF_Scheduler;
import org.cloudbus.cloudsim.examples.bean.CloudletVo;
import org.cloudbus.cloudsim.examples.bean.UtilizationVo;
import org.cloudbus.cloudsim.examples.utils.DatacenterCreator;
import org.cloudbus.cloudsim.examples.utils.GenerateMatrices;
import utils.Constants;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.cloudbus.cloudsim.examples.SJF.SJF_Scheduler.getUtilization;
import static org.cloudbus.cloudsim.examples.SJF.SJF_Scheduler.printCloudletList;

public class algorithmSelect {

    private static List<Cloudlet> cloudletList;
    private static List<Vm> vmList;
    private static Datacenter[] datacenter;
    private static PSO PSOSchedularInstance;
    private static double mapping[];
    private static double[][] commMatrix;
    private static double[][] execMatrix;
    private static final int num = 0;

    public static Map<String, List<?>> SJFgo(int numOfTask, int numOfDC, int numOfVM){
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
            datacenter = new Datacenter[numOfDC];
            for (int i = 0; i < numOfDC; i++) {
                datacenter[i] = DatacenterCreator.createDatacenter("Datacenter_" + i);
            }

            //Third step: Create Broker
            SJFDatacenterBroker broker = SJF_Scheduler.createBroker("Broker_0");
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them to broker
            vmList = createVM(brokerId, numOfVM);
            cloudletList = createCloudlet(brokerId, numOfTask, 0);
            for(Cloudlet cloudlet : cloudletList){
                System.err.println(cloudlet.getCloudletId() + " " + cloudlet.getCloudletLength() + " " + cloudlet.getVmId());
            }

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

            printCloudletList(newList);

            getUtilization(datacenter);

            Log.printLine(SJF_Scheduler.class.getName() + " finished!");

            //process result
            Map<String, List<?>> map = processResult(newList, datacenter);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
        return null;
    }

    public static Map<String, List<?>> FCFSgo(int numOfTask, int numOfDC, int numOfVM){
        Log.printLine("Starting FCFS Scheduler...");

        new GenerateMatrices();
        execMatrix = GenerateMatrices.getExecMatrix();
        commMatrix = GenerateMatrices.getCommMatrix();

        try {
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            CloudSim.init(num_user, calendar, trace_flag);

            // Second step: Create Datacenters
            datacenter = new Datacenter[numOfDC];
            for (int i = 0; i < numOfDC; i++) {
                datacenter[i] = DatacenterCreator.createDatacenter("Datacenter_" + i);
            }

            //Third step: Create Broker
            FCFSDatacenterBroker broker = FCFS_Scheduler.createBroker("Broker_0");
//            SJFDatacenterBroker broker = createBroker("Broker_0");
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them to broker
            vmList = createVM(brokerId, numOfVM);
            cloudletList = createCloudlet(brokerId, numOfTask, 0);

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

            printCloudletList(newList);

            getUtilization(datacenter);

            Log.printLine(FCFS_Scheduler.class.getName() + " finished!");

            //process result
            Map<String, List<?>> map = processResult(newList, datacenter);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
        return null;
    }

    public static Map<String, List<?>> Greedygo(int numOfTask, int numOfDC, int numOfVM){
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
            datacenter = new Datacenter[numOfDC];
            for (int i = 0; i < numOfDC; i++) {
                datacenter[i] = DatacenterCreator.createDatacenter("Datacenter_" + i);
            }

            //Third step: Create Broker
            SJFDatacenterBroker broker = SJF_Scheduler.createBroker("Broker_0");
//            SJFDatacenterBroker broker = createBroker("Broker_0");
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them to broker
            vmList = createVM(brokerId, numOfVM);
            cloudletList = createCloudlet(brokerId, numOfTask, 0);
            for(Cloudlet cloudlet : cloudletList){
                System.err.println(cloudlet.getCloudletId() + " " + cloudlet.getCloudletLength() + " " + cloudlet.getVmId());
            }
            broker.submitVmList(vmList);
            broker.submitCloudletList(cloudletList);

//            broker.bindCloudletsToVmsSimple();
            broker.bindCloudletsToVmsTimeAward();

            // Fifth step: Starts the simulation
            CloudSim.startSimulation();

            // Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            //newList.addAll(globalBroker.getBroker().getCloudletReceivedList());

            CloudSim.stopSimulation();

            printCloudletList(newList);

            getUtilization(datacenter);

            Log.printLine(SJF_Scheduler.class.getName() + " finished!");

            //process result
            Map<String, List<?>> map = processResult(newList, datacenter);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
        return null;
    }

    public static Map<String, List<?>> ACOgo(int numOfTask, int numOfDC, int numOfVM){
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
            datacenter = new Datacenter[numOfDC];
            for (int i = 0; i < numOfDC; i++) {
                datacenter[i] = DatacenterCreator.createDatacenter("Datacenter_" + i);
            }

            //Third step: Create Broker
            ACODatacenterBroker broker = ACO_Scheduler.createBroker("Broker_0");
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them to broker
            vmList = createVM(brokerId, numOfVM);
            cloudletList = createCloudlet(brokerId, numOfTask, 0);
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

            printCloudletList(newList);

            getUtilization(datacenter);

            Log.printLine(ACO_Scheduler.class.getName() + " finished!");

            //process result
            Map<String, List<?>> map = processResult(newList, datacenter);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
        return null;
    }

    public static Map<String, List<?>> PSOgo(int numOfTask, int numOfDC, int numOfVM){
        Log.printLine("Starting PSO Scheduler...");

        new GenerateMatrices();
        commMatrix = GenerateMatrices.getCommMatrix();
        execMatrix = GenerateMatrices.getExecMatrix();
        PSOSchedularInstance = new PSO();
        mapping = PSOSchedularInstance.run();

        try {
            String filePath = "D:\\github\\cloudsim-package\\modules\\cloudsim-examples\\src\\main\\java\\org\\cloudbus\\cloudsim\\examples\\cloudlets.txt";
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            CloudSim.init(num_user, calendar, trace_flag);

            // Second step: Create Datacenters
            datacenter = new Datacenter[numOfDC];
            for (int i = 0; i < numOfDC; i++) {
                datacenter[i] = DatacenterCreator.createDatacenter("Datacenter_" + i);
            }

            //Third step: Create Broker
            PSODatacenterBroker broker = PSO_Scheduler.createBroker("Broker_0");
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them to broker
            vmList = createVM(brokerId, numOfVM);
            cloudletList = createCloudlet(brokerId, numOfTask, 0);
//            createTasks(brokerId,filePath,Constants.NO_OF_TASKS);
            // mapping our dcIds to cloudsim dcIds
            HashSet<Integer> dcIds = new HashSet<>();
            HashMap<Integer, Integer> hm = new HashMap<>();
            for (Vm dc : vmList) {
                if (!dcIds.contains(dc.getId()))
                    dcIds.add(dc.getId());
            }
            Iterator<Integer> it = dcIds.iterator();
            for (int i = 0; i < mapping.length; i++) {
                if (hm.containsKey((int) mapping[i])) continue;
                hm.put((int) mapping[i], it.next());
            }
            for (int i = 0; i < mapping.length; i++)
                mapping[i] = hm.containsKey((int) mapping[i]) ? hm.get((int) mapping[i]) : mapping[i];

            broker.submitVmList(vmList);
            broker.setMapping(mapping);
            broker.submitCloudletList(cloudletList);

            // Fifth step: Starts the simulation
            CloudSim.startSimulation();

            List<Cloudlet> newList = broker.getCloudletReceivedList();

            CloudSim.stopSimulation();

            printCloudletList(newList);

            getUtilization(datacenter);

            Log.printLine(ACO_Scheduler.class.getName() + " finished!");

            //process result
            Map<String, List<?>> map = processResult(newList, datacenter);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
        return null;
    }

    public static List<Vm> createVM(int userId, int vms) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<Vm> list = new LinkedList<Vm>();

        //VM Parameters
        long size = 10000; //image size (MB)
//        int ram = 512; //vm memory (MB)
        Random rand = new Random();

//        int[] mips = {500, 500, 500, 500, 500};
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        Vm[] vm = new Vm[vms];

        for (int i = 0; i < vms; i++) {
            int ram = rand.nextInt(400) + 300;
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
            System.err.println(cloudlet[i].getCloudletId() + " " + cloudlet[i].getCloudletLength() + " " + cloudlet[i].getVmId());
        }

        return list;
    }

    private static Map<String, List<?>> processResult(List<Cloudlet> cloudlets, Datacenter[] datacenters) {
        List<CloudletVo> cloudletVos = cloudlets.stream()
                .map(cloudlet -> {
                    CloudletVo cloudletVo = new CloudletVo();
                    cloudletVo.setCloudletId(cloudlet.getCloudletId());
                    cloudletVo.setResourceId(cloudlet.getResourceId());
                    cloudletVo.setVmId(cloudlet.getVmId());
                    cloudletVo.setActualCPUTime(cloudlet.getActualCPUTime());
                    cloudletVo.setExecStartTime(cloudlet.getExecStartTime());
                    cloudletVo.setFinishTime(cloudlet.getFinishTime());
                    cloudletVo.setStatus("Success");
                    return cloudletVo;
                }).collect(Collectors.toList());

        double totalExecTime = 0;
        for (Cloudlet cloudlet : cloudlets) {
            totalExecTime += cloudlet.getExecStartTime();
        }
        double avgExecTime = totalExecTime / Constants.NO_OF_TASKS;
        List<Double> avgTimeList = new ArrayList<>();
        avgTimeList.add(avgExecTime);
        System.out.println("response time:" + avgExecTime);

        List<UtilizationVo> utilizationVos = new ArrayList<>(); // 用于存储UtilizationVo对象

        DecimalFormat dft = new DecimalFormat("#.####");
        dft.setMinimumFractionDigits(4);
        for(Datacenter dc: datacenters){
            for(Host host : dc.getHostList()) {
                UtilizationVo utilizationVo = new UtilizationVo(); // 新建UtilizationVo对象
                utilizationVo.setDatacenterId(dc.getId());
                utilizationVo.setHostId(host.getId());
                utilizationVo.setCpuUtilization(Double.parseDouble(dft.format(host.getCpuUtilization())));
                utilizationVo.setRamUtilization(Double.parseDouble(dft.format(host.getRamUtilization())));

                utilizationVos.add(utilizationVo); // 将UtilizationVo对象存入list中
            }
        }

        Map<String, List<?>> map = new HashMap<>();
        map.put("cloudletList",cloudletVos);
        map.put("utilizationList",utilizationVos);
        map.put("avgTimeList", avgTimeList);
        return map;
    }
}
