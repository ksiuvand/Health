package com.example.healthy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthy.Adapters.DishMarksAdapter;
import com.example.healthy.Adapters.RandomDishAdaptor;
import com.example.healthy.Listeners.RecipeClickListener;
import com.example.healthy.Listeners.RecipeDetailsListener;
import com.example.healthy.Models.RecipeDetailsResponse;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MarksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarksFragment extends Fragment {
    ProgressDialog dialog;
    public final static String PATH_MARKS_LOCATION = "marks";
    RequestManager manager;
    RandomDishAdaptor randomDishAdaptor;
    RecyclerView recyclerView;
    private DishMarksAdapter adapter;
    private List<MarksItem> listItems;
    ArrayList<String> marksM = new ArrayList<String>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MarksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MarksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MarksFragment newInstance(String param1, String param2) {
        MarksFragment fragment = new MarksFragment();
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
        View view = inflater.inflate(R.layout.fragment_marks, container, false);
        SharedPreferences uri = getActivity().getSharedPreferences(PATH_MARKS_LOCATION, Context.MODE_PRIVATE);
        int size = uri.getInt("marksSize", 0);
        listItems = new ArrayList<>();
        manager = new RequestManager(getActivity());
        for(int i=0; i < size; i++)
            marksM.add(uri.getString("marks" + i, ""));
        for(int i=0; i < size; i++)
            manager.getRecipeDetails(recipeDetailsListener, Integer.parseInt(marksM.get(i)), true);
        recyclerView = view.findViewById(R.id.recycler_marks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        String title, servings, minutes, likes, image;
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            Log.d("sdfbghn", response.title);
            String country = Locale.getDefault().getCountry();
            if (country.equals("RU")){
                TranslateAPI translateAPI = new TranslateAPI(Language.ENGLISH, Language.RUSSIAN , response.title);
                translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                    @Override
                    public void onSuccess(String translatedText) {
                        title = translatedText;
                        Log.d("oiuygbn", title);
                    }
                    @Override
                    public void onFailure(String ErrorText) {
                        Log.d("idcn", "onFailure: "+ErrorText);
                    }
                });
            }else{
                title = response.title;
                Log.d("oiuygbn", title);
            }
            title = response.title;
            servings = String.valueOf(response.servings);
            minutes = String.valueOf(response.readyInMinutes);
            likes = String.valueOf(response.aggregateLikes);
            image = String.valueOf(response.image);
            listItems.add(new MarksItem(title, servings, likes, minutes, image));
            adapter = new DishMarksAdapter(listItems, getActivity(), recipeClickListener);
            recyclerView.setAdapter(adapter);

        }

        @Override
        public void didError(String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    };


    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(getActivity(), RecipeDetailsActivity.class)
                    .putExtra("id", id));

        }
    };
}