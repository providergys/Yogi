package com.yogi_app.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.yogi_app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by AndroidDev on 19-Nov-18.
 */

public class TestCalendarEvents extends AppCompatActivity{
    final int callbackId = 42;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        Calendar dt = Calendar.getInstance();

// Where untilDate is a date instance of your choice, for example 30/01/2012
//        dt.setTime(untilDate);

// If you want the event until 30/01/2012, you must add one day from our day because UNTIL in RRule sets events before the last day
        dt.add(Calendar.DATE, 1);
        String dtUntill = yyyyMMdd.format(dt.getTime());

        ContentResolver cr = this.getContentResolver();
        ContentValues values = new ContentValues();

//        values.put(CalendarContract.Events.DTSTART, dtstart);
//        values.put(CalendarContract.Events.TITLE, title);
//        values.put(CalendarContract.Events.DESCRIPTION, comment);

        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());

// Default calendar
        values.put(CalendarContract.Events.CALENDAR_ID, 1);

        values.put(CalendarContract.Events.RRULE, "FREQ=DAILY;UNTIL="
                + dtUntill);
// Set Period for 1 Hour
        values.put(CalendarContract.Events.DURATION, "+P1H");

        values.put(CalendarContract.Events.HAS_ALARM, 1);

// Insert event to calendar

//        checkPermission(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);

//        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);





    }

    private void checkPermissions(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }


}
