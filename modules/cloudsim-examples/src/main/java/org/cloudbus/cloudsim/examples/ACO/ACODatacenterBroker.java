package org.cloudbus.cloudsim.examples.ACO;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import utils.Constants;

import java.util.List;

public class ACODatacenterBroker extends DatacenterBroker {
    private double[] mapping;

    public ACODatacenterBroker(String name) throws Exception {
        super(name);
    }


    /**
     * bind Cloudlet to Vm based on ACO
     * @param antcount 蚂蚁个数
     * @param maxgen 最大迭代次数
     * **/
    //public void init(int antNum, List<? extends Cloudlet> cloudletList, List<? extends Vm> vmList)
    //public void run(int maxgen)
    public void RunACO(int antcount, int maxgen){
        long time = System.currentTimeMillis();
        ACO aco;
        aco = new ACO();
        aco.init(antcount, cloudletList, vmList);
        aco.run(maxgen);
        aco.ReportResult();
        double[] bestposition = new double[Constants.NO_OF_TASKS];
        for (int i = 0; i < cloudletList.size(); i++) {
            cloudletList.get(aco.bestTour[i].task).setVmId(aco.bestTour[i].vm);
            bestposition[i] = aco.bestTour[i].vm;
        }
        ACO_FitnessFunction ff = new ACO_FitnessFunction();
        //System.out.println("best totalcost:"+ff.calcTotalTime(bestposition));
    }

    @Override
    protected void processResourceCharacteristics(SimEvent ev) {
        DatacenterCharacteristics characteristics = (DatacenterCharacteristics) ev.getData();
        getDatacenterCharacteristicsList().put(characteristics.getId(), characteristics);

        if (getDatacenterCharacteristicsList().size() == getDatacenterIdsList().size()) {
            distributeRequestsForNewVmsAcrossDatacenters();
        }
    }

    protected void distributeRequestsForNewVmsAcrossDatacenters() {
        int numberOfVmsAllocated = 0;
        int i = 0;

        final List<Integer> availableDatacenters = getDatacenterIdsList();

        for (Vm vm : getVmList()) {
            int datacenterId = availableDatacenters.get(i++ % availableDatacenters.size());
            String datacenterName = CloudSim.getEntityName(datacenterId);

            if (!getVmsToDatacentersMap().containsKey(vm.getId())) {
                Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + vm.getId() + " in " + datacenterName);
                sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, vm);
                numberOfVmsAllocated++;
            }
        }

        setVmsRequested(numberOfVmsAllocated);
        setVmsAcks(0);
    }

}
