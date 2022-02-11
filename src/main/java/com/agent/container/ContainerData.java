package com.agent.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientBuilder;
import com.agent.network.NetworkDataBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ContainerData extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(ContainerData.class);
    public static Map<String, Boolean> containerMap = new HashMap<>();

    public static void setData(){
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        List<Container> containers = dockerClient.listContainersCmd().exec();
        String monitoredContainers = "";
        initializeContainer(containers);
        for(Container container: containers) {
            if(containerMap.get(container.getId()) == false){
                ContainerDataBuilder containerDataBuilder = new ContainerDataBuilder.Builder()
                        .hostname(dockerClient.inspectContainerCmd(container.getId()).exec().getConfig().getHostName())
                        .containerName(Arrays.asList(container.getNames()).get(0).substring(1))
                        .containerId(container.getId())
                        .containerImageName(container.getImage())
                        .containerStartedAt(dockerClient.inspectContainerCmd(container.getId()).exec().getCreated())
                        .build();
                NetworkDataBuilder networkDataBuilder = new NetworkDataBuilder.Builder()
                        .hostname(dockerClient.inspectContainerCmd(container.getId()).exec().getConfig().getHostName())
                        .mac(container.getNetworkSettings().getNetworks().get("bridge").getMacAddress())
                        .ipv4(container.getNetworkSettings().getNetworks().get("bridge").getIpAddress())
                        .ipv6(container.getNetworkSettings().getNetworks().get("bridge").getIpV6Gateway())
                        .build();
                logger.info(containerDataBuilder.getContainerId() + "-> Name = " + containerDataBuilder.getContainerName() + ", Image name = " + containerDataBuilder.getContainerImageName() + ", Hostname = " + containerDataBuilder.getHostname() + ", Container Started At = " +containerDataBuilder.getContainerStartedAt());
                logger.info("IPv4 = " + networkDataBuilder.getIpv4() + ", IPv6 = " + networkDataBuilder.getIpv6() + ", MAC = " + networkDataBuilder.getMac());
                containerMap.replace(container.getId(), false, true);
            }
        }
        for (Map.Entry<String, Boolean> i : containerMap.entrySet()){
            monitoredContainers += i.getKey() + " ";
        }
        logger.info("Containers being monitored by Docker Agent is " + containerMap.size());
        logger.info("Collecting Docker Statistics for containerIds : " + monitoredContainers);
    }

    private static void initializeContainer(List<Container> containersList){
        for(Container container : containersList) {
            containerMap.putIfAbsent(container.getId(), false);
        }
    }

    @Override
    public void run(){
        setData();
    }
}
