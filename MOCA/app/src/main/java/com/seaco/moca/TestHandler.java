package com.seaco.moca;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.view.View;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by HaoBin on 15/01/16.
 */
public class TestHandler {
    public Activity activity;
    static RelativeLayout container;
    static XMLHandlerSession session;

    static int testMark;
    private Chronometer section5Watch;

    public TestHandler(Activity _activity, XMLHandlerSession _session) {
        testMark = 0;
        if (_session.additionalPointForEducation) {
            testMark += 1;
        }
        activity = _activity;
        session = _session;
        container = (RelativeLayout) activity.findViewById(R.id.moca_tests_container);
        System.out.println(testMark);
    }

    public int getTestMark() {
        return testMark;
    }

    public void section1(long startTime) {

        int sectionMark = 0;
        View section = container.getChildAt(1);
        HashMap<String, String> map = new HashMap<String, String>();
        CheckBox cb;
        int[] cbArray = {R.id.moca_visuo_node_cb1};
        String[] cbMapKey = {"node-mark"};

        for (int i=0; i<cbArray.length; i++) {
            cb = (CheckBox) section.findViewById(cbArray[i]);
            map.put(cbMapKey[i], String.valueOf(cb.isChecked()));
            if (cb.isChecked()) {
                sectionMark += 1;
            }
        }

        DrawCanvas canvas = (DrawCanvas) activity.findViewById(R.id.node_canvas);
        String strokeTimes = canvas.getStrokeTimes().toString();
        map.put("strokeTimesInMilliSeconds", strokeTimes);
        String points = canvas.getPoints().toString();
        map.put("pointsInXYPair", points);

        testMark += sectionMark;
        map.put("section-mark",String.valueOf(sectionMark));
        String imgname = session.getFilename() + "_image_visuonode.jpg";
        map.put("image", imgname);
        session.saveTestData("visuo-node", map, true);

        ((DrawCanvas) activity.findViewById(R.id.node_canvas)).saveImageIntoSessionWithOverlay(session, "visuonode");

        System.out.println(testMark);
    }

    public void section2(long startTime) {
                int sectionMark = 0;
        View section = container.getChildAt(2);
        HashMap<String, String> map = new HashMap<String, String>();

        CheckBox cb;

        int[] cbArray = {R.id.moca_visuo_cube_cb1};


        String[] cbMapKey = {"cube-mark"};

        for (int i=0; i<cbArray.length; i++) {
            cb = (CheckBox) section.findViewById(cbArray[i]);
            map.put(cbMapKey[i], String.valueOf(cb.isChecked()));
            if (cb.isChecked()) {
                sectionMark += 1;
            }
        }

        testMark += sectionMark;
        map.put("section-mark",String.valueOf(sectionMark));

        String imgname = session.getFilename() + "_image_visuocube.jpg";
        map.put("image", imgname);
        session.saveTestData("visuo-cube", map, true);

        ((DrawCanvas) activity.findViewById(R.id.cube_canvas)).saveImageIntoSession(session, "visuocube");System.out.println(testMark);
    }

    public void section3(long startTime) {
                int sectionMark = 0;
        View section = container.getChildAt(3);
        HashMap<String, String> map = new HashMap<String, String>();

        CheckBox cb;

        int[] cbArray = {
                R.id.moca_visuo_clock_cb1, R.id.moca_visuo_clock_cb2, R.id.moca_visuo_clock_cb3
        };


        String[] cbMapKey = {
                "contour-mark", "number-mark", "hands-mark"
        };


        for (int i=0; i<cbArray.length; i++) {
            cb = (CheckBox) section.findViewById(cbArray[i]);
            map.put(cbMapKey[i], String.valueOf(cb.isChecked()));
            if (cb.isChecked()) {
                sectionMark += 1;
            }
        }

        testMark += sectionMark;
        String imgname = session.getFilename() + "_image_visuoclock.jpg";
        map.put("image", imgname);

        map.put("section-mark", String.valueOf(sectionMark));
        session.saveTestData("visuo-clock", map, true);

        ((DrawCanvas) activity.findViewById(R.id.clock_canvas)).saveImageIntoSession(session, "visuoclock");System.out.println(testMark);

    }

