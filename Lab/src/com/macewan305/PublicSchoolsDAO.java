package com.macewan305;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PublicSchoolsDAO {
    private final String endpoint;

    private final HttpClient client;

    public PublicSchoolsDAO() {
        endpoint = "https://data.edmonton.ca/resource/996c-239n.csv";
        client = HttpClient.newHttpClient();
    }

    private PublicSchool createPublicSchool(String schoolInfo) {

        String[] splitInfo = schoolInfo.split(",");

        for(int i = 0; i < splitInfo.length; i++) {
            splitInfo[i] = splitInfo[i].replaceAll("\"","");
        }

        splitInfo = Arrays.copyOf(splitInfo, 17);   // Ensure it has a space of 17 to prevent going oob

        // Assume everything in splitInfo is of type string.

        PublicSchool newSchool = new PublicSchool(splitInfo[2], splitInfo[4], splitInfo[5], splitInfo[9], splitInfo[10], splitInfo[11]);

        return newSchool;


    }

    private List<PublicSchool> makeCall(String lat, String lon, String radius) {

        String query = endpoint + "?$where=within_circle(geometry_point," + lat + "," + lon + "," + radius + ")";
        List<PublicSchool> found = new ArrayList<>();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query))
                .GET()
                .build();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String[] arr = response.body().split("\n");

            if (arr.length == 1) {     // Return if there was no retrieved schools
                return new ArrayList<>();
            }

            for (int i = 1; i < arr.length; i++) {
                found.add(createPublicSchool(arr[i]));
            }

            return found;

        } catch (IOException | InterruptedException e){
            return new ArrayList<>();
        }
    }

    public List<PublicSchool> findSchools(String lat, String lon, String radius) {

        List<PublicSchool> schoolsFound = makeCall(lat, lon, radius);

        return schoolsFound;
    }

}
