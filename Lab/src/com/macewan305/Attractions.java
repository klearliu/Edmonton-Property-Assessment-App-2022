package com.macewan305;

public class Attractions {

    private final String attName;
    private final String attType;
    private final String attAddress;
    private final String attURL;

    public Attractions(String name, String type, String address, String URL){

        this.attName = name;
        this.attType = type;
        this.attAddress = address;
        this.attURL = URL;

    }
    public String getName() {
        return this.attName;
    }

    public String getType() {
        return this.attType;
    }

    public String getAddress() {
        return this.attAddress;
    }

    public String getURL() {
        return this.attURL;
    }

    public String toString(){
        return ("\nAttraction Name: " + attName +
                "\nAttraction Type: " + attType +
                "\nAddress: " + attAddress +
                "\nWebsite: " + attURL);
    }
}