    public void section4(long startTime) {

        int sectionMark = 0;

        View section = container.getChildAt(4);
        HashMap<String, String> map4 = new HashMap<String, String>();
        EditText et;
        CheckBox cb;

        long duration = session.getCurrentDateTimeUTC() - startTime;
        map4.put("durationInMilliSeconds", String.valueOf(duration));

        int[] etArray = {
                R.id.moca_naming_camel_field, R.id.moca_naming_lion_field, R.id.moca_naming_var_field
        };
        int[] cbArray = {
                R.id.moca_naming_camel_mark, R.id.moca_naming_lion_mark, R.id.moca_naming_var_mark
        };

        String[] str = new String[] {"rhino-input","rhino-mark"};

        if (InfoScreen.moca_naming_flag) {
            str = new String[] {"elephant-input", "elephant-mark"};
        }

        String[] etMapKey = {
                "camel-input", "lion-input", str[0]
        };

        String[] cbMapKey = {
                "camel-mark", "lion-mark", str[1]
        };

        for (int i=0; i<etArray.length; i++) {
            map4.put(etMapKey[i], ((EditText) section.findViewById(etArray[i]))
                    .getText().toString());
            cb = (CheckBox) section.findViewById(cbArray[i]);
            map4.put(cbMapKey[i], String.valueOf(cb.isChecked()));
            if (cb.isChecked()) {
                sectionMark += 1;
            }
        }

        testMark += sectionMark;
        map4.put("section-mark",String.valueOf(sectionMark));
        session.saveTestData("naming", map4, true);System.out.println(testMark);
    }



    public void section5(long startTime) {
                View section = container.getChildAt(5);
        HashMap<String, String> map = new HashMap<String, String>();

        section5Watch = (Chronometer) container.getChildAt(12).findViewById(R.id.chronometer);
        section5Watch.setBase(SystemClock.elapsedRealtime());
        section5Watch.start();

        long duration = session.getCurrentDateTimeUTC() - startTime;
        map.put("durationInMilliSeconds", String.valueOf(duration));

        EditText et;

        int[] etArray = {
                R.id.moca_memory_q1_field1,
                R.id.moca_memory_q2_field1,
                R.id.moca_memory_q3_field1,
                R.id.moca_memory_q4_field1,
                R.id.moca_memory_q5_field1,
                R.id.moca_memory_q1_field2,
                R.id.moca_memory_q2_field2,
                R.id.moca_memory_q3_field2,
                R.id.moca_memory_q4_field2,
                R.id.moca_memory_q5_field2,
        };

        String[] etMapKey = {
                "q1-input1", "q2-input1","q3-input1", "q4-input1","q5-input1",
                "q1-input2", "q2-input2","q3-input2", "q4-input2","q5-input2"
        };

        for (int i=0; i<etArray.length; i++) {
            map.put(etMapKey[i], ((EditText) section.findViewById(etArray[i]))
                    .getText().toString());

        }

        map.put("section-mark","None");
        session.saveTestData("memory", map, true);System.out.println(testMark);
    }

    public void section6(long startTime) {

        int sectionMark = 0;

        View section = container.getChildAt(6);
        HashMap<String, String> map = new HashMap<String, String>();
        CheckBox cb;

        long duration = session.getCurrentDateTimeUTC() - startTime;
        map.put("durationInMilliSeconds", String.valueOf(duration));

        int[] cbArray = {
                R.id.moca_attention_digits_q1_mark, R.id.moca_attention_digits_q2_mark
        };

        String[] cbMapKey = {
                "q1-mark", "q2-mark"
        };

        for (int i=0; i<cbArray.length; i++) {

            cb = (CheckBox) section.findViewById(cbArray[i]);
            map.put(cbMapKey[i], String.valueOf(cb.isChecked()));
            if (cb.isChecked()) {
                sectionMark += 1;
            }
        }

        testMark += sectionMark;
        map.put("section-mark",String.valueOf(sectionMark));
        session.saveTestData("attention-digits", map, true);System.out.println(testMark);
    }

    public void section7(long startTime) {


        int sectionMark = 0;

        View section = container.getChildAt(7);
        HashMap<String, String> map = new HashMap<String, String>();
        CheckBox cb;

        long duration = session.getCurrentDateTimeUTC() - startTime;
        map.put("durationInMilliSeconds", String.valueOf(duration));

        int[] cbArray = {
                R.id.moca_attention_letters_q1_mark
        };

        String[] cbMapKey = {
                "mark"
        };

        for (int i=0; i<cbArray.length; i++) {

            cb = (CheckBox) section.findViewById(cbArray[i]);
            map.put(cbMapKey[i], String.valueOf(cb.isChecked()));
            if (cb.isChecked()) {
                sectionMark += 1;
            }
        }

        testMark += sectionMark;
        map.put("section-mark",String.valueOf(sectionMark));
        session.saveTestData("attention-letters", map, true);System.out.println(testMark);
    }

