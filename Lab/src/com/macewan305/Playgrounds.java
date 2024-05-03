package com.macewan305;

public class Playgrounds {
    private final String name;
    private final String address;
    private final String surface;
    private final String access;

    public Playgrounds(String playName, String playAddress, String playSurface, String accessibility) {
        this.name = playName;
        this.address = playAddress;
        this.surface = playSurface;
        this.access = accessibility;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getSurface() {
        return this.surface;
    }

    public String getAccess() {
        return this.access;
    }

    public String toString(){
        return ("\nPlayground Name: " + name +
                "\nSurface Type: " + surface +
                "\nAddress: " + address +
                "\nAccessibility: " + access);
    }
}
