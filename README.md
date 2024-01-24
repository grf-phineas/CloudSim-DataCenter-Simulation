# Study and Implementation of Resource  Scheduling in Cloud Database #

In this project, I utilized the currently popular cloud computing environment simulation framework, CloudSim, to validate the effectiveness of algorithms. The project implements ACO (Ant Colony Optimization), PSO (Particle Swarm Optimization), FCFS (First-Come, First-Served), SJF (Shortest Job First), Greedy algorithms, and a hybrid algorithm within the CloudSim framework. The main innovations lie in the implementation of ACO and PSO, as well as the fusion algorithm combining SJF with the Greedy algorithm. Additionally, the project extends the functionality of CloudSim by visualizing the utilization of CPU and RAM in data center servers.

# To Run This Project

1. Git Clone

   ```shell
   git clone https://github.com/grf-phineas/CloudSim-DataCenter-Simulation.git
   ```

2. Run algorithmSelect.java


# Abstract #

With the widespread use of cloud-native databases, more and more enterprises are placing their databases in data centers of cloud service providers. However, cloud service providers find it difficult to provide reliable guarantees for the service quality of cloud databases. Reasonable resource scheduling for databases can minimize the completion time and average waiting time of tasks, which can improve the service quality of cloud databases to some extent.

First, this thesis analyzes the current research status of cloud resource scheduling at home and abroad. It introduces the origin and development of TPC mixed workload and briefly explains the usage principle of the mainstream open-source cloud computing simulation framework, CloudSim. The paper also presents the two mainstream scheduling algorithms: heuristic algorithms and deep reinforcement learning algorithms. The advantages and disadvantages of these two algorithms are analyzed. Furthermore, the resource scheduling problem of cloud-native databases is discussed, which focuses on the rational allocation of query transactions to virtual machines in different data centers for resource scheduling. To address this problem, the paper proposes the use of heuristic algorithms, which are characterized by fast solving speed and real-time scheduling capabilities. For small-scale problems, heuristic algorithms can quickly find a suboptimal solution within an acceptable time frame. The specific heuristic algorithms implemented in this paper include particle swarm optimization, ant colony optimization, and greedy algorithm. In particular, the greedy algorithm is combined with the Shortest Job First (SJF) algorithm, which achieves excellent performance in terms of both maximum completion time and average response time.

In order to demonstrate the effectiveness of the proposed algorithm in this paper, we used two benchmark algorithms: a fusion algorithm of sequential scheduling algorithm and First Come First Serve (FCFS) algorithm, and a fusion algorithm of sequential scheduling algorithm and Shortest Job First (SJF) algorithm. The experiments were conducted in the CloudSim cloud simulation environment, with maximum completion time and average response time as the metrics. The experimental results show that the proposed greedy algorithm combined with SJF achieves the best performance in terms of scheduling efficiency. Ant colony and particle swarm heuristic algorithms follow, both outperforming the traditional sequential scheduling algorithm.

# Reference

[1]Shao K, Song Y, Wang B. PGA: A New Hybrid PSO and GA Method for Task Scheduling with Deadline Constraints in Distributed Computing[J]. Mathematics, 2023, 11(6): 1548.

[2]Guo L, Zhao S, Shen S, et al. Task scheduling optimization in cloud computing based on heuristic algorithm[J]. Journal of networks, 2012, 7(3): 547.

[3]Tawfeek M A, El-Sisi A, Keshk A E, et al. Cloud task scheduling based on ant colony optimization[C]//2013 8th international conference on computer engineering & systems (ICCES). IEEE, 2013: 64-69.

[4]Zhang Y, Sun J, Zhu J. An effective heuristic for due-date-constrained bag-of-tasks scheduling problem for total cost minimization on hybrid clouds[C]//2016 International Conference on Progress in Informatics and Computing (PIC). IEEE, 2016: 479-486.
