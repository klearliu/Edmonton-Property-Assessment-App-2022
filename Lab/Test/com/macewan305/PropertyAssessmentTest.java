package com.macewan305;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PropertyAssessmentTest {
    private PropertyAssessment property1;
    private PropertyAssessment property2;
    private PropertyAssessment property3;

    private PropertyAssessment property4;

    @BeforeEach
    void setup(){
        property1 = new PropertyAssessment(101000 ,"3421","69230",
                "Test St.","Y","3515","Testing Neighbourhood","Honda Civic Ward",519603,"59.29503","102.352","POINT (102.352352, 59.29503319)",
                "100", "", "","RESIDENTIAL", "" ,"");

        property2 = new PropertyAssessment(101500 ,"","420",
                "Boulevard St.","N","1058","Probability Neighbourhood","Jaguar SVR F Type Ward",1572390,"109.5932049","132.3362","POINT (132.3362435, 109.5932049582)",
                "95", "5", "","RESIDENTIAL", "OTHER" ,"");

        property3 = new PropertyAssessment(101000 ,"3421","69230",
                "Test St.","Y","3515","Testing Neighbourhood","Honda Civic Ward",519603,"59.29503","102.352","POINT (102.352352, 59.29503319)",
                "100", "", "","RESIDENTIAL", "" ,"");

        property4 = new PropertyAssessment(10130, "", "420",
                "Avenue St.", "N", "10528", "Potential Neighbourhood", "Jaguar SVR F Type Ward", 1390, "109.5939", "13.3362", "POINT (13.3362434, 109.593951)",
                "90", "5", "5", "RESIDENTIAL", "OTHER", "ASSESSMENT NAME 3");
    }

    @Test
    void accountNum() {
        assertEquals(101000, property1.getAccountNum());
    }

    @Test
    void getSuite() {
        assertEquals("3421", property1.getSuite());
    }

    @Test
    void getHouseNum() {
        assertEquals("69230", property1.getHouseNum());
    }

    @Test
    void getStreet() {
        assertEquals("Test St.", property1.getStreet());
    }

    @Test
    void garage() {
        assertEquals("Y", property1.getGarage());
    }

    @Test
    void neighbourhoodID() {
        assertEquals("3515", property1.getNeighbourhoodID());
    }

    @Test
    void neighbourhoodName() {
        assertEquals("Testing Neighbourhood", property1.getNeighbourhoodName());
    }

    @Test
    void getWard() {
        assertEquals("Honda Civic Ward", property1.getWard());
    }

    @Test
    void assessmentVal() {
        assertEquals(519603, property1.getAssessmentVal());
    }

    @Test
    void latitude() {
        assertEquals("59.29503", property1.getLatitude());
    }

    @Test
    void longitude() {
        assertEquals("102.352", property1.getLongitude());
    }

    @Test
    void point() {
        assertEquals("POINT (102.352352, 59.29503319)",property1.getPoint());
    }

    @Test
    void assess1P() {
        assertEquals("100", property1.getAssess1P());
    }

    @Test
    void assess2P() {
        assertEquals("", property1.getAssess2P());
    }

    @Test
    void assess3P() {
        assertEquals("", property1.getAssess3P());
    }

    @Test
    void assess1Name() {
        assertEquals("RESIDENTIAL", property1.getAssess1Name());
    }

    @Test
    void assess2Name() {
        assertEquals("", property1.getAssess2Name());
    }

    @Test
    void assess3Name() {
        assertEquals("", property1.getAssess3Name());
    }

    @Test
    void address() {
        assertEquals("3421 69230 Test St.", property1.getAddress());
    }

    @Test
    void location() {
        assertEquals("(102.352, 59.29503)", property1.getLocation());
    }

    @Test
    void allClasses() {
        assertEquals("[RESIDENTIAL 100%]", property1.getAllClasses());
        assertEquals("[RESIDENTIAL 95%, OTHER 5%]", property2.getAllClasses());
        assertEquals("[RESIDENTIAL 90%, OTHER 5%, ASSESSMENT NAME 3 5%]", property4.getAllClasses());
    }

    @Test
    void area() {
        assertEquals("Testing Neighbourhood (Honda Civic Ward)", property1.Area());
    }

    @Test
    void compareAssessValue() {
        assertEquals(true, property1.compareAssessValue(property1));
        assertEquals(false, property1.compareAssessValue(property4));
        assertEquals(true, property4.compareAssessValue(property1));
        assertEquals(false, property1.compareAssessValue(null));

    }

    @Test
    void testToString() {
        assertEquals("Account Number: 101000\n" +
                "Suite: 3421\n" +
                "House Number: 69230\n" +
                "Street Name: Test St.\n" +
                "Garage: Y\n" +
                "Neighbourhood ID: 3515\n" +
                "Neighbourhood Name: Testing Neighbourhood\n" +
                "Ward: Honda Civic Ward\n" +
                "Assessment Value: 519603\n" +
                "Latitude: 59.29503\n" +
                "Longitude: 102.352\n" +
                "Point: POINT (102.352352, 59.29503319)\n" +
                "Assessment 1 Name and Percent: RESIDENTIAL100%\n" +
                "Assessment 2 Name and Percent: %\n" +
                "Assessment 3 Name and Percent: %" , property1.toString());
    }

    @Test
    void testEquals() {
        assertEquals(true, property1.equals(property1)); // Equal self
        assertEquals(false, property1.equals(null)); // Is it null
        assertEquals(false, property1.equals(property2)); // different properties
        assertEquals(true, property1.equals(property3)); // Equals another object with the same values
    }

    @Test
    void testHashCode() {
        assertEquals(816393996, property1.hashCode());
        assertEquals(-1955518990, property2.hashCode());
    }
}