package com.example.healthy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthy.Database.Database;
import com.example.healthy.util.Util;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SportStatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SportStatisticFragment extends Fragment {

    private TextView totalsteps, totalCalories, totldistance;
    ImageButton backBtn;
    private String unit;
    public static NumberFormat formatter=NumberFormat.getInstance(Locale.getDefault());
    private int todayoffset,total_start,since_boot;

    private BarChart barChart;
    private int goalStatistic;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SportStatisticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SportStatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SportStatisticFragment newInstance(String param1, String param2) {
        SportStatisticFragment fragment = new SportStatisticFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sport_statistic, container, false);
        barChart =view.findViewById(R.id.bargraph);
        updateBars();
        totalCalories = view.findViewById(R.id.caloriestotal);
        totalsteps = view.findViewById(R.id.totalstep);
        totldistance = view.findViewById(R.id.distancetotal);
        Database db = Database.getInstance(getActivity());
        todayoffset = db.getSteps(Util.getToday());
        since_boot = db.getCurrentSteps();
        total_start = db.getTotalWithoutToday();
        totalsteps.setText(formatter.format(todayoffset+since_boot+total_start));
        double kcal=(todayoffset+since_boot+total_start)*0.04;
        totalCalories.setText(formatter.format(kcal));
        float distance_total=(todayoffset+since_boot+total_start)*75;
        distance_total/=100000;
        unit="km";
        totldistance.setText(formatter.format(distance_total)+" "+unit);
        backBtn = view.findViewById(R.id.backbtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new SportFragment());
            }
        });
        return view;
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

    private void updateBars() {

        SimpleDateFormat df= new SimpleDateFormat("E", Locale.getDefault());
        if(barChart.getData().size()>0) barChart.clearChart();
        int steps;
        boolean stepsize_cm=true;

        BarModel bm;
        Database db=Database.getInstance(getActivity());
        List<Pair<Long,Integer>> last = db.getLastEntries(7);
        db.close();
        for(int i=last.size()-1;i>0;i--) {

            Pair<Long, Integer> current = last.get(i);
            steps = current.second;
            if (steps > 0) {
                SharedPreferences prefs =
                        getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
                goalStatistic = prefs.getInt("goal", (int) SportFragment.DEFAULT_GOAL);
                bm = new BarModel(df.format(new Date(current.first)), 0, steps > goalStatistic ? Color.parseColor("#A9E30B") : Color.parseColor("#777876"));
                bm.setValue(steps);

                barChart.addBar(bm);
            }
        }
        if(barChart.getData().size()>0){
            barChart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog_Statistics.getDialog(getActivity(), SportFragment.since_boot).show();
                }
            });
        }

    }
}