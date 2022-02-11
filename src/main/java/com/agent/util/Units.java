package com.agent.util;

public enum Units {

    BYTE {
        public double toBytes(double size) {
            return size;
        }

        public double toKiloBytes(double size) {
            return size / (C1 / C0);
        }

        public double toMegaBytes(double size) {
            return size / (C2 / C0);
        }

        public double toGigaBytes(double size) {
            return size / (C3 / C0);
        }

        public double toTeraBytes(double size) {
            return size / (C4 / C0);
        }

        public double convert(double size, Units u) {
            return u.toBytes(size);
        }
    };

    public abstract double toBytes(double size);
    public abstract double toKiloBytes(double size);
    public abstract double toMegaBytes(double size);
    public abstract double toGigaBytes(double size);
    public abstract double toTeraBytes(double size);

    static final double C0 = 1d;
    static final double C1 = C0 * 1024d;
    static final double C2 = C1 * 1024d;
    static final double C3 = C2 * 1024d;
    static final double C4 = C3 * 1024d;

}
