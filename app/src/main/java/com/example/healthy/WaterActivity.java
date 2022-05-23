package com.example.healthy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import es.dmoral.toasty.Toasty;

public class WaterActivity extends AppCompatActivity {

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3, floatingActionButton4, floatingActionButton5;
    CircularFillableLoaders circle;

    private final static String TAG = WaterActivity.class.getSimpleName();
    public final static String GOAL = "Goal : ";
    public final static String TODAY = "Today : ";
    public final static String PATH_GOAL_LOCATION = "goal";
    public final static String PATH_GOAL_KEY = "goalFile";
    public final static String PATH_TODAY_LOCATION = "today";
    public final static String PATH_TODAY_KEY = "todayFile";
    public final static String PATH_PROGRESS_LOCATION = "progress";
    public final static String PATH_PROGRESS_KEY = "progressFile";

    static final String DAY_OF_YEAR = "day_of_year";
    static final String NUMBER_OF_BOTTLES = "number_of_bottles";
    private int dayOfYear;



    Button endDay;
    TextView goal;
    TextView today;
    int progress = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.water);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.eat:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.sport:
                        startActivity(new Intent(getApplicationContext(), SportActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.water:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        goal = findViewById(R.id.goal);
        today = findViewById(R.id.today);

        goal.setText(GOAL + getGoal() + "ml");
        today.setText(TODAY + getToday() + "ml");

        /*
         Floating menu button init
          */
        materialDesignFAM = findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = findViewById(R.id.material_design_floating_action_menu_item3);
        floatingActionButton4 = findViewById(R.id.material_design_floating_action_menu_item4);
        floatingActionButton5 = findViewById(R.id.material_design_floating_action_menu_item5);

        circle = findViewById(R.id.circle);
        animateString(getGoal(),getToday());


        /*
        end the day  alert dialog + button + animate on change
         */

        /*
        floating buttons listeners
         */
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tempToday = Integer.parseInt(getToday());
                int tempGoal = Integer.parseInt(getGoal());

                tempToday = tempToday + 250;

                setTodayTextFromInt(tempToday);
                setTodayPref(tempToday);

                Log.d(TAG, "onClick: **************** tempToday" + tempToday);
                animate(tempGoal, tempToday);

            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tempToday = Integer.parseInt(getToday());
                int tempGoal = Integer.parseInt(getGoal());

                tempToday = tempToday + 500;

                setTodayTextFromInt(tempToday);
                setTodayPref(tempToday);

                Log.d(TAG, "onClick: **************** tempToday" + tempToday);
                animate(tempGoal, tempToday);

            }
        });

        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tempToday = Integer.parseInt(getToday());
                int tempGoal = Integer.parseInt(getGoal());

                tempToday = tempToday + 1000;

                setTodayTextFromInt(tempToday);
                setTodayPref(tempToday);

                Log.d(TAG, "onClick: **************** tempToday" + tempToday);
                animate(tempGoal, tempToday);

            }
        });

        /*
        CUSTOM user input for todays intake
         */

        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new MaterialDialog.Builder(WaterActivity.this)
                        .title(R.string.inputCustomAmount)
                        .content(R.string.input_content)
                        .inputType(InputType.TYPE_CLASS_NUMBER )
                        .inputRangeRes(3,5,R.color.warning_stroke_color)
                        .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                int tempToday = Integer.parseInt(getToday());
                                int tempGoal = Integer.parseInt(getGoal());

                                tempToday = tempToday + Integer.parseInt(input.toString());
                                setTodayTextFromInt(tempToday);
                                setTodayPref(tempToday);

                                Log.d(TAG, "onClick: **************** tempToday CUSTOM" + tempToday);
                                animate(tempGoal, tempToday);

                                Toasty.success(WaterActivity.this, "Added " + Integer.parseInt(input.toString()), Toast.LENGTH_SHORT,true).show();
                            }
                        }).show();