    public void section8(long startTime) {


        int sectionMark = 0;

        View section = container.getChildAt(8);
        HashMap<String, String> map = new HashMap<String, String>();
        CheckBox cb;

        long duration = session.getCurrentDateTimeUTC() - startTime;
        map.put("durationInMilliSeconds", String.valueOf(duration));

        int[] cbArray = {
                R.id.moca_attention_subtraction_q1_mark,
                R.id.moca_attention_subtraction_q2_mark,
                R.id.moca_attention_subtraction_q3_mark,
                R.id.moca_attention_subtraction_q4_mark,
                R.id.moca_attention_subtraction_q5_mark
        };

        String[] cbMapKey = {
                "q1-mark", "q2-mark", "q3-mark", "q4-mark", "q5-mark"
        };

        for (int i=0; i<cbArray.length; i++) {

            cb = (CheckBox) section.findViewById(cbArray[i]);
                        map.put(cbMapKey[i], String.valueOf(cb.isChecked()));
            if (cb.isChecked()) {
                sectionMark += 1;
            }
        }

        if (sectionMark == 4 || sectionMark == 5) {
            sectionMark = 3;
        } else if (sectionMark == 2 || sectionMark == 3) {
            sectionMark = 2;
        }

        testMark += sectionMark;
        map.put("section-mark",String.valueOf(sectionMark));
        session.saveTestData("attention-subtraction", map, true);System.out.println(testMark);
    }

    public void section9(long startTime) {

        int sectionMark = 0;

        View section = container.getChildAt(9);
        HashMap<String, String> map = new HashMap<String, String>();
        EditText et;
        CheckBox cb;

        long duration = session.getCurrentDateTimeUTC() - startTime;
        map.put("durationInMilliSeconds", String.valueOf(duration));

        int[] cbArray = {
                R.id.moca_language_repeat_q1_mark, R.id.moca_language_repeat_q2_mark
        };

        String[] cbMapKey = {
                "q1-mark", "q2-mark"
        };

        for (int i=0; i<cbArray.length; i++) {

            cb = (CheckBox) section.findViewById(cbArray[i]);
            map.put(cbMapKey[i], String.valueOf(cb.isChecked()));
            if (cb.isChecked()) {
                sectionMark += 1;
            }
        }

        testMark += sectionMark;
        map.put("section-mark",String.valueOf(sectionMark));
        session.saveTestData("lang-repeat", map, true);System.out.println(testMark);
    }

    public void section10(long startTime) {


        int sectionMark = 0;

        View section = container.getChildAt(10);
        HashMap<String, String> map10 = new HashMap<String, String>();
        EditText et;
        CheckBox cb;

        long duration = session.getCurrentDateTimeUTC() - startTime;
        map10.put("durationInMilliSeconds", String.valueOf(duration));

        int[] etArray = {
                R.id.moca_fluency_field
        };
        int[] cbArray = {
                R.id.moca_fluency_mark
        };

        String[] etMapKey = {
                "words-input"
        };

        String[] cbMapKey = {
                "fluency-mark"
        };

        for (int i=0; i<etArray.length; i++) {
            map10.put(etMapKey[i], ((EditText) section.findViewById(etArray[i]))
                    .getText().toString());
            cb = (CheckBox) section.findViewById(cbArray[i]);
            map10.put(cbMapKey[i], String.valueOf(cb.isChecked()));
            if (cb.isChecked()) {
                sectionMark += 1;
            }
        }

        testMark += sectionMark;
        map10.put("section-mark",String.valueOf(sectionMark));
        session.saveTestData("lang-fluency", map10, true);System.out.println(testMark);
    }

