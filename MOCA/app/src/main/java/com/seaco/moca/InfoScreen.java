package com.seaco.moca;

import android.app.AlertDialog;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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

public class InfoScreen extends AppCompatActivity {

//    FragmentManager fragmentManager;
    RelativeLayout container;
    LayoutInflater layoutInflater;
    static int pageIndex;
    static Integer[] pageArray;
    FloatingActionButton fab_forward;
    FloatingActionButton fab_back;

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
                Snackbar.make(view, "Go back", Snackbar.LENGTH_SHORT)
                        .setAction("Next", null).show();
                proceedToPreviousScreen();
            }
        });

        fab_forward = (FloatingActionButton) findViewById(R.id.fab_forward);
        fab_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Proceed", Snackbar.LENGTH_SHORT)
                        .setAction("Next", null).show();
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
                R.layout.moca_visuo_node
        };
    }

    private void proceedToScreenLayout(int screen) {
        // screen = R.layout.content_info_screen
        container.removeAllViews();
        View child = getLayoutInflater().inflate(screen ,null);
        container.addView(child);
    }

    private void proceedToNextScreen() {
        if (pageIndex + 1 < pageArray.length) {
            proceedToScreenLayout(pageArray[pageIndex+1]);
            fab_back.setVisibility(View.VISIBLE);
            pageIndex += 1;
        }
        if (pageIndex == pageArray.length - 1) {
            fab_forward.setVisibility(View.INVISIBLE);
        }


    }

    private void proceedToPreviousScreen() {
        if (pageIndex - 1 >= 0) {
            proceedToScreenLayout(pageArray[pageIndex-1]);
            fab_forward.setVisibility(View.VISIBLE);
            pageIndex -= 1;
        }
        if (pageIndex == 0) {
            fab_back.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.screen_menu, menu);
        return true;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    private void goBackHome() {
        finish();
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


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
