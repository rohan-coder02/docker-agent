package com.agent.container;

import lombok.Getter;

@Getter
public class ContainerDataBuilder {

    private String hostname;
    private String containerName;
    private String containerId;
    private String containerImageName;
    private String containerStartedAt;
    public ContainerDataBuilder(Builder builder){
        this.hostname = builder.hostname;
        this.containerName = builder.containerName;
        this.containerId = builder.containerId;
        this.containerImageName = builder.containerImageName;
        this.containerStartedAt = builder.containerStartedAt;
    }
    public static class Builder{
        private String hostname;
        private String containerName;
        private String containerId;
        private String containerImageName;
        private String containerStartedAt;
        public Builder hostname(String hostname){
            this.hostname = hostname;
            return this;
        }
        public Builder containerName(String containerName){
            this.containerName = containerName;
            return this;
        }
        public Builder containerId(String containerId){
            this.containerId = containerId;
            return this;
        }
        public Builder containerImageName(String containerImageName){
            this.containerImageName = containerImageName;
            return this;
        }
        public Builder containerStartedAt(String containerStartedAt){
            this.containerStartedAt = containerStartedAt;
            return this;
        }
        public ContainerDataBuilder build(){
            return new ContainerDataBuilder(this);
        }
    }
}
