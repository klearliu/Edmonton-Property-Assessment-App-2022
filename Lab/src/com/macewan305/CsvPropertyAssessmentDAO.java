package com.macewan305;

import java.util.function.Predicate;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

public class CsvPropertyAssessmentDAO implements PropertyAssessmentDAO {


    // Database of all property assessments in CSV file.
    private List<PropertyAssessment> allProperties = new ArrayList<>();

    /**
     *
     * This is the CSV implementation of the PropertyAssessmentDAO.
     * The object is created by passing the filepath to the wanted CSV file, which is then read and
     * the property assessments read will then populate the list allProperties.
     *
     * @param CSVFile: A file path to a CSV file with property assessments.
     * @throws Exception: If the file cannot be read
     */
    public CsvPropertyAssessmentDAO(Path CSVFile) throws Exception{
        BufferedReader lineBuffer = Files.newBufferedReader(CSVFile);
        lineBuffer.readLine(); // Remove headers
        String eachLine;

        while((eachLine = lineBuffer.readLine()) != null){
            String[] propertyData = eachLine.split(",");

            // Force array to be size of 18
            propertyData = Arrays.copyOf(propertyData, 18);

            // Object.toString for 16 and 17 are null checks.
            PropertyAssessment eachProperty = new PropertyAssessment(Integer.parseInt(propertyData[0]),propertyData[1],propertyData[2],
                    propertyData[3],propertyData[4],propertyData[5],propertyData[6],propertyData[7],Integer.parseInt(propertyData[8]),propertyData[9],propertyData[10],propertyData[11],
                    propertyData[12], propertyData[13], propertyData[14],propertyData[15], Objects.toString(propertyData[16], ""),Objects.toString(propertyData[17], ""));


            allProperties.add(eachProperty);
        }
    }

    /**
     *
     * This function is what allows the DAO to filter out wanted properties
     *
     * @param check: A Predicate to base the filter off of
     * @return A list of all the property assessments that pass the Predicate check
     */
    private List<PropertyAssessment> filterProperties(Predicate<PropertyAssessment> check) {

        List<PropertyAssessment> filtered = new ArrayList<>();

        for (PropertyAssessment eachProperty : allProperties){

            if (check.test(eachProperty)){
                filtered.add(eachProperty);
            }

        }

        return filtered;
    }

    /**
     *
     * Finds a specific property assessment
     *
     * @param accountNumber: A potential property assessment integer
     * @return The property assessment if found, or null if not
     */
    @Override
    public PropertyAssessment getAccountNum(int accountNumber){

        for (PropertyAssessment property : allProperties){

            if (property.getAccountNum() == accountNumber) {
                return property;
            }
        }
        return null;
    }

    /**
     *
     * This passes a predicate to check for a specific neighbourhood to filter by.
     *
     * @param nameOfNeighbourhood: A string that contains the name of a neighbourhood to filter by
     * @return A list of all the property assessments that are in the supplied neighbourhood
     */
    @Override
    public List<PropertyAssessment> getNeighbourhood(String nameOfNeighbourhood){

        Predicate<PropertyAssessment> neighbourhoodFilter = property -> property.getNeighbourhoodName().equalsIgnoreCase(nameOfNeighbourhood);

        List<PropertyAssessment> filtered = filterProperties(neighbourhoodFilter);

        return filtered;
    }

    /**
     *
     * This passes a predicate to check for a specific assessment class
     *
     * @param nameOfAssessClass: A string that contains the name of a neighbourhood to filter by
     * @return A list of all the property assessments that had the supplied assessment class
     */
    @Override
    public List<PropertyAssessment> getAssessClass(String nameOfAssessClass){

        Predicate<PropertyAssessment> assessClass = property -> property.getAssess1Name().equalsIgnoreCase(nameOfAssessClass) ||
                property.getAssess2Name().equalsIgnoreCase(nameOfAssessClass) ||
                property.getAssess3Name().equalsIgnoreCase(nameOfAssessClass);

        List<PropertyAssessment> filtered = filterProperties(assessClass);

        return filtered;

    }

    /**
     *
     * This passes a predicate to check for a specific suite to filter by
     *
     * @param nameOfSuite: A string that contains the name of a neighbourhood to filter by
     * @return A list of all the property assessments that have the supplied suite
     */
    @Override
    public List<PropertyAssessment> getSuite(String nameOfSuite) {

        Predicate<PropertyAssessment> suite = property -> property.getSuite().equalsIgnoreCase(nameOfSuite);

        List<PropertyAssessment> filtered = filterProperties(suite);

        return filtered;

    }

