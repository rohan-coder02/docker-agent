package com.agent.memory;

import com.agent.util.Units;
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

public class MemoryMetrics extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(MemoryMetrics.class);

    public synchronized static void countMemoryMetrics() {
        for (Map.Entry<String, Boolean> i : containerMap.entrySet()) {
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
            long memoryUsage = stats.getMemoryStats().getUsage();
            long memoryLimit = stats.getMemoryStats().getLimit();
            logger.info("Memory metrics for container id" + i.getKey() + ": memoryUsage: " + Units.BYTE.toMegaBytes(memoryUsage) + ", memoryLimit: " + Units.BYTE.toMegaBytes(memoryLimit));
        }
    }

    @Override
    public void run(){
        countMemoryMetrics();
    }
}
