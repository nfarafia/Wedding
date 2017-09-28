package com.vergiliy.wedding.helpers;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// Class with base helper methods
public class BaseHelper {

    // Parse String to Integer with default value
    public static Integer parseInteger(String string, int defaultValue ) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e ) {
            return defaultValue;
        }
    }

    // Read file from Assets to String
    static String readFromAssets(Context context, String filename) {
        InputStream myInput;
        StringBuilder string = null;
        BufferedReader reader = null;

        // Get file
        try {
            myInput = context.getAssets().open(filename);

            if (myInput == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(myInput));

            // Do reading, usually loop until end of file reading
            string = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                string.append(line); // Process line
                line = reader.readLine();
            }
        } catch(IOException ex){
            Log.e("IOException", "BaseHelper -> readFromAssets (read): " + ex.getMessage());
        } finally {
            try{
                if (reader != null) {
                    reader.close();
                }
            } catch(IOException ex){
                Log.e("IOException", "BaseHelper -> readFromAssets (close): " + ex.getMessage());
            }
        }

        if (string != null)
            return string.toString();

        return null;
    }
}