    public void section11(long startTime) {

        int sectionMark = 0;

        View section = container.getChildAt(11);
        HashMap<String, String> map11 = new HashMap<String, String>();
        EditText et;
        CheckBox cb;

        long duration = session.getCurrentDateTimeUTC() - startTime;
        map11.put("durationInMilliSeconds", String.valueOf(duration));

        int[] etArray = {
                R.id.moca_abstraction_q1_field, R.id.moca_abstraction_q2_field
        };
        int[] cbArray = {
                R.id.moca_abstraction_q1_mark, R.id.moca_abstraction_q2_mark
        };

        String[] etMapKey = {
                "train-bicycle", "watch-ruler"
        };

        String[] cbMapKey = {
                "train-bicycle-mark", "watch-ruler-mark"
        };

        for (int i=0; i<etArray.length; i++) {
            map11.put(etMapKey[i], ((EditText) section.findViewById(etArray[i]))
                    .getText().toString());
            cb = (CheckBox) section.findViewById(cbArray[i]);
            map11.put(cbMapKey[i], String.valueOf(cb.isChecked()));
            if (cb.isChecked()) {
                sectionMark += 1;
            }
        }

        testMark += sectionMark;
        map11.put("section-mark",String.valueOf(sectionMark));
        session.saveTestData("abstraction", map11, true);System.out.println(testMark);
    }

    public void section12(long startTime) {


        int sectionMark = 0;
        View section = container.getChildAt(12);
        HashMap<String, String> map = new HashMap<String, String>();


        long elapsedMillis = SystemClock.elapsedRealtime() - section5Watch.getBase();
        map.put("timeElapsedSinceSection5inMilliSeconds", String.valueOf(elapsedMillis));

        long duration = session.getCurrentDateTimeUTC() - startTime;
        map.put("durationInMilliSeconds", String.valueOf(duration));

        EditText et;
        CheckBox cb;

        int[] etArray = {
                R.id.moca_memory_delayed_q1_field1,
                R.id.moca_memory_delayed_q2_field1,
                R.id.moca_memory_delayed_q3_field1,
                R.id.moca_memory_delayed_q4_field1,
                R.id.moca_memory_delayed_q5_field1
        };

        int[] cbArray = {
                R.id.moca_memory_delayed_q1_mark,
                R.id.moca_memory_delayed_q2_mark,
                R.id.moca_memory_delayed_q3_mark,
                R.id.moca_memory_delayed_q4_mark,
                R.id.moca_memory_delayed_q5_mark
        };

        String[] etMapKey = {
                "q1-input", "q2-input", "q3-input", "q4-input", "q5-input"
        };

        String[] cbMapKey = {
                "q1-mark", "q2-mark", "q3-mark", "q4-mark", "q5-mark"
        };

        for (int i=0; i<etArray.length; i++) {
            map.put(etMapKey[i], ((EditText) section.findViewById(etArray[i]))
                    .getText().toString());
            cb = (CheckBox) section.findViewById(cbArray[i]);
            map.put(cbMapKey[i], String.valueOf(cb.isChecked()));
            if (cb.isChecked()) {
                sectionMark += 1;
            }

        }

        testMark += sectionMark;
        map.put("section-mark",String.valueOf(sectionMark));
        session.saveTestData("memory-delayed", map, true);


        // Optional Data Saving
        int[] optionalDataArray = {
                R.id.moca_memory_delayed_q1_field2,
                R.id.moca_memory_delayed_q2_field2,
                R.id.moca_memory_delayed_q3_field2,
                R.id.moca_memory_delayed_q4_field2,
                R.id.moca_memory_delayed_q5_field2,
                R.id.moca_memory_delayed_q1_field3,
                R.id.moca_memory_delayed_q2_field3,
                R.id.moca_memory_delayed_q3_field3,
                R.id.moca_memory_delayed_q4_field3,
                R.id.moca_memory_delayed_q5_field3,
        };

        String[] optionalDataMapKey = {
                "q1-input2", "q2-input2", "q3-input2", "q4-input2", "q5-input2",
                "q1-input3", "q2-input3", "q3-input3", "q4-input3", "q5-input3"
        };

        map = new HashMap<String, String>();
        for (int i=0; i<optionalDataArray.length; i++) {
            map.put(optionalDataMapKey[i], ((EditText) section.findViewById(optionalDataArray[i]))
                    .getText().toString());
        }

        session.saveTestData("memory-delayed-optionals", map, true);System.out.println(testMark);
    }

    public void section13(long startTime) {


        int sectionMark = 0;

        View section = container.getChildAt(13);
        HashMap<String, String> map13 = new HashMap<String, String>();
        EditText et;
        CheckBox cb;

        long duration = session.getCurrentDateTimeUTC() - startTime;
        map13.put("durationInMilliSeconds", String.valueOf(duration));

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

        for (int i=0; i<etArray.length; i++) {
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
        session.saveTestData("orientation", map13, true);System.out.println(testMark);
    }
}
