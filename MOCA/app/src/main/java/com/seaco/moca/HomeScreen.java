package com.seaco.moca;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class HomeScreen extends AppCompatActivity {

    static int localeInt = 0;
    Spinner spinner;
    Button startButton;

    static HashMap<String, String> userData;
    DatePickerDialog.OnDateSetListener dateListener;
    Calendar c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);

        userData = new HashMap<String, String>();
        c = Calendar.getInstance();

        populateLanguageChoice();
        populateFieldChoices();
        populateDatePicker();

        startButton = (Button) findViewById(R.id.main_start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    try {
                        Thread.sleep(300);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    createSessionFile();
                    loadNextScreen();
                }
            }
        });
    }

    private void createSessionFile() {

        String currentDateTime = c.get(Calendar.YEAR) + "/"
                + (c.get(Calendar.MONTH)+1)
                + "/" + c.get(Calendar.DAY_OF_MONTH)
                + "-" + c.get(Calendar.HOUR_OF_DAY)
                + ":" + c.get(Calendar.MINUTE);
        userData.put("testDateTime", currentDateTime);

        String UUID = "";

        char[] ints = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        Random randomizer = new Random();
        for (int i=0; i<16; i++) {
            char random = ints[randomizer.nextInt(ints.length)];
            UUID += random;
        }
        System.out.println(UUID + " " + userData.values());

    }

    private boolean validateForm() {
        EditText editText = (EditText) findViewById(R.id.info_table_field_name);
        String nameText = editText.getText().toString();
        if (nameText.length() == 0) {
            userData.put("name", "Anonymous");
        } else {
            userData.put("name",nameText);
        }



        return true;
    }

    private void populateDatePicker() {
        {
            dateListener = new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker picker, int y, int m, int d) {
                    String dob = y + "/" + (m+1) + "/" + (d+1);
                    System.out.println(dob);
                    EditText dateField = (EditText) findViewById(R.id.info_table_field_dob);
                    dateField.setText(dob);
                    userData.put("DateOfBirth", dob);
                }
            };
            EditText editText = (EditText) findViewById(R.id.info_table_field_dob);
            String todayDate = c.get(Calendar.YEAR) + "/"
                    + (c.get(Calendar.MONTH)+1)
                    + "/" + c.get(Calendar.DAY_OF_MONTH);
            editText.setText(todayDate);
            userData.put("DateOfBirth", todayDate);

            editText.setInputType(InputType.TYPE_NULL);
            editText.setFocusable(false);
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog dpd = new DatePickerDialog(HomeScreen.this, dateListener, 2016, 1, 1);
                    dpd.getDatePicker().setCalendarViewShown(false);
                    dpd.show();
                }


            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_screen_menu, menu);
        return true;
    }

    private void loadNextScreen() {
        Intent intent = new Intent(this, InfoScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 0);
        overridePendingTransition(0, 0);
        finish();
    }


    private void populateFieldChoices() {
        Spinner spinner = (Spinner) findViewById(R.id.info_table_field_sex);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_choices, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    userData.put("sex", "M");
                } else if (i == 1) {
                    userData.put("sex", "F");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }));
        spinner.setSelection(0, true);

        Spinner spinner2 = (Spinner) findViewById(R.id.info_table_field_education);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.education_choices, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0) {
                    userData.put("education","None");
                } else if (i == 1) {
                    userData.put("education","Primary");
                }else if (i == 2) {
                    userData.put("education","Secondary");
                }else if (i == 3) {
                    userData.put("education","Tertiary");
                }else if (i == 4) {
                    userData.put("education","Other");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }));
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
                localeInt = i;
                setLocaleHelper();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }));

    }

    private void setLocaleHelper() {
        if (localeInt == 0) {
            setLocale(getString(R.string.lang_en_code));

        } else if (localeInt == 1) {
            setLocale(getString(R.string.lang_ms_code));

        } else if (localeInt == 2) {
            setLocale(getString(R.string.lang_zh_code));

        }
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
        startActivityForResult(refresh, 0);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view:
                Snackbar.make(this.startButton, "Not yet implemented", Snackbar.LENGTH_LONG)
                        .setAction("Next", null).show();


                return true;
            case R.id.action_about:
                Snackbar.make(this.startButton, getString(R.string.about_app), Snackbar.LENGTH_LONG)
                        .setAction("Next", null).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocaleHelper();
    }
}
