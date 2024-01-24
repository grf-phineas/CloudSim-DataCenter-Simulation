package org.cloudbus.cloudsim.examples.bean;

public class UtilizationVo {
    private int datacenterId;
    private int hostId;
    private double cpuUtilization;
    private double ramUtilization;

    public UtilizationVo() {
    }

    public int getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(int datacenterId) {
        this.datacenterId = datacenterId;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public double getCpuUtilization() {
        return cpuUtilization;
    }

    public void setCpuUtilization(double cpuUtilization) {
        this.cpuUtilization = cpuUtilization;
    }

    public double getRamUtilization() {
        return ramUtilization;
    }

    public void setRamUtilization(double ramUtilization) {
        this.ramUtilization = ramUtilization;
    }
}
