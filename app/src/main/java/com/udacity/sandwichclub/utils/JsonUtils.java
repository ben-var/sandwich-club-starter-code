package com.udacity.sandwichclub.utils;

import android.app.PendingIntent;
import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    /**
     * Method used to take as input a json string for a particular sandwich,
     * build a Sandwich model object from the parts of the JSON String, and
     * return the built sandwich.
     * @param json json string for a sandwich type
     * @return Sandwich object built from the JSON string, null if error with input JSON
     */
    public static Sandwich parseSandwichJson(String json) {

        JSONObject sandwichJSON;
        List<String> alsoKnownAsList;
        List<String> ingredientsList;
        Sandwich parsedSandwich;

        // keys that correspond to JSONStrings for each sandwich
        String NAME_KEY =  "name";
        String MAINNAME_KEY = "mainName";
        String OTHERNAMES_KEY = "alsoKnownAs";
        String ORIGIN_KEY = "placeOfOrigin";
        String DESCRIPTION_KEY = "description";
        String IMAGE_KEY = "image";
        String INGREDIENTS_KEY = "ingredients";

        try {
            sandwichJSON = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Format Error: ", e.getMessage());
            return null;
        }

        // parsing contents of JSON string object, and building Sandwich
        try {
            JSONObject nameObject = sandwichJSON.getJSONObject(NAME_KEY);
            String mainName = nameObject.getString(MAINNAME_KEY);

            JSONArray alsoKnownAs = nameObject.getJSONArray(OTHERNAMES_KEY);
            alsoKnownAsList = new ArrayList<>(alsoKnownAs.length());
            for (int i = 0; i < alsoKnownAs.length(); i++) {
                alsoKnownAsList.add( (String) alsoKnownAs.get(i) );
            }

            String origin = sandwichJSON.getString(ORIGIN_KEY);
            String description = sandwichJSON.getString(DESCRIPTION_KEY);
            String imgFile = sandwichJSON.getString(IMAGE_KEY);

            JSONArray ingredients = sandwichJSON.getJSONArray(INGREDIENTS_KEY);
            ingredientsList = new ArrayList<>(ingredients.length());
            for (int i = 0; i < ingredients.length(); i++) {
                ingredientsList.add( (String) ingredients.get(i) );
            }

            parsedSandwich = new Sandwich(mainName, alsoKnownAsList, origin,
                    description, imgFile, ingredientsList);

        } catch (JSONException e) {
            Log.e("Parsing Error: ", e.getMessage());
            return null;
        }

        return parsedSandwich;
    }
}
