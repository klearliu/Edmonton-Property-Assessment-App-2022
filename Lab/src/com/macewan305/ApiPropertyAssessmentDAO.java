package com.macewan305;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class ApiPropertyAssessmentDAO implements PropertyAssessmentDAO{

    // This is the beginning of any call to the api + the client
    private final String endpoint;
    private final HttpClient client;

    // This is a cache for residential, will record for residential once it is made.
    // This only caches once a residential call is made for the first time
    private List<PropertyAssessment> residentialProps = new ArrayList<>();
    private boolean residentCached = false;

    private int limit = 75000;
    private int offset = 0;

    /**
     *
     * This is the API implementation of the PropertyAssessmentDAO.
     * The object creation simply makes a new http client and stores the string endpoint
     */
    public ApiPropertyAssessmentDAO() {
        client = HttpClient.newHttpClient();
        endpoint = "https://data.edmonton.ca/resource/q7d6-ambg.csv";
    }

    /**
     *
     * This function creates property assessments using info read from the api calls.
     *
     * @param propertyInfo: A string that hasn't been modified at all, but holds the information to make a property
     * @return A newly made property assessment
     */
    private PropertyAssessment createProperty(String propertyInfo) {

        String[] splitInfo = propertyInfo.split(",");

        for(int i = 0; i < splitInfo.length; i++) {
            splitInfo[i] = splitInfo[i].replaceAll("\"","");
        }

        splitInfo = Arrays.copyOf(splitInfo, 18);   // Ensure it has a space of 18 to prevent going oob

        // Assume everything in splitInfo is of type string. i.e. we must convert all numbers to actual ints if PropertyAssessment object demands it

        PropertyAssessment newProperty = new PropertyAssessment(Integer.parseInt(splitInfo[0]), splitInfo[1], splitInfo[2],
                splitInfo[3], splitInfo[4], splitInfo[5], splitInfo[6], splitInfo[7], Integer.parseInt(splitInfo[8]), splitInfo[9], splitInfo[10], splitInfo[11],
                splitInfo[12], splitInfo[13], splitInfo[14], splitInfo[15], Objects.toString(splitInfo[16], ""), Objects.toString(splitInfo[17], ""));

        return newProperty;

    }

    /**
     *
     * This function is what makes the actual API call, using provided information from other functions
     *
     * @param queryType A string that defines what kind of search we are requesting (i.e. neighbourhood name, ward name, account number etc.)
     * @param search: The filter by which we are searching
     * @return A list of property assessments are returned that follow the passed filter type.
     * @throws UnsupportedEncodingException This error occurs if the specified character set cannot encode a character
     */

    private List<PropertyAssessment> filter (String queryType, String search) throws UnsupportedEncodingException {

        String query = endpoint + "?$limit=" + limit + "&$offset=" + offset + queryType + URLEncoder.encode(search, StandardCharsets.UTF_8);
        List<PropertyAssessment> filter = new ArrayList<>();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query))
                .GET()
                .build();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String[] arr = response.body().split("\n");

            if (arr.length == 1) {     // Return if there was no retrieved property
                return null;
            }

            for (int i = 1; i < arr.length; i++) {
                filter.add(createProperty(arr[i]));
            }

            return filter;

        } catch (IOException | InterruptedException e){
            return null;
        }

    }

    /**
     * This short function resets the limit and offset values after each filter call
     */
    private void reset() {
        limit = 75000;
        offset = 0;

    }

    /**
     *
     * This function retrieves a specific property assessment by its account number
     *
     * @param accountNumber: An integer to find the property with the supplied account number
     * @return A single property assessment that corresponds to the account number, or null if nothing is found
     * @throws UnsupportedEncodingException This error occurs if the specified character set cannot encode a character
     */
    @Override
    public PropertyAssessment getAccountNum(int accountNumber) throws UnsupportedEncodingException {

        List<PropertyAssessment> filtered = filter("&account_number=", Integer.toString(accountNumber));

        if (filtered != null) {
            return filtered.get(0);
        } else {
            return null;
        }
    }

    /**
     *
     * Creates the proper query to find properties in a neighbourhood
     *
     * @param nameOfNeighbourhood: Name of the neighbourhood to filter by
     * @return A list of property assessments that are in the specified neighbourhood
     * @throws UnsupportedEncodingException This error occurs if the specified character set cannot encode a character
     */
    @Override
    public List<PropertyAssessment> getNeighbourhood(String nameOfNeighbourhood) throws UnsupportedEncodingException {

        List<PropertyAssessment> neighProps = new ArrayList<>();
        List<PropertyAssessment> obtained;

        while ((obtained = filter("&neighbourhood=",nameOfNeighbourhood.toUpperCase())) != null) {
            neighProps.addAll(obtained);
            offset += limit;
        }

        this.reset();

        return neighProps;

    }

    /**
     *
     * Creates a query to find properties with a specific suite
     *
     * @param nameOfSuite: Name of the suite to filter by
     * @return A list of properties that have the suite number
     * @throws UnsupportedEncodingException This error occurs if the specified character set cannot encode a character
     */
    @Override
    public List<PropertyAssessment> getSuite(String nameOfSuite) throws UnsupportedEncodingException{
        List<PropertyAssessment> suiteProps = new ArrayList<>();
        List<PropertyAssessment> obtained;

        while ((obtained = filter("&suite=", nameOfSuite.toUpperCase())) != null) {
            suiteProps.addAll(obtained);
            offset += limit;
        }

        this.reset();

        return suiteProps;
    }

    /**
     *
     * Creates a query to find properties with a street name.
     * Regex is used to prevent false positives (i.e. searching for 49 st, and getting 149 st values.)
     *
     * @param streetName : Name of the street to filter by
     * @return A list of properties that are on the given street
     * @throws UnsupportedEncodingException This error occurs if the specified character set cannot encode a character
     */
    @Override
    public List<PropertyAssessment> getStreet(String streetName) throws UnsupportedEncodingException {

        List<PropertyAssessment> allStreetProps = new ArrayList<>();
        List<PropertyAssessment> obtained;
        boolean givenDirection = false;
        String directions[] = {"NW", "SW", "NE", "SE"};


        // Ensure it is in proper form
        Pattern repAve = Pattern.compile("\\bAVE\\b");
        Pattern repST = Pattern.compile("\\bST\\b");

        streetName = repAve.matcher(streetName.toUpperCase()).replaceAll("AVENUE");
        streetName = repST.matcher(streetName.toUpperCase()).replaceAll("STREET");


        // Since we cannot guarantee a direction is entered (i.e. NW), as well as which direction is wanted, we have to obtain all directions

        // Check to see if a direction was given
        for (String eachDir : directions) {
            if (streetName.endsWith(eachDir)) {
                givenDirection = true;
                break;
            }
        }

        if (givenDirection) {
            while ((obtained = filter("&street_name=", streetName.toUpperCase())) != null) {
                allStreetProps.addAll(obtained);
                offset += limit;
            }

        } else { // Need to obtain all directions

            for (String eachDir : directions) {
                while ((obtained = filter("&street_name=", streetName.toUpperCase() + " " + eachDir)) != null) {
                    allStreetProps.addAll(obtained);
                    offset += limit;
                }
            }

        }
        this.reset();

        return allStreetProps;
    }

    /**
     *
     * Creates a query to find properties with a specific house number
     *
     * @param houseNum: house number to filter by
     * @return A list of properties that have the suite number
     * @throws UnsupportedEncodingException This error occurs if the specified character set cannot encode a character
     */
    @Override
    public List<PropertyAssessment> getHouse(String houseNum) throws UnsupportedEncodingException {

        List<PropertyAssessment> houseNumProps = new ArrayList<>();
        List<PropertyAssessment> obtained;

        while ((obtained = filter("&house_number=", houseNum.toUpperCase())) != null) {
            houseNumProps.addAll(obtained);
            offset += limit;
        }

        this.reset();

        return houseNumProps;
    }

    /**
     *
     * Creates a query to find properties with a certain assessment class
     * This query checks for class 1, 2, and 3.
     *
     * @param nameOfAssessClass: Name of the assessment class to filter by
     * @return A list of properties that have the given assessment class
     * @throws UnsupportedEncodingException This error occurs if the specified character set cannot encode a character
     */
    @Override
    public List<PropertyAssessment> getAssessClass(String nameOfAssessClass) throws UnsupportedEncodingException {

        List<PropertyAssessment> classProps;
        List<PropertyAssessment> allProps = new ArrayList<>();

        if(residentCached && nameOfAssessClass.compareTo("RESIDENTIAL") == 0) {
            return residentialProps;
        }

        String queryType = "&$where=mill_class_1='" + nameOfAssessClass.toUpperCase() + "' OR " + "mill_class_2='" + nameOfAssessClass.toUpperCase() + "' OR " + "mill_class_3='" + nameOfAssessClass.toUpperCase() + "'";

        queryType = queryType.replace("'","%27");
        queryType = queryType.replace(" ", "%20");

        while((classProps = filter(queryType, "")) != null) {
            allProps.addAll(classProps);

            offset+=limit;
        }

        if (nameOfAssessClass.compareTo("RESIDENTIAL") == 0) {
            residentialProps = List.copyOf(allProps);
            residentCached = true;
        }
        this.reset();
        return allProps;
    }

    /**
     *
     * Creates a query to find properties in a ward
     *
     * @param nameOfWard: Name of the ward to filter by
     * @return A list of properties that are in a specific ward
     * @throws UnsupportedEncodingException This error occurs if the specified character set cannot encode a character
     */
    @Override
    public List<PropertyAssessment> getWard(String nameOfWard) throws UnsupportedEncodingException {

        List<PropertyAssessment> wardProps = new ArrayList<>();
        List<PropertyAssessment> obtained;

        while ((obtained = filter("&ward=",nameOfWard)) != null) {
            wardProps.addAll(obtained);
            offset += limit;
        }

        this.reset();

        return wardProps;
    }

    /**
     *
     * Creates a query to find properties in between the supplied values
     *
     * @param lowerVal: The lowest assessment to check for
     * @param higherVal: the maximum assessment to check for
     * @return A list of the property assessments between the lower and higher value assessments
     * @throws UnsupportedEncodingException This error occurs if the specified character set cannot encode a character
     */
    @Override
    public List<PropertyAssessment> getRange(int lowerVal, int higherVal) throws UnsupportedEncodingException {

        List<PropertyAssessment> inBetween = new ArrayList<>();
        List<PropertyAssessment> eachQuery;

        String queryType = "&$where=assessed_value between '"+ lowerVal + "' and " + "'" + higherVal + "'";
        queryType = queryType.replace("'","%27");
        queryType = queryType.replace(" ", "+");

        while ((eachQuery = filter(queryType,"")) != null) {
            inBetween.addAll(eachQuery);
            offset += limit;
        }

        this.reset();

        return inBetween;
    }

    /**
     *
     * Calls all api until it has read all the properties
     *
     * @return A list of all the properties in the api
     * @throws UnsupportedEncodingException This error occurs if the specified character set cannot encode a character
     */
    @Override
    public List<PropertyAssessment> getAll() throws UnsupportedEncodingException {

        List<PropertyAssessment> allProps = new ArrayList<>();
        List<PropertyAssessment> obtained;

        while ((obtained = filter("","")) != null) {
            allProps.addAll(obtained);
            offset += limit;
        }

        this.reset();

        return allProps;
    }

    /**
     *
     * Creates a query to find a certain amount of properties
     *
     * @param limit: Amount of properties to get
     * @return A list of properties
     * @throws UnsupportedEncodingException This error occurs if the specified character set cannot encode a character
     */
    @Override
    public List<PropertyAssessment> getData(int limit) throws UnsupportedEncodingException {
        this.limit = limit;
        List<PropertyAssessment> data = filter("", "");
        this.reset();
        return data;
    }

    /**
     *
     * Creates a query to find a certain amount of properties, after going offset deep into the api.
     *
     * @param limit: Amount of properties to obtain
     * @param newOffset: how far into the api to go before reading
     * @return A list of properties
     * @throws UnsupportedEncodingException This error occurs if the specified character set cannot encode a character
     */
    @Override
    public List<PropertyAssessment> getData(int limit, int newOffset) throws UnsupportedEncodingException {

        this.limit = limit;
        offset = newOffset;
        List<PropertyAssessment> data = filter("","");
        this.reset();
        return data;
    }

}
