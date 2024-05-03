package com.macewan305;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaygroundsDAO {

    private final String endpoint;

    private final HttpClient client;

    public PlaygroundsDAO() {
        endpoint = "https://data.edmonton.ca/resource/9nqb-w48x.csv";
        client = HttpClient.newHttpClient();
    }

    private Playgrounds createPlayground(String playgroundInfo) {

        String[] splitInfo = playgroundInfo.split(",");

        for(int i = 0; i < splitInfo.length; i++) {
            splitInfo[i] = splitInfo[i].replaceAll("\"","");
        }

        // Assume everything in splitInfo is of type string.
        Playgrounds newPlayground = new Playgrounds(splitInfo[2], splitInfo[3], splitInfo[5], splitInfo[6]);

        return newPlayground;


    }

    private List<Playgrounds> makeCall(String lat, String lon, String radius) {

        String query = endpoint + "?$where=within_circle(geometry_point," + lat + "," + lon + "," + radius + ")";
        List<Playgrounds> found = new ArrayList<>();

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

            // The API has newlines in some sections, which breaks up the call into 3 parts. This is also an issue in the attractions API
            String[] fixedResponse = new String[0];
            int fixedIndex = 0;
            for (int y = 1; y < arr.length; y+=3) {

                String oneAttraction = arr[y] + arr[y+1] + arr[y+2];
                fixedResponse = Arrays.copyOf(fixedResponse, fixedResponse.length+1);
                fixedResponse[fixedIndex] = oneAttraction;
                fixedIndex++;
            }

            for (int i = 1; i < fixedResponse.length; i++) {
                found.add(createPlayground(fixedResponse[i]));
            }

            return found;

        } catch (IOException | InterruptedException e){
            return new ArrayList<>();
        }
    }

    public List<Playgrounds> findPlaygrounds(String lat, String lon, String radius) {

        List<Playgrounds> playgroundsFound = makeCall(lat, lon, radius);

        return playgroundsFound;
    }
}
