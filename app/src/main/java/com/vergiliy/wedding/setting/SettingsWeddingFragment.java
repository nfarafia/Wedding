package com.vergiliy.wedding.setting;

import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.text.InputFilter;
import android.util.Log;

import com.vergiliy.wedding.BaseLocale;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.helpers.DecimalDigitsInputFilter;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class SettingsWeddingFragment extends BasePreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_wedding);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get fields
        ListPreference currencyField = (ListPreference) findPreference("currency");
        EditTextPreference budgetField = (EditTextPreference) findPreference("budget");

        // Set filter to budgetField (20 digits before zero and 2 digits after zero)
        DecimalFormatSymbols decimal = DecimalFormatSymbols.getInstance(Locale.getDefault());
        budgetField.getEditText().setFilters(new InputFilter[] {new DecimalDigitsInputFilter()});

        // Set data to currencyField
        setListPreferenceData(currencyField);
    }

    // Set data to ListPreference
    private void setListPreferenceData(ListPreference listPreference) {
        List<String> entries = new ArrayList<>();
        List<String> values = new ArrayList<>();

        // Get available currencies
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                Currency currency = Currency.getInstance(locale);
                if (currency != null) {
                    String currencyCode = currency.getCurrencyCode();

                    if (values.indexOf(currencyCode) == -1) {
                        String entry;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            entry = currencyCode + " (" + currency.getDisplayName() + ")";
                        } else {
                            entry = currencyCode;
                        }
                        entries.add(entry);
                        values.add(currencyCode);
                    }
                }
            } catch (Exception e) {
                Log.e("SettingsWeddingFragment", "Locale \"" + locale + "\" not found; " + e.getMessage());
            }
        }

        // Sort collections
        Collections.sort(entries);
        Collections.sort(values);

        // Add new data
        listPreference.setEntries(entries.toArray(new CharSequence[entries.size()]));
        listPreference.setEntryValues(values.toArray(new CharSequence[values.size()]));

        String text = sharedPreferences.getString("currency", null);
        Currency currency = BaseLocale.getLocalCurrency();
        if (text == null && currency != null) {
            listPreference.setValueIndex(values.indexOf(currency.getCurrencyCode()));
        }
    }
}