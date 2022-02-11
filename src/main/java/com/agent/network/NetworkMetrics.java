package com.agent.network;

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

public class NetworkMetrics extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(NetworkMetrics.class);

    public synchronized static void countNetworkMetrics() {
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
            long incomingBytes = stats.getNetworks().get("eth0").getRxBytes();
            long outgoingBytes = stats.getNetworks().get("eth0").getTxBytes();
            long incomingPackets = stats.getNetworks().get("eth0").getRxPackets();
            long outgoingPackets = stats.getNetworks().get("eth0").getTxPackets();
            long incomingErrors = stats.getNetworks().get("eth0").getRxErrors();
            long outgoingErrors = stats.getNetworks().get("eth0").getTxErrors();
            long incomingDroppedPackets = stats.getNetworks().get("eth0").getRxDropped();
            long outgoingDroppedPackets = stats.getNetworks().get("eth0").getTxDropped();
            logger.info("Network metrics for container id: " + i.getKey());
            logger.info("incoming bytes : " + incomingBytes + ", outgoing bytes: " + outgoingBytes);
            logger.info("incoming packets : " + incomingPackets + ", outgoing packets: " + outgoingPackets);
            logger.info("incoming errors : " + incomingErrors + ", outgoing errors: " + outgoingErrors);
            logger.info("incoming dropped packets : " + incomingDroppedPackets + ", outgoing dropped packets: " + outgoingDroppedPackets);
        }
    }

    @Override
    public void run(){ countNetworkMetrics(); }

}
