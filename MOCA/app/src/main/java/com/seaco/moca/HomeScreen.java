package com.seaco.moca;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class HomeScreen extends AppCompatActivity {

    Locale myLocale;
    static int localeInt = 0;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);

        populateLanguageChoice();
    }

    private void populateLanguageChoice() {
        spinner = (Spinner) findViewById(R.id.main_lang_selector);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_choices, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(localeInt, true);
        spinner.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    localeInt = i;
                    setLocale(getString(R.string.lang_en_code));

                } else if (i == 1) {
                    localeInt = i;
                    setLocale(getString(R.string.lang_ms_code));

                } else if (i == 2) {
                    localeInt = i;
                    setLocale(getString(R.string.lang_zh_code));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    public void setLocale(String lang) {
        System.out.println(lang);
        Resources res = getResources();

        Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang);

        res.updateConfiguration(conf, res.getDisplayMetrics());

        Intent refresh = new Intent(this, HomeScreen.class);
        refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(refresh,0);
        overridePendingTransition(0,0);
        finish();
    }


}
