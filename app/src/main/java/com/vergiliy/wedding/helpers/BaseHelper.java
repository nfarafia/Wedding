package com.vergiliy.wedding.helpers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Class with base helper methods
public class BaseHelper {

    private static final SimpleDateFormat CURRENT_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd kk:mm", Locale.getDefault());

    // Parse String to Integer with default value
    public static Integer parseInteger(String string, int defaultValue ) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e ) {
            return defaultValue;
        }
    }

    // Parse String to Double with default value
    public static double parseDouble(String string, int defaultValue ) {
        try {
            return Double.parseDouble(string);
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

    // Get Date from String
    public static Date getDateFromString(String string) {
        if (string != null) {
            try {
                return CURRENT_DATE_FORMAT.parse(string);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    // Get String from Date
    public static String getStringFromDate(Date date) {
        return CURRENT_DATE_FORMAT.format(date);
    }

    // Get Date from string
    public static Date getCurrentDate() {
        return new Date();
    }

    // Hide keyboard after clicking outside
    public static void hideKeyboardWhenLostFocus(final Activity context, View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent event) {
                    hideKeyboard(context, view);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyboardWhenLostFocus(context, innerView);
            }
        }
    }

    // Hide keyboard
    private static void hideKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