//                setTodayTextFromInt(tempToday);
//                setTodayPref(tempToday);
//
//                Log.d(TAG, "onClick: **************** tempToday" + tempToday);
//                animate(tempGoal, tempToday);

            }
        });

        floatingActionButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(WaterActivity.this)
                        .title(R.string.inputCustomAmountDelete)
                        .content(R.string.input_content)
                        .inputType(InputType.TYPE_CLASS_NUMBER )
                        .inputRangeRes(3,5,R.color.warning_stroke_color)
                        .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                int tempToday = Integer.parseInt(getToday());
                                int tempGoal = Integer.parseInt(getGoal());

                                if(Integer.parseInt(input.toString()) <= tempToday) {
                                    tempToday = tempToday - Integer.parseInt(input.toString());
                                    setTodayTextFromInt(tempToday);
                                    setTodayPref(tempToday);
                                    Log.d(TAG, "onClick: **************** tempToday CUSTOM" + tempToday);
                                    animate(tempGoal, tempToday);
                                    Toasty.success(WaterActivity.this, "Discarded " + Integer.parseInt(input.toString()), Toast.LENGTH_SHORT, true).show();
                                }
                                else {
                                    Toasty.error(WaterActivity.this, "Amount is too big", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        }).show();
            }
        });
        Calendar calendar = Calendar.getInstance();

        //get the data from SharedPreferences  and set dayOfYear, waterPercentText and
        //the number of bottles drunk
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        dayOfYear = sharedPreferences.getInt(DAY_OF_YEAR, 0);
        if(dayOfYear < calendar.get(Calendar.DAY_OF_YEAR)){

            dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            setTodayPref(0);
            setTodayTextFromInt(0);
            animateString(getGoal(),getToday());

        }
    }

    /*
    retrieve goal or empty
     */
    public String getGoal() {
        SharedPreferences uri = this.getSharedPreferences(PATH_GOAL_LOCATION, Context.MODE_PRIVATE);
        return uri.getString(PATH_GOAL_KEY, "2000");
    }

    /*
    store goal
     */
    public void setGoal(String goal) {
        SharedPreferences uri = this.getSharedPreferences(PATH_GOAL_LOCATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = uri.edit();
        editor.putString(PATH_GOAL_KEY, goal);
        editor.apply();
    }

    /*
    retrieve today value if there is one
     */
    public String getToday() {
        SharedPreferences uri = this.getSharedPreferences(PATH_TODAY_LOCATION, Context.MODE_PRIVATE);
        return uri.getString(PATH_TODAY_KEY, "0");
    }

    /*
    set today value
     */
    public void setTodayPref(int value) {
        String today = String.valueOf(value);
        SharedPreferences uri = this.getSharedPreferences(PATH_TODAY_LOCATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = uri.edit();
        editor.putString(PATH_TODAY_KEY, today);
        editor.apply();
    }

    /*
   retrieve circle value if there is one
    */
    public String getProgress() {
        SharedPreferences uri = this.getSharedPreferences(PATH_PROGRESS_LOCATION, Context.MODE_PRIVATE);
        return uri.getString(PATH_PROGRESS_KEY, "100");
    }

    /*
    set circle value
     */
    public void setProgress(String progress) {
        SharedPreferences uri = this.getSharedPreferences(PATH_PROGRESS_LOCATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = uri.edit();
        editor.putString(PATH_PROGRESS_KEY, progress);
        editor.apply();
    }


    /*
    set text field of today
     */
    public void setTodayTextFromInt(int val) {
        String i = String.valueOf(val);
        today.setText(TODAY + i + "ml");
    }

    /*
      What percentage of goal is the value of today consumed water, if more than 100% - keep the jar filled at 100%
                 */
    public void animate(int goal, int today) {

        int percent = today * 100 / goal;

        if(percent > 100) {
            circle.setProgress(0);
        }
        else {
            circle.setProgress(100-percent);
        }
    }

    /*
    same animation, but params are string
     */
    public void animateString(String goalSt, String todaySt) {

        int goal = Integer.parseInt(goalSt);
        int today = Integer.parseInt(todaySt);

        int percent = today * 100 / goal;

        if(percent > 100) {
            circle.setProgress(0);
        }
        else {
            circle.setProgress(100-percent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        goal.setText(GOAL + getGoal() + "ml");
        animateString(getGoal(),getToday());
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(DAY_OF_YEAR, dayOfYear);

        editor.commit();

    }
}
