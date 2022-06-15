package com.example.healthy;

import android.app.Dialog;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.healthy.Database.Database;
import com.example.healthy.util.Util;

import java.util.Calendar;
import java.util.Date;

abstract class Dialog_Statistics {
    public static Dialog getDialog(final Context c, int since_boot){
        final Dialog d = new Dialog(c);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.statistics);
        d.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                d.dismiss();
            }
        });
        Database db= Database.getInstance(c);
        Pair<Date,Integer> record= db.getRecordData();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(Util.getToday());
        int daysThisMonth=date.get(Calendar.DAY_OF_MONTH);
        date.add(Calendar.DATE,-6);
        int thisWeek= db.getSteps(date.getTimeInMillis(),System.currentTimeMillis())+since_boot;
        date.setTimeInMillis(Util.getToday());
        date.set(Calendar.DAY_OF_MONTH,1);
        int thisMonth=db.getSteps(date.getTimeInMillis(),System.currentTimeMillis())+since_boot;
        ( (TextView) d.findViewById(R.id.record)).setText(SportFragment.formatter.format(record.second)+"  "+
                java.text.DateFormat.getDateInstance().format(record.first));
        ( (TextView) d.findViewById(R.id.totalthisweek)).setText(SportFragment.formatter.format(thisWeek));
        ( (TextView) d.findViewById(R.id.totalthismonth)).setText(SportFragment.formatter.format(thisMonth));
        ( (TextView) d.findViewById(R.id.averagethisweek)).setText(SportFragment.formatter.format(thisWeek/7));
        ( (TextView) d.findViewById(R.id.averagethismonth)).setText(SportFragment.formatter.format(thisMonth/daysThisMonth));
        db.close();
        return d;





    }
}
