package com.agent;

import com.agent.container.ContainerData;
import com.agent.cpu.CpuMetrics;
import com.agent.memory.MemoryMetrics;
import com.agent.network.NetworkMetrics;

import com.agent.util.Sampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

import java.util.Timer;
import java.util.TimerTask;

public class DockerAgent {

    private static final Logger logger = LoggerFactory.getLogger(DockerAgent.class);
    private static final ContainerData containerData = new ContainerData();
    private static final CpuMetrics cpuMetrics = new CpuMetrics();
    private static final MemoryMetrics memoryMetrics = new MemoryMetrics();
    private static final NetworkMetrics networkMetrics = new NetworkMetrics();
    private static String scheduler[] = {"Container-Scheduler", "Cpu-Scheduler", "Memory-Scheduler", "Network-Scheduler"};

    public static void main(String[] args) throws InterruptedException {
        initialize();
        System.out.println("Docker Agent started successfully");
        logger.info("Docker Agent started successfully");
        while(true){
            containerData.run();
            collectSamples(Sampler.SAMPLER.perMinute());
            cpuMetrics.run();
            memoryMetrics.run();
            networkMetrics.run();
        }
    }

    private static void initialize(){
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        String version = Runtime.class.getPackage().getImplementationVersion();
        String userDir = System.getProperty("user.dir");
        System.out.println("Using Java version [" + version + "] for Agent");
        logger.info("Using Java version " + version + " for agent");
        System.out.println("Agent jar file location = " + userDir);
        logger.info("Agent file location = " + userDir);
        System.out.println("Operating System = " + systemInfo.getOperatingSystem());
        logger.info("Operating System = " + systemInfo.getOperatingSystem());
    }

    private static void collectSamples(long time) throws InterruptedException {
        Thread.sleep(time);
    }

    private static void startMetricCollection(){
        Timer timer = new Timer(scheduler[0]);
        TimerTask containerTask = new ContainerData();
        timer.schedule(containerTask, 0, 60000);
        Timer timer1 = new Timer(scheduler[1]);
        TimerTask cpuTask = new CpuMetrics();
        timer1.schedule(cpuTask, 0, 60000);
        Timer timer2 = new Timer(scheduler[2]);
        TimerTask memoryTask = new MemoryMetrics();
        timer2.schedule(memoryTask, 0, 60000);
        Timer timer3 = new Timer(scheduler[3]);
        TimerTask networkTask = new NetworkMetrics();
        timer3.schedule(networkTask, 0, 60000);
    }
}
