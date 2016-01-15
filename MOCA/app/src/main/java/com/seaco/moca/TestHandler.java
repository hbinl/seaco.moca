package com.seaco.moca;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.view.View;

import java.util.HashMap;

/**
 * Created by HaoBin on 15/01/16.
 */
public class TestHandler {
    public Activity activity;
    static RelativeLayout container;
    static XMLHandlerSession session;

    static int testMark;

    public TestHandler(Activity _activity, XMLHandlerSession _session) {
        testMark = 0;
        activity = _activity;
        session = _session;
        container = (RelativeLayout) activity.findViewById(R.id.moca_tests_container);
    }

    public int getTestMark() {
        return testMark;
    }

    public void section1() {
        System.out.println(1);
    }

    public void section2() {
        System.out.println(2);
    }

    public void section3() {
        System.out.println(3);
    }

    public void section4() {
        System.out.println(4);
    }

    public void section5() {
        System.out.println(5);
    }

    public void section6() {
        System.out.println(6);
    }
    public void section7() {
        System.out.println(7);
    }

    public void section8() {
        System.out.println(8);
    }

    public void section9() {
        System.out.println(9);
    }
    public void section10() {
        System.out.println(10);
    }

    public void section11() {
        System.out.println(11);
    }

    public void section12() {
        System.out.println(12);
    }

    public void section13() {
        System.out.println(13);
        int sectionMark = 0;

        View section = container.getChildAt(13);
        HashMap<String, String> map13 = new HashMap<String, String>();
        EditText et;
        CheckBox cb;

        int[] etArray = {
                R.id.moca_orientation_day_field, R.id.moca_orientation_city_field,
                R.id.moca_orientation_month_field, R.id.moca_orientation_year_field,
                R.id.moca_orientation_weekday_field, R.id.moca_orientation_place_field
        };
        int[] cbArray = {
                R.id.moca_orientation_day_mark, R.id.moca_orientation_city_mark,
                R.id.moca_orientation_month_mark, R.id.moca_orientation_year_mark,
                R.id.moca_orientation_weekday_mark, R.id.moca_orientation_place_mark
        };


        String[] etMapKey = {
                "day-input", "city-input", "month-input", "year-input", "weekday-input", "place-input"
        };

        String[] cbMapKey = {
            "day-mark", "city-mark", "month-mark", "year-mark", "weekday-mark", "place-mark"
        };

        for (int i=0; i<6; i++) {
            map13.put(etMapKey[i], ((EditText) section.findViewById(etArray[i]))
                    .getText().toString());
            cb = (CheckBox) section.findViewById(cbArray[i]);
            map13.put(cbMapKey[i], String.valueOf(cb.isChecked()));
            if (cb.isChecked()) {
                sectionMark += 1;
            }
        }

        testMark += sectionMark;
        map13.put("section-mark",String.valueOf(sectionMark));
        session.saveTestData("orientation", map13, true);
    }
}
