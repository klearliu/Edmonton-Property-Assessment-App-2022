package com.macewan305;

public class PublicSchool {

    private final String name;
    private final String grades;
    private final String address;
    private final String website;
    private final String phone;
    private final String email;

    public PublicSchool(String schoolName, String gradeRange, String schoolAddress, String site, String phoneNum, String schoolEmail) {

        this.name = schoolName;
        this.grades = gradeRange;
        this.address = schoolAddress;
        this.website = site;
        this.phone = phoneNum;
        this.email = schoolEmail;
    }

    public String getName() {
        return this.name;
    }

    public String getGrades() {
        return this.grades;
    }

    public String getAddress() {
        return this.address;
    }

    public String getContactInfo() {
        return this.website + "\n" +  this.phone + "\n" + this.email;
    }

    public String toString(){
        return ("\nSchool Name: " + name +
                "\nGrades: " + grades +
                "\nAddress: " + address +
                "\nWebsite: " + website +
                "\nPhone Number: " + phone +
                "\nEmail: " + email);
    }
}
