package com.macewan305;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttractionsDAO {

    private final String endpoint;

    private final HttpClient client;

    public AttractionsDAO() {
        endpoint = "https://data.edmonton.ca/resource/7yt8-7467.csv";
        client = HttpClient.newHttpClient();
    }

    private Attractions createAttractions(String attractionInfo) {

        String[] splitInfo = attractionInfo.split(",");

        for(int i = 0; i < splitInfo.length; i++) {
            splitInfo[i] = splitInfo[i].replaceAll("\"","");
        }

        // Assume everything in splitInfo is of type string.

        Attractions newAttraction = new Attractions(splitInfo[0], splitInfo[6], splitInfo[7], splitInfo[splitInfo.length-2]);

        return newAttraction;


    }

    private List<Attractions> makeCall(String lat, String lon, String radius) {

        String query = endpoint + "?$where=within_circle(geometry_point," + lat + "," + lon + "," + radius + ")";
        List<Attractions> found = new ArrayList<>();

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

            // For whatever reason, whoever is running this API endpoint messed up the location section, and included newlines in it, so every 3 lines is technically
            // for one attraction. This section will fix that.
            String[] fixedResponse = new String[0];
            int fixedIndex = 0;
            for (int y = 1; y < arr.length; y+=3) {

                String oneAttraction = arr[y] + arr[y+1] + arr[y+2];
                fixedResponse = Arrays.copyOf(fixedResponse, fixedResponse.length+1);
                fixedResponse[fixedIndex] = oneAttraction;
                fixedIndex++;
            }

            for (int i = 0; i < fixedResponse.length; i++) {
                found.add(createAttractions(fixedResponse[i]));
            }

            return found;

        } catch (IOException | InterruptedException e){
            return new ArrayList<>();
        }
    }

    public List<Attractions> findAttractions(String lat, String lon, String radius) {

        List<Attractions> attractionsFound = makeCall(lat, lon, radius);

        return attractionsFound;
    }

}

