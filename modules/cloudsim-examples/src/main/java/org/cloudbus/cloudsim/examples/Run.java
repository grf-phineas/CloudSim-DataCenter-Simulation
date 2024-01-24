package org.cloudbus.cloudsim.examples;

import utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.err;
import static java.lang.System.out;
import static org.cloudbus.cloudsim.examples.algorithmSelect.*;

public class Run {

    public Map<String, List<?>> run(String[] algorithms, int numOfTask, int numOfDC, int numOfVM){
        Constants.setNoOfTasks(numOfTask);
        Constants.setNoOfDatacenters(numOfDC);
        Constants.setNoOfVms(numOfVM);
        out.println("任务数量是：" + String.valueOf(Constants.NO_OF_TASKS));
        out.println("数据中心数量是：" + String.valueOf(Constants.NO_OF_DATA_CENTERS));
        out.println("虚拟机数量是：" + String.valueOf(Constants.NO_OF_VMS));

        Map<String, List<?>> map = new HashMap<>();
        out.println("选择的编程语言是：");
        for (String algorithm : algorithms) {
            out.println(algorithm);
        }
        if("SJF".equals(algorithms[0])){
            map = SJFgo(numOfTask, numOfDC, numOfVM);
        }else if("FCFS".equals(algorithms[0])){
            map = FCFSgo(numOfTask, numOfDC, numOfVM);
        }else if("PSO".equals(algorithms[0])){
            map = PSOgo(numOfTask, numOfDC, numOfVM);
        }else if("ACO".equals(algorithms[0])){
            map = ACOgo(numOfTask, numOfDC, numOfVM);
        }else if("Greedy".equals(algorithms[0])){
            map = Greedygo(numOfTask, numOfDC, numOfVM);
        }else{
            err.println("没有找到对应算法");
        }

        return map;
    }
}
