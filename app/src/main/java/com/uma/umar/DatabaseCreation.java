package com.uma.umar;

import com.google.gson.Gson;
import com.uma.umar.model.Place;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danieh on 9/2/17.
 */

public class DatabaseCreation {

    private static final String SHEET = "/Users/danieh/Downloads/PlacesUmAR-Sheet1.csv";

    private static int floor = -1;

    public static void main(String[] args) {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        List<Place> placeList = new ArrayList<>();

        try {

            br = new BufferedReader(new FileReader(SHEET));
            while ((line = br.readLine()) != null) {

                Place place = extractPlace(line, cvsSplitBy);
                if (place != null) {
                    //System.out.println("Place: " + place);
                    placeList.add(place);
                }
            }

            if (!placeList.isEmpty()) {
                // Convert the object to a JSON string
                Gson gson = new Gson();
                String json = gson.toJson(placeList);
                System.out.println(json);

                System.out.println("========== Script Start ==========");
                StringBuffer stringBuffer = new StringBuffer();
                for (Place place : placeList) {
                    String placeNameTrim = place.getName().replaceAll("[\\s.]", "");
                    // var classroom301 = { .. }
                    stringBuffer.append("var ").append(placeNameTrim).append(" = ").append(gson.toJson(place)).append("\n");

                    //var classroom301Key = databasePlaces.pushData("places/" + informaticaKey + "/", classroom301);
                    stringBuffer.append("var ").append(placeNameTrim).append("Key = databasePlaces.pushData(\"places/\" + informaticaKey + \"/\", ").append(placeNameTrim).append(");\n");

                    //databasePlaces.pushData("places/" + informaticaKey + "/" + classroom301Key + "/profiles/", profileStudentKey);
                    //databasePlaces.pushData("places/" + informaticaKey + "/" + classroom301Key + "/profiles/", profileTeacherKey);
                    if (place.getName_es().contains("Cafetería") || place.getName_es().contains("Salon de Actos")) {
                        stringBuffer.append("databasePlaces.pushData(\"places/\" + informaticaKey + \"/\" + ").append(placeNameTrim).append("Key + \"/profiles/\", profileCongressmanKey);\n");
                        stringBuffer.append("databasePlaces.pushData(\"places/\" + informaticaKey + \"/\" + ").append(placeNameTrim).append("Key + \"/profiles/\", profileCursesKey);\n");

                    } else {
                        stringBuffer.append("databasePlaces.pushData(\"places/\" + informaticaKey + \"/\" + ").append(placeNameTrim).append("Key + \"/profiles/\", profileStudentKey);\n");
                        stringBuffer.append("databasePlaces.pushData(\"places/\" + informaticaKey + \"/\" + ").append(placeNameTrim).append("Key + \"/profiles/\", profileTeacherKey);\n");
                    }
                    stringBuffer.append("\n");
                }
                System.out.println(stringBuffer.toString());
                System.out.println("========== Script End ==========");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private static Place extractPlace(String line, String cvsSplitBy) {
        // use comma as separator
        String[] placeSplit = line.split(cvsSplitBy);

        if (placeSplit.length <= 0)
            return null;

        if (!placeSplit[0].isEmpty()) {
            if (placeSplit[0].toLowerCase().contains("floor")) {
                floor++;
                System.out.println("Floor = " + floor);
            }
        }

        if (placeSplit.length > 1 && !placeSplit[1].isEmpty() && !placeSplit[1].toLowerCase().contains("classroom")) {
            //System.out.println("Place [Name=" + placeSplit[1] + " , latitude=" + placeSplit[2] + " , longitude=" + placeSplit[3] + " , altitude=" + placeSplit[4] + " , image=" + placeSplit[5] + "]");
            return new Place(placeSplit, floor);
        }
        return null;
    }

}
