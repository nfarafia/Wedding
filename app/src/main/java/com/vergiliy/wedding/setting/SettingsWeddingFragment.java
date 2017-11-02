package com.vergiliy.wedding.setting;

import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;

import com.vergiliy.wedding.Language;
import com.vergiliy.wedding.R;

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
        ListPreference currencyField = (ListPreference) findPreference("currency");
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
                System.out.println("Locale " + locale + " not found; " + e.getMessage());
            }
        }

        // Sort collections
        Collections.sort(entries);
        Collections.sort(values);

        // Add new data
        listPreference.setEntries(entries.toArray(new CharSequence[entries.size()]));
        listPreference.setEntryValues(values.toArray(new CharSequence[values.size()]));

        String text = sharedPreferences.getString("currency", null);
        Currency currency = Language.getLocalCurrency();
        if (text == null && currency != null) {
            listPreference.setValueIndex(values.indexOf(currency.getCurrencyCode()));
        }
    }
}