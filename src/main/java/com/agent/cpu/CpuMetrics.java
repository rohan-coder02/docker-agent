package com.agent.cpu;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.InvocationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.TimerTask;

import static com.agent.container.ContainerData.containerMap;

public class CpuMetrics extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(CpuMetrics.class);

    public synchronized static void countCpuMetrics() {
        for (Map.Entry<String, Boolean> i : containerMap.entrySet()){
            DockerClient dockerClient = DockerClientBuilder.getInstance().build();
            InvocationBuilder.AsyncResultCallback<Statistics> callback = new InvocationBuilder.AsyncResultCallback<>();
            dockerClient.statsCmd(i.getKey()).exec(callback);
            Statistics stats = null;
            try {
                stats = callback.awaitResult();
                callback.close();
            } catch (RuntimeException | IOException e) {
                logger.error(e.toString());
            }
            double cpuDelta = stats.getCpuStats().getCpuUsage().getTotalUsage() - stats.getPreCpuStats().getCpuUsage().getTotalUsage();
            double systemCpuDelta = stats.getCpuStats().getSystemCpuUsage();
            double cpuPercent = (cpuDelta/systemCpuDelta) * stats.getCpuStats().getOnlineCpus() * 100;
            double cpuThrottle = stats.getCpuStats().getThrottlingData().getThrottledTime();
            logger.info("CPU Delta = " + cpuDelta + " System CPU Delta = " + systemCpuDelta);
            logger.info("Cpu metrics for container id " + i.getKey() + ": cpu usage: " + cpuPercent + ", cpu throttle: " + cpuThrottle);
        }
    }

    @Override
    public void run(){ countCpuMetrics(); }
}
