package org.cloudbus.cloudsim;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

import java.util.List;

public class NewHost extends Host{

    /** The utilization mips. */
    private double utilizationMips;

    /** The previous utilization mips. */
    private double previousUtilizationMips;




    /**
     * Instantiates a new host.
     *
     * @param id             the host id
     * @param ramProvisioner the ram provisioner
     * @param bwProvisioner  the bw provisioner
     * @param storage        the storage capacity
     * @param peList         the host's PEs list
     * @param vmScheduler    the vm scheduler
     */
//    public NewHost(int id, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner, long storage, List<? extends Pe> peList, VmScheduler vmScheduler) {
//        super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
//    }
    public NewHost(
            int id,
            RamProvisioner ramProvisioner,
            BwProvisioner bwProvisioner,
            long storage,
            List<? extends Pe> peList,
            VmScheduler vmScheduler) {
        super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
        setUtilizationMips(0);
        setPreviousUtilizationMips(0);
    }

    public double updateVmsProcessing(double currentTime) {
        double smallerTime = super.updateVmsProcessing(currentTime);
        setPreviousUtilizationMips(getUtilizationMips());
        setUtilizationMips(0);

        for (Vm vm : getVmList()) {
            getVmScheduler().deallocatePesForVm(vm);
        }

        for (Vm vm : getVmList()) {
            getVmScheduler().allocatePesForVm(vm, vm.getCurrentRequestedMips());
        }

        int vmId = -1 ,hostId = -1 , datacenterId = -1;
        for (Vm vm : getVmList()) {
            datacenterId = getDatacenter().getId();
            double totalRequestedMips = vm.getCurrentRequestedTotalMips();
            double totalAllocatedMips = getVmScheduler().getTotalAllocatedMipsForVm(vm);

            if (!Log.isDisabled()) {
//                Log.formatLine(
//                        "%.2f: [Host #" + getId() + "] Total allocated MIPS for VM #" + vm.getId()
//                                + " (Host #" + vm.getHost().getId() + " datacenter #" + datacenterId
//                                + ") is %.2f, was requested %.2f out of total %.2f (%.2f%%)",
//                        CloudSim.clock(),
//                        totalAllocatedMips,
//                        totalRequestedMips,
//                        vm.getMips(),
//                        totalRequestedMips / vm.getMips() * 100);

                List<Pe> pes = getVmScheduler().getPesAllocatedForVM(vm);
                StringBuilder pesString = new StringBuilder();
                for (Pe pe : pes) {
                    pesString.append(String.format(" PE #" + pe.getId() + ": %.2f.", pe.getPeProvisioner()
                            .getTotalAllocatedMipsForVm(vm)));
                }
//                Log.formatLine(
//                        "%.2f: [Host #" + getId() + "] MIPS for VM #" + vm.getId() + " by PEs ("
//                                + getNumberOfPes() + " * " + getVmScheduler().getPeCapacity() + ")."
//                                + pesString,
//                        CloudSim.clock());
            }

            setUtilizationMips(getUtilizationMips() + totalAllocatedMips);
        }
        double ramUtil = getRamProvisioner().getUsedRam();
        double ram = getRamProvisioner().getRam();
        if(ramUtil == 0) System.out.println("breakpoint");
        String indent = "    ";
        System.out.println(" datacenterId:" + datacenterId + " vmId:" + vmId + " hostId:" + hostId + " ram:"+ramUtil/ram + " cpu:"+getUtilizationOfCpu());
        setCpuUtilization(getUtilizationOfCpu());
        setRamUtilization(ramUtil/ram);
//        System.out.println("cpu:"+getUtilizationOfCpu());
        return smallerTime;
    }

    /**
     * Sets the previous utilization of CPU in mips.
     *
     * @param previousUtilizationMips the new previous utilization of CPU in mips
     */
    protected void setPreviousUtilizationMips(double previousUtilizationMips) {
        this.previousUtilizationMips = previousUtilizationMips;
    }

    public double getUtilizationMips() {
        return utilizationMips;
    }

    /**
     * Sets the utilization mips.
     *
     * @param utilizationMips the new utilization mips
     */
    protected void setUtilizationMips(double utilizationMips) {
        this.utilizationMips = utilizationMips;
    }

    /**
     * Get current utilization of CPU in percentage.
     *
     * @return current utilization of CPU in percents
     */
    public double getUtilizationOfCpu() {
        double utilization = getUtilizationMips() / getTotalMips();
        if (utilization > 1 && utilization < 1.01) {
            utilization = 1;
        }
        return utilization;
    }

}
