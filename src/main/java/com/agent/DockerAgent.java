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
    
}
