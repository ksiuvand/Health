package com.example.healthy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthy.Adapters.AllIngredientsAdapter;
import com.example.healthy.Adapters.InstructionAdapter;
import com.example.healthy.Listeners.InstructionListener;
import com.example.healthy.Listeners.RecipeDetailsListener;
import com.example.healthy.Models.InstructionsResponse;
import com.example.healthy.Models.RecipeDetailsResponse;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeDetailsActivity extends AppCompatActivity {
    int id;
    boolean flag = true;
    TextView mealNameTxt, informationRes;
    public final static String PATH_MARKS_LOCATION = "marks";
    ImageView mealImage;
    ImageButton toMarks;
    RecyclerView mealIngredientsRecycler, mealInstructionsRecycler;
    RequestManager manager;
    ProgressDialog dialog;
    AllIngredientsAdapter allIngredientsAdapter;
    InstructionAdapter instructionAdapter;
    ImageButton backBtn;
    ArrayList<String> marksM = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_details);

        findViews();
        id = Integer.parseInt(getIntent().getStringExtra("id"));
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener, id, true);
        manager.getInstructions(instructionListener, id);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.show();
        toMarks = findViewById(R.id.toMarks);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                SharedPreferences uri = RecipeDetailsActivity.this.getSharedPreferences(PATH_MARKS_LOCATION, Context.MODE_PRIVATE);
                Log.d("kojihu", String.valueOf(uri.getInt("marksSize", 0)));
                Log.d("ljik", marksM.toString());
            }
        });
        SharedPreferences uri = RecipeDetailsActivity.this.getSharedPreferences(PATH_MARKS_LOCATION, Context.MODE_PRIVATE);
        int size = uri.getInt("marksSize", 0);
        for(int i=0; i < size; i++)
            marksM.add(uri.getString("marks" + i, ""));
        Log.d("koijuhygtf", marksM.toString() + "      " + marksM.size());
        Log.d("junhyb", String.valueOf(size));

        if(marksM.contains(String.valueOf(id))){
            toMarks.setImageResource(R.drawable.bookmarknew3);
            flag = false;
        }

        toMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences uri = RecipeDetailsActivity.this.getSharedPreferences(PATH_MARKS_LOCATION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = uri.edit();
                if (flag){
                    toMarks.setImageResource(R.drawable.bookmarknew3);
                    flag = false;
                    marksM.add(String.valueOf(id));
                    editor.putInt("marksSize", uri.getInt("marksSize", 0)+1);
                    for(int i=0; i < marksM.size(); i++)
                        editor.putString("marks"+i, marksM.get(i));
                    editor.apply();
                    Log.d("hjghf", marksM.toString());

                }else{
                    for(int i=0; i < marksM.size(); i++)
                        editor.remove("marks"+i).apply();
                    marksM.remove(String.valueOf(id));
                    editor.putInt("marksSize", uri.getInt("marksSize", 0)-1);
                    for(int i=0; i < marksM.size(); i++)
                        editor.putString("marks"+i, marksM.get(i));
                    editor.apply();
                    Log.d("ijbh", marksM.toString());
                    toMarks.setImageResource(R.drawable.bookmarknew2);
                    flag=true;}
            }
        });
    }

    private void findViews() {
        mealNameTxt = findViewById(R.id.mealNameTxt);
        mealImage = findViewById(R.id.mealImage);
        mealIngredientsRecycler = findViewById(R.id.mealIngredientsRecycler);
        mealInstructionsRecycler = findViewById(R.id.mealInstructionsRecycler);
        informationRes = findViewById(R.id.informationRes);
        backBtn = findViewById(R.id.backbtn);
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            String country = Locale.getDefault().getCountry();
            if (country.equals("RU")){
                TranslateAPI translateAPI = new TranslateAPI(Language.ENGLISH, Language.RUSSIAN , response.title);
                translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                    @Override
                    public void onSuccess(String translatedText) {
                        mealNameTxt.setText(translatedText);
                    }
                    @Override
                    public void onFailure(String ErrorText) {
                        Log.d("idcn", "onFailure: "+ErrorText);
                    }
                });
                String ch1 = response.summary.replaceAll("</b>","");
                String ch2 = ch1.replace("<b>","");
                TranslateAPI translateAPI2 = new TranslateAPI(Language.ENGLISH, Language.RUSSIAN , ch2.split("<a href")[0]);
                translateAPI2.setTranslateListener(new TranslateAPI.TranslateListener() {
                    @Override
                    public void onSuccess(String translatedText) {
                        informationRes.setText(translatedText);
                    }
                    @Override
                    public void onFailure(String ErrorText) {
                        Log.d("idcn", "onFailure: "+ErrorText);
                    }
                });
            }else{
                mealNameTxt.setText(response.title);
                String ch1 = response.summary.replaceAll("</b>","");
                String ch2 = ch1.replace("<b>","");
                informationRes.setText(ch2.split("<a href")[0]);
            }

            Picasso.get().load(response.image).into(mealImage);

            mealIngredientsRecycler.setHasFixedSize(true);
            mealIngredientsRecycler.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
            allIngredientsAdapter = new AllIngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
            mealIngredientsRecycler.setAdapter(allIngredientsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private final InstructionListener instructionListener = new InstructionListener() {
        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            mealInstructionsRecycler.setHasFixedSize(true);
            mealInstructionsRecycler.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this,LinearLayoutManager.VERTICAL,false));
            instructionAdapter = new InstructionAdapter(RecipeDetailsActivity.this, response);
            mealInstructionsRecycler.setAdapter(instructionAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message,Toast.LENGTH_SHORT).show();
        }
    };
}