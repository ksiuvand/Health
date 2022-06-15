package com.example.healthy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.healthy.Components.SensorListener;
import com.example.healthy.Database.Database;
import com.example.healthy.util.API26Wrapper;
import com.example.healthy.util.Logger;
import com.example.healthy.util.Util;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SportFragment extends Fragment implements SensorEventListener {

    public static NumberFormat formatter=NumberFormat.getInstance(Locale.getDefault());
    private ImageView levels,dialog;
    private TextView stepsView, caloriesTxt, distanceTxt;
    private PieModel sliceGoal,sliceCurrent;
    private PieChart pg;
    private ImageButton statistic;
    public static int totalstepsgoal=0;
    public static int since_boot;
    private int todayoffset, total_start, goal, totaldays;
    private int goalreach;
    private boolean showSteps = true;
    public static final float DEFAULT_GOAL = 8000;

    public TextView goalSteps;
    private ImageButton changeGoalSteps;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SportFragment newInstance(String param1, String param2) {
        SportFragment fragment = new SportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
        if(Build.VERSION.SDK_INT>=26){
            API26Wrapper.startForegroundService(getActivity(),new Intent(getActivity(), SensorListener.class));
        }
        else{
            getActivity().startService(new Intent(getActivity(), SensorListener.class));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sport, container, false);
        goalSteps = view.findViewById(R.id.goalStep);
        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
        goal = prefs.getInt("goal", (int) DEFAULT_GOAL);
        goalSteps.setText(goal +" " + getStepchislo(goal));

        stepsView=view.findViewById(R.id.stepsinpiechart);
        caloriesTxt = view.findViewById(R.id.calories);
        distanceTxt = view.findViewById(R.id.distance);
        statistic = view.findViewById(R.id.statistic);
        statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new SportStatisticFragment());
            }
        });
        pg=view.findViewById(R.id.graph);
        setPiechart();
        pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSteps = !showSteps;
                stepsDistanceChanges();
            }

        });
        changeGoalSteps = view.findViewById(R.id.changegoalSteps);
        changeGoalSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGoalSteps();
            }
        });

        return view;
    }

    private void setGoalSteps() {
        final SharedPreferences prefs= getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
        new MaterialDialog.Builder(getActivity())
                .title(R.string.sportGoatUpdate)
                .content(R.string.inputGoalStep)
                .inputType(InputType.TYPE_CLASS_NUMBER )
                .inputRangeRes(3,5,R.color.black)
                .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        prefs.edit().putInt("goal",Integer.parseInt(input.toString())).commit();
                        goalSteps.setText(Integer.parseInt(input.toString())+ " " + getStepchislo(Integer.parseInt(input.toString())));
                        dialog.dismiss();
                        updatePie();
                        getActivity().startService(new Intent(getActivity(), SensorListener.class).putExtra("updateNotificationState",true));

                    }
                }).show();


    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        Database db = Database.getInstance(getActivity());

        if (BuildConfig.DEBUG) db.logState();
        // read todays offset
        todayoffset = db.getSteps(Util.getToday());

        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);

        goal = prefs.getInt("goal", (int) DEFAULT_GOAL);
        since_boot = db.getCurrentSteps();
        int pauseDifference = since_boot - prefs.getInt("pauseCount", since_boot);

        // register a sensorlistener to live update the UI if a step is taken
        SensorManager sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor == null) {
            new AlertDialog.Builder(getActivity()).setTitle(R.string.no_sensor)
                    .setMessage(R.string.no_sensor_explain)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(final DialogInterface dialogInterface) {
                            getActivity().finish();
                        }
                    }).setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        } else {
            sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI, 0);
        }

        since_boot -= pauseDifference;

        total_start = db.getTotalWithoutToday();
        totaldays = db.getDays();

        db.close();

        stepsDistanceChanges();
    }

    private void stepsDistanceChanges() {
        if(showSteps){
            int steps_today=Math.max(todayoffset+since_boot,0);
            ((TextView) getView().findViewById(R.id.unit)).setText(getStepchislo(steps_today));

        }else{
            String unit="km";
            ((TextView) getView().findViewById(R.id.unit)).setText(unit);
        }
        updatePie();

    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            SensorManager sm=(SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            sm.unregisterListener(this);


        } catch (Exception e) {
            if(BuildConfig.DEBUG) Logger.log(e);
            e.printStackTrace();
        }
        Database db= Database.getInstance(getActivity());
        db.saveCurrentSteps(since_boot);
        db.close();
    }

    public String getStepchislo(int n) {
        int result = n % 100;
        if (result >=10 && result <= 20) {
            return this.getResources().getString(R.string.step);
        }
        result = n % 10;
        if (result == 0 || result > 4) {
            return this.getResources().getString(R.string.step);
        }
        if (result > 1) {
            return this.getResources().getString(R.string.step2);
        } if (result == 1) {
            return this.getResources().getString(R.string.step3);
        }
        return null;
    }

    private void updatePie() {
        if(BuildConfig.DEBUG) Logger.log("UI-updatesteps:"+since_boot);
        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
        goal = prefs.getInt("goal", (int) DEFAULT_GOAL);
        int steps_today=Math.max(todayoffset+since_boot,0);
        sliceCurrent.setValue(steps_today);
        if(goal-steps_today>0){
            if(pg.getData().size()==1){
                pg.addPieSlice(sliceGoal);
            }
            sliceGoal.setValue(goal-steps_today);}
        else{
            pg.clearChart();
            pg.addPieSlice(sliceCurrent);
        }
        pg.update();
        if(showSteps){
            stepsView.setText(formatter.format(steps_today));
            double kcal=steps_today*0.04;
            caloriesTxt.setText(formatter.format(kcal));
            float stepsize= 75;
            float distance_today=steps_today*stepsize;
            distance_today/=100000;
            distanceTxt.setText(formatter.format(distance_today));
            totalstepsgoal=total_start+steps_today;
        }
        else{
            //SharedPreferences prefs=getActivity().getSharedPreferences("pedometer",Context.MODE_PRIVATE);
            float stepsize= 75;
            float distance_today=steps_today*stepsize;
            float distance_total=(steps_today+total_start)*stepsize;
            distance_today/=100000;
            distance_total/=100000;
            stepsView.setText(formatter.format(distance_today));
            totalstepsgoal=total_start+steps_today;
        }
    }

    private void setPiechart() {
        sliceCurrent=new PieModel(0, Color.parseColor("#A9E30B"));
        pg.addPieSlice(sliceCurrent);
        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
        goal = prefs.getInt("goal", (int) DEFAULT_GOAL);
        sliceGoal=new PieModel(goal, Color.parseColor("#D3D3D3"));
        pg.addPieSlice(sliceGoal);
        pg.setDrawValueInPie(false);
        pg.setUsePieRotation(true);
        pg.startAnimation();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(BuildConfig.DEBUG) Logger.log(
                "UI- sensorchanged| todatyoffset:"+todayoffset+"sinceboot:"+since_boot+event.values[0]);
        if(event.values[0]>Integer.MAX_VALUE || event.values[0]==0){
            todayoffset=-(int) event.values[0];
            Database db= Database.getInstance(getActivity());
            db.insertNewDay(Util.getToday(),(int)event.values[0]);
            db.close();

        }
        since_boot=(int)event.values[0];
        updatePie();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //will not change
    }
}