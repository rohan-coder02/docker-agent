package com.agent.network;

import lombok.Getter;

@Getter
public class NetworkDataBuilder {

    private String hostname, mac, ipv4, ipv6;
    public NetworkDataBuilder(Builder builder){
        this.hostname = builder.hostname;
        this.mac = builder.mac;
        this.ipv4 = builder.ipv4;
        this.ipv6 = builder.ipv6;
    }
    public static class Builder{
        private String hostname, mac, ipv4, ipv6;
        public Builder hostname(String hostname){
            this.hostname = hostname;
            return this;
        }
        public Builder mac(String mac){
            this.mac = mac;
            return this;
        }
        public Builder ipv4(String ipv4){
            this.ipv4 = ipv4;
            return this;
        }
        public Builder ipv6(String ipv6){
            this.ipv6 = ipv6;
            return this;
        }
        public NetworkDataBuilder build(){return new NetworkDataBuilder(this);}
    }
}
