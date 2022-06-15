package com.example.healthy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WaterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WaterFragment extends Fragment {

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3, floatingActionButton4, floatingActionButton5;
    CircularFillableLoaders circle;

    private final static String TAG = WaterFragment.class.getSimpleName();
    public final static String GOAL = "Цель : ";
    public final static String TODAY = "Сегодня : ";
    public final static String PATH_GOAL_LOCATION = "goal";
    public final static String PATH_GOAL_KEY = "goalFile";
    public final static String PATH_TODAY_LOCATION = "today";
    public final static String PATH_TODAY_KEY = "todayFile";
    public final static String PATH_PROGRESS_LOCATION = "progress";
    public final static String PATH_PROGRESS_KEY = "progressFile";

    static final String DAY_OF_YEAR = "day_of_year";
    static final String NUMBER_OF_BOTTLES = "number_of_bottles";
    private int dayOfYear;

    private ImageButton changeGoal;



    Button endDay;
    public TextView goal;
    TextView today;
    int progress = 100;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WaterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WaterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WaterFragment newInstance(String param1, String param2) {
        WaterFragment fragment = new WaterFragment();
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
        View view = inflater.inflate(R.layout.fragment_water, container, false);

        goal = view.findViewById(R.id.goal);
        today = view.findViewById(R.id.today);

        changeGoal = view.findViewById(R.id.changegoal);
        changeGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.inputCustomAmount)
                        .content(R.string.input_content)
                        .inputType(InputType.TYPE_CLASS_NUMBER )
                        .inputRangeRes(3,5,R.color.black)
                        .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                                setGoal(input.toString());

                                int tempToday = Integer.parseInt(getToday());
                                int tempGoal = Integer.parseInt(getGoal());
                                goal.setText(GOAL + getGoal() + getResources().getString(R.string.mll));

                                Log.d(TAG, "onClick: **************** tempToday CUSTOM" + tempToday);
                                animate(tempGoal, tempToday);

                            }
                        }).show();
            }
        });

        goal.setText(GOAL + getGoal() + getResources().getString(R.string.mll));
        today.setText(TODAY + getToday() + getResources().getString(R.string.mll));

        /*
         Floating menu button init
          */
        materialDesignFAM = view.findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = view.findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = view.findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = view.findViewById(R.id.material_design_floating_action_menu_item3);
        floatingActionButton4 = view.findViewById(R.id.material_design_floating_action_menu_item4);
        floatingActionButton5 = view.findViewById(R.id.material_design_floating_action_menu_item5);

        circle = view.findViewById(R.id.circle);
        animateString(getGoal(),getToday());

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


        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new MaterialDialog.Builder(getActivity())
                        .title(R.string.inputCustomAmount)
                        .content(R.string.input_content)
                        .inputType(InputType.TYPE_CLASS_NUMBER )
                        .inputRangeRes(3,5,R.color.black)
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

                                Toasty.success(getActivity(), getResources().getString(R.string.added)+" " + Integer.parseInt(input.toString())+getResources().getString(R.string.mll), Toast.LENGTH_SHORT,true).show();
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
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.inputCustomAmountDelete)
                        .content(R.string.input_content)
                        .inputType(InputType.TYPE_CLASS_NUMBER )
                        .inputRangeRes(3,5,R.color.black)
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
                                    Toasty.success(getActivity(), getResources().getString(R.string.discard)+ " " + Integer.parseInt(input.toString()) + getResources().getString(R.string.mll), Toast.LENGTH_SHORT, true).show();
                                }
                                else {
                                    Toasty.error(getActivity(), getResources().getString(R.string.bigwater), Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        }).show();
            }
        });
        Calendar calendar = Calendar.getInstance();

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        dayOfYear = sharedPreferences.getInt(DAY_OF_YEAR, 0);
        if(dayOfYear < calendar.get(Calendar.DAY_OF_YEAR)){

            dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            setTodayPref(0);
            setTodayTextFromInt(0);
            animateString(getGoal(),getToday());

        }
        return view;
    }

    public String getGoal() {
        SharedPreferences uri = getActivity().getSharedPreferences(PATH_GOAL_LOCATION, Context.MODE_PRIVATE);
        return uri.getString(PATH_GOAL_KEY, "2000");
    }

    public void setGoal(String goal) {
        SharedPreferences uri = getActivity().getSharedPreferences(PATH_GOAL_LOCATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = uri.edit();
        editor.putString(PATH_GOAL_KEY, goal);
        editor.apply();
    }

    public String getToday() {
        SharedPreferences uri = getActivity().getSharedPreferences(PATH_TODAY_LOCATION, Context.MODE_PRIVATE);
        return uri.getString(PATH_TODAY_KEY, "0");
    }

    public void setTodayPref(int value) {
        String today = String.valueOf(value);
        SharedPreferences uri = getActivity().getSharedPreferences(PATH_TODAY_LOCATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = uri.edit();
        editor.putString(PATH_TODAY_KEY, today);
        editor.apply();
    }

    public String getProgress() {
        SharedPreferences uri = getActivity().getSharedPreferences(PATH_PROGRESS_LOCATION, Context.MODE_PRIVATE);
        return uri.getString(PATH_PROGRESS_KEY, "100");
    }

    public void setProgress(String progress) {
        SharedPreferences uri = getActivity().getSharedPreferences(PATH_PROGRESS_LOCATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = uri.edit();
        editor.putString(PATH_PROGRESS_KEY, progress);
        editor.apply();
    }

    public void setTodayTextFromInt(int val) {
        String i = String.valueOf(val);
        today.setText(TODAY + i + getResources().getString(R.string.mll));
    }

    public void animate(int goal, int today) {

        int percent = today * 100 / goal;

        if(percent > 100) {
            circle.setProgress(0);
        }
        else {
            circle.setProgress(100-percent);
        }
    }

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
    public void onResume() {
        super.onResume();
        goal.setText(GOAL + getGoal() + getResources().getString(R.string.mll));
        animateString(getGoal(),getToday());
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(DAY_OF_YEAR, dayOfYear);

        editor.commit();

    }
}