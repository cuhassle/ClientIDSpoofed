package com.novinitygames.clientid.utils;

import java.util.Comparator;

public class Version implements Comparable<Version> {
    int major;
    int minor;
    int patch;

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static Version fromString(String version) {
        String[] split = version.split("\\.");
        try {
            int major = Integer.parseInt(split[0]);
            int minor = Integer.parseInt(split[1]);
            int patch = Integer.parseInt(split[2]);
            return new Version(major, minor, patch);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static final Comparator<Version> COMP = Comparator.comparingInt(Version::getMajor).thenComparing(Version::getMinor).thenComparing(Version::getPatch);

    @Override
    public int compareTo(Version otherVersion) {
        return COMP.compare(this, otherVersion);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public String toString() {
        return String.format("%d.%d.%d", major, minor, patch);
    }
}
