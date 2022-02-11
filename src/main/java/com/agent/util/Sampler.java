package com.agent.util;

public enum Sampler {

    SAMPLER{
        public long perMinute(){
            return 60000;
        }
    };

    public abstract long perMinute();
}
