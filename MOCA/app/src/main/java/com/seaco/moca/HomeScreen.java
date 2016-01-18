package com.seaco.moca;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class HomeScreen extends AppCompatActivity {

    static int localeInt = 0;
    static HashMap<String, String> userData;
    static XMLHandlerSession xmlHandlerSession;

    Spinner spinner;
    Button startButton;

    DatePickerDialog.OnDateSetListener dateListener;
    Calendar c;
    static boolean additionPointForEducation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);

        userData = new HashMap<String, String>();
        userData.put("lang", getString(R.string.lang_en_code) );
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
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    createSession();
                    loadNextScreen();
                }
            }
        });

    }



    private void createSession() {
        xmlHandlerSession = new XMLHandlerSession(userData);
        for (HashMap.Entry<String, String> e : userData.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
        xmlHandlerSession.additionalPointForEducation = additionPointForEducation;

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
                    String dob = y + "-" + (m+1) + "-" + (d+1);
                    System.out.println(dob);
                    EditText dateField = (EditText) findViewById(R.id.info_table_field_dob);
                    dateField.setText(dob);
                    userData.put("dob", dob);
                }
            };
            EditText editText = (EditText) findViewById(R.id.info_table_field_dob);
            String todayDate = c.get(Calendar.YEAR) + "-"
                    + (c.get(Calendar.MONTH)+1)
                    + "-" + c.get(Calendar.DAY_OF_MONTH);
            editText.setText(todayDate);
            userData.put("dob", todayDate);

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
        System.out.println(xmlHandlerSession.getFilename());
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
                if (i == 0) {
                    userData.put("education", "None");
                    userData.put("additionalPointForEducation", "false");
                    additionPointForEducation = false;
                } else if (i == 1) {
                    userData.put("education", "Primary");
                    userData.put("additionalPointForEducation", "false");
                    additionPointForEducation = false;
                } else if (i == 2) {
                    userData.put("education", "Secondary");
                    userData.put("additionalPointForEducation", "true");
                    additionPointForEducation = true;
                } else if (i == 3) {
                    userData.put("education", "Tertiary");
                    userData.put("additionalPointForEducation", "true");
                    additionPointForEducation = true;
                } else if (i == 4) {
                    userData.put("education", "Other");
                    userData.put("additionalPointForEducation", "true");
                    additionPointForEducation = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }));
        spinner2.setSelection(0, true);
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
            userData.put("lang", getString(R.string.lang_en_code) );
            setLocale(getString(R.string.lang_en_code));

        } else if (localeInt == 1) {
            userData.put("lang", getString(R.string.lang_ms_code));
            setLocale(getString(R.string.lang_ms_code));

        } else if (localeInt == 2) {
            userData.put("lang", getString(R.string.lang_zh_code));
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
                String folderPath = Environment.getExternalStorageDirectory().getPath() + "/SEACO/MOCA/";
                Snackbar.make(this.startButton, "Path: " + folderPath, Snackbar.LENGTH_INDEFINITE)
                        .setAction("Next", null).show();
                return true;

//            case R.id.action_about:
//                Snackbar.make(this.startButton, getString(R.string.about_app), Snackbar.LENGTH_LONG)
//                        .setAction("Next", null).show();
//                return true;

            case R.id.action_credits:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.screen_menu_credits))
                        .setMessage(getString(R.string.credit_text))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.ok), null)
                        .show();

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
