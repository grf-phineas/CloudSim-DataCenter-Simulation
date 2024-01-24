package org.cloudbus.cloudsim.examples.bean;

public class CloudletVo {

    private String status;
    private int cloudletId;
    private int resourceId;
    private int vmId;
    private double actualCPUTime;
    private double execStartTime;
    private double finishTime;

    public CloudletVo() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCloudletId() {
        return cloudletId;
    }

    public void setCloudletId(int cloudletId) {
        this.cloudletId = cloudletId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getVmId() {
        return vmId;
    }

    public void setVmId(int vmId) {
        this.vmId = vmId;
    }

    public double getActualCPUTime() {
        return actualCPUTime;
    }

    public void setActualCPUTime(double actualCPUTime) {
        this.actualCPUTime = actualCPUTime;
    }

    public double getExecStartTime() {
        return execStartTime;
    }

    public void setExecStartTime(double execStartTime) {
        this.execStartTime = execStartTime;
    }

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }
}