    /**
     *
     * This passes a predicate to check for a specific street to filter by.
     *
     * @param streetName: A string that contains the name of the street to filter by
     * @return A list of all the property assessments that are in the supplied street
     */
    @Override
    public List<PropertyAssessment> getStreet(String streetName) {

        Pattern repAve = Pattern.compile("\\bAVE\\b");
        Pattern repST = Pattern.compile("\\bST\\b");

        streetName = repAve.matcher(streetName.toUpperCase()).replaceAll("AVENUE");
        streetName = repST.matcher(streetName.toUpperCase()).replaceAll("STREET");

        // This creates the regex pattern to find (\\b means nothing can come before it)
        // i.e. if 45 ave was input, it would also grab 145 ave, but \\b prevents that)
        Pattern nameToFind = Pattern.compile("\\b" + streetName);

        // The matcher checks to see if the pattern is within that string, with the proper regex
        Predicate<PropertyAssessment> streetCheck = property -> nameToFind.matcher(property.getStreet()).find();

        List<PropertyAssessment> filtered = filterProperties(streetCheck);

        return filtered;
    }

    /**
     *
     * This passes a predicate to check for a specific house number
     *
     * @param houseNum: A string that contains the house number
     * @return A list of all the property assessments that have the supplied house number
     */
    @Override
    public List<PropertyAssessment> getHouse(String houseNum) {

        Predicate<PropertyAssessment> suite = property -> property.getHouseNum().equalsIgnoreCase(houseNum);

        List<PropertyAssessment> filtered = filterProperties(suite);

        return filtered;
    }

    /**
     *
     * This passes a predicate to check for a specific ward
     *
     * @param nameOfWard: A string that contains the wanted ward
     * @return A list of all the property assessments that are in the specified ward
     */
    @Override
    public List<PropertyAssessment> getWard(String nameOfWard) {

        Predicate<PropertyAssessment> wardFilter = property -> property.getWard().equalsIgnoreCase(nameOfWard);

        List<PropertyAssessment> filtered = filterProperties(wardFilter);

        return filtered;
    }

    /**
     *
     * This passes a predicate to check assessment value of each property assessment
     *
     * @param lowerVal: The lower assessment value to search by
     * @param higherVal: The higher assessment value to search by
     * @return A list of all the property assessments that between the supplied values
     */
    @Override
    public List<PropertyAssessment> getRange(int lowerVal, int higherVal) {

        Predicate<PropertyAssessment> rangeFilter = property -> property.getAssessmentVal() >= lowerVal && property.getAssessmentVal() <= higherVal;

        List<PropertyAssessment> filtered = filterProperties(rangeFilter);

        return filtered;

    }

    /**
     *
     * This returns all the properties from the CSV file
     *
     * @return A list of all the property assessments
     */
    @Override
    public List<PropertyAssessment> getAll() {
        return allProperties;
    }


    /**
     *
     * This extracts a certain amount of properties from the total list
     *
     * @param limit: An integer that specifies how many properties to read
     * @return A list of all the property assessments up to the specified limit
     */
    @Override
    public List<PropertyAssessment> getData(int limit){

        List<PropertyAssessment> filtered = new ArrayList<>();

        if (limit <= 0 ){ // 0 or a negative value was passed into the argument
            return filtered;
        }

        int index = 0;

        for (PropertyAssessment property: allProperties) {

            if (index == limit){
                break;
            }

            filtered.add(property);
            index++;

        }
        return filtered;
    }

    /**
     *
     * This extracts a certain amount of properties from the total list
     * after skipped over the first "offset" amount of properties.
     *
     * @param limit: An integer that specifies how many properties to read
     * @param offset: An integer to decide how far into the list to start
     * @return A list of all the property assessments up to the specified limit, after moving offset amount of properties in.
     */
    @Override
    public List<PropertyAssessment> getData(int limit, int offset){

        List<PropertyAssessment> filtered = new ArrayList<>();

        if (limit <= 0 || offset < 0 || offset >= allProperties.size()){ // 0 or a negative value was passed in either argument
            return filtered;
        }

        int index = offset;
        int limitCount = 0;

        while (limitCount < limit && index < allProperties.size()) {

            filtered.add(allProperties.get(index));
            index++;
            limitCount++;

        }
        return filtered;
    }
}
