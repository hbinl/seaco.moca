package com.seaco.moca;

import android.app.AlertDialog;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InfoScreen extends AppCompatActivity {

//    FragmentManager fragmentManager;
    RelativeLayout container;
    LayoutInflater layoutInflater;
    static int pageIndex;
    static Integer[] pageArray;
    static String[] pageNameArray;
    FloatingActionButton fab_forward;
    FloatingActionButton fab_back;
    Menu menu_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab_back = (FloatingActionButton) findViewById(R.id.fab_back);
        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceedToPreviousScreen();
            }
        });

        fab_forward = (FloatingActionButton) findViewById(R.id.fab_forward);
        fab_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageIndex != 0 && pageIndex < (pageArray.length-2)) {
                    Snackbar.make(view, R.string.saved_message, Snackbar.LENGTH_SHORT)
                            .setAction("Next", null).show();
                }

                proceedToNextScreen();
            }
        });

        //fab_back = (FloatingActionButton) findViewById(R.id.fab_back);
        fab_back.setVisibility(View.INVISIBLE);

        initializePages();

        container = (RelativeLayout) findViewById(R.id.moca_tests_container);
        View child = getLayoutInflater().inflate(pageArray[pageIndex],null);
        container.addView(child);


    }



    private void initializePages() {
        pageIndex = 0;
        pageArray = new Integer[] {
                R.layout.moca_overview,
                R.layout.moca_visuo_node,
                R.layout.moca_visuo_cube,
                R.layout.moca_visuo_clock,
                R.layout.moca_naming,
                R.layout.moca_memory,
                R.layout.moca_attention_digits,
                R.layout.moca_attention_letters,
                R.layout.moca_attention_subtraction,
                R.layout.moca_language_repeat,
                R.layout.moca_language_fluency,
                R.layout.moca_abstraction,
                R.layout.moca_delayed_recall,
                R.layout.moca_orientation,
                R.layout.moca_summary
        };
        pageNameArray = getResources().getStringArray(R.array.pageNameArray);
    }

    private void proceedToScreenLayout(int index) {
        updateTestName(index);
        int screen = pageArray[index];
        container.removeAllViews();
        View child = getLayoutInflater().inflate(screen ,null);
        container.addView(child);
    }

    private void updateTestName(int id) {
        TextView tv = (TextView) findViewById(R.id.survey_label);
        String ind = "";
        System.out.println(pageIndex);
        if (pageIndex > 0 && pageIndex <= 13) {
            ind = (pageIndex) + "/13 - ";
            System.out.println(">>>>>>>SADA" + pageIndex);
        }

        tv.setText(ind + pageNameArray[id]);
    }

    private void proceedToNextScreen() {
        if (pageIndex + 1 < pageArray.length) {
            pageIndex += 1;
            proceedToScreenLayout(pageIndex);
            fab_back.setVisibility(View.VISIBLE);
            if (pageIndex == pageArray.length - 1) {
                summaryScreen();
            }
        }



    }

    private void summaryScreen() {
        fab_forward.setVisibility(View.INVISIBLE);
        fab_back.setVisibility(View.INVISIBLE);
        Snackbar.make(fab_back, R.string.summary_screen_message, Snackbar.LENGTH_SHORT)
                .setAction("Next", null).show();

        menu_ref.findItem(R.id.action_quit).setVisible(false);
        menu_ref.findItem(R.id.action_finish).setVisible(true);


    }

    private void proceedToPreviousScreen() {
        if (pageIndex - 1 >= 0) {
            pageIndex -= 1;
            proceedToScreenLayout(pageIndex);
            fab_forward.setVisibility(View.VISIBLE);

            if (pageIndex == 0) {
                fab_back.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.screen_menu, menu);
        menu.findItem(R.id.action_finish).setVisible(false);
        menu_ref = menu;
        return true;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    private void goBackHome() {
        finish();
        Intent intent = new Intent(this, HomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 0);
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_quit:
                // User chose the "Settings" item, show the app settings UI...
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.confirm_exit_title))
                        .setMessage(getString(R.string.confirm_exit_message))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                goBackHome();
                            }
                        })
                        .setNegativeButton(getString(R.string.confirm_cancel), null)
                        .show();

                return true;

            case R.id.action_finish:
                // User chose the "Settings" item, show the app settings UI...
                goBackHome();

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
