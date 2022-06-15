package com.example.healthy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthy.Adapters.RandomDishAdaptor;
import com.example.healthy.Listeners.RandomRecipeListener;
import com.example.healthy.Listeners.RecipeClickListener;
import com.example.healthy.Models.RandomRecipeApiResponse;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesFragment extends Fragment {

    ProgressDialog dialog;
    RequestManager manager;
    RandomDishAdaptor randomDishAdaptor;
    RecyclerView recyclerView;
    Spinner spinner;
    List<String> tags = new ArrayList<>();
    SearchView searchView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipesFragment newInstance(String param1, String param2) {
        RecipesFragment fragment = new RecipesFragment();
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
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        recyclerView = view.findViewById(R.id.recycler_random);
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle(getResources().getString(R.string.loading));

        searchView = view.findViewById(R.id.searchView_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                String country = Locale.getDefault().getCountry();
                if (country.equals("RU")){
                    TranslateAPI translateAPI = new TranslateAPI(Language.RUSSIAN, Language.ENGLISH , query);
                    translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                        @Override
                        public void onSuccess(String translatedText) {
                            Log.d("idcn", translatedText);
                            tags.add(translatedText);
                            manager.getRandomRecipes(randomRecipeListener, tags);
                            dialog.show();
                        }
                        @Override
                        public void onFailure(String ErrorText) {
                            Log.d("idcn", "onFailure: "+ErrorText);
                        }
                    });
                }else{
                    tags.add(query);
                    manager.getRandomRecipes(randomRecipeListener, tags);
                    dialog.show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        spinner = view.findViewById(R.id.spinner_tags);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.tags,
                R.layout.spinner_text
        );
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);

        manager = new RequestManager(getActivity());

        return view;
    }

    private final RandomRecipeListener randomRecipeListener = new RandomRecipeListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            dialog.dismiss();
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            randomDishAdaptor = new RandomDishAdaptor(getActivity(), response.recipes, recipeClickListener);
            recyclerView.setAdapter(randomDishAdaptor);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        }
    };

    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String country = Locale.getDefault().getCountry();
            if (country.equals("RU")){
                if (parent.getSelectedItem().toString().equals("основное блюдо")){
                    tags.clear();
                    tags.add("main course");
                }else if (parent.getSelectedItem().toString().equals("гарнир")){
                    tags.clear();
                    tags.add("side dish");
                }else if (parent.getSelectedItem().toString().equals("десерт")){
                    tags.clear();
                    tags.add("dessert");
                }else if (parent.getSelectedItem().toString().equals("закуска")){
                    tags.clear();
                    tags.add("appetizer");
                }else if (parent.getSelectedItem().toString().equals("салат")){
                    tags.clear();
                    tags.add("salad");
                }else if (parent.getSelectedItem().toString().equals("хлеб")){
                    tags.clear();
                    tags.add("bread");
                }else if (parent.getSelectedItem().toString().equals("завтрак")){
                    tags.clear();
                    tags.add("breakfast");
                }else if (parent.getSelectedItem().toString().equals("суп")){
                    tags.clear();
                    tags.add("soup");
                }else if (parent.getSelectedItem().toString().equals("напиток")){
                    tags.clear();
                    tags.add("beverage");
                }else if (parent.getSelectedItem().toString().equals("соус")){
                    tags.clear();
                    tags.add("sauce");
                }else if (parent.getSelectedItem().toString().equals("маринад")){
                    tags.clear();
                    tags.add("marinade");
                }else if (parent.getSelectedItem().toString().equals("фингерфуд")){
                    tags.clear();
                    tags.add("fingerfood");
                }else if (parent.getSelectedItem().toString().equals("перекус")){
                    tags.clear();
                    tags.add("snack");
                }else if (parent.getSelectedItem().toString().equals("напиток")){
                    tags.clear();
                    tags.add("drink");
                }
            }else{
                tags.clear();
                tags.add(parent.getSelectedItem().toString());
            }
            manager.getRandomRecipes(randomRecipeListener, tags);
            dialog.show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

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