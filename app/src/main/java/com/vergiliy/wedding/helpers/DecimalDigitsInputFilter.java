package com.vergiliy.wedding.helpers;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Input Filter for digits (20 digits before zero and 2 digits after zero)
public class DecimalDigitsInputFilter implements InputFilter {

    private Pattern pattern_1, pattern_2;

    public DecimalDigitsInputFilter() {
        setPattern(20, 2);
    }

    public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
        setPattern(digitsBeforeZero, digitsAfterZero);
    }

    private void setPattern(int digitsBeforeZero, int digitsAfterZero) {
        pattern_1 = Pattern.compile("[0-9]{0," + digitsBeforeZero + "}+((\\.[0-9]{0,"
                + digitsAfterZero + "})?)||(\\.)?");
        pattern_2 = Pattern.compile("[0-9]{0," + digitsBeforeZero + "}+((\\,[0-9]{0,"
                + digitsAfterZero + "})?)||(\\,)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

        // Get full string
        String spanned = dest.toString();
        String string = spanned.substring(0, dstart) +  source.subSequence(start, end) + spanned.substring(dend);

        Matcher matcher_1 = pattern_1.matcher(string);
        Matcher matcher_2 = pattern_2.matcher(string);


        if (!matcher_1.matches() && !matcher_2.matches()) {
            return "";
        }

        return null;
    }
}