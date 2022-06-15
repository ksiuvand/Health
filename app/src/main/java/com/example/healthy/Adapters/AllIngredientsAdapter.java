package com.example.healthy.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthy.Models.ExtendedIngredient;
import com.example.healthy.R;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class AllIngredientsAdapter extends RecyclerView.Adapter<AllIngredientsViewHolder>{

    Context context;
    List<ExtendedIngredient> list;

    public AllIngredientsAdapter(Context context, List<ExtendedIngredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AllIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllIngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.ingredients_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllIngredientsViewHolder holder, int position) {
        String country = Locale.getDefault().getCountry();
        if (country.equals("RU")){
            TranslateAPI translateAPI = new TranslateAPI(Language.ENGLISH, Language.RUSSIAN , list.get(position).name);
            translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    holder.ingredientNameTxt.setText(translatedText);
                    holder.ingredientNameTxt.setSelected(true);
                }
                @Override
                public void onFailure(String ErrorText) {
                    Log.d("idcn", "onFailure: "+ErrorText);
                }
            });
            TranslateAPI translateAPI2 = new TranslateAPI(Language.ENGLISH, Language.RUSSIAN , list.get(position).original);
            translateAPI2.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    holder.ingredientQuantityTxt.setText(translatedText);
                    holder.ingredientQuantityTxt.setSelected(true);
                }
                @Override
                public void onFailure(String ErrorText) {
                    Log.d("idcn", "onFailure: "+ErrorText);
                }
            });
        }else{
            holder.ingredientNameTxt.setText(list.get(position).name);
            holder.ingredientNameTxt.setSelected(true);
            holder.ingredientQuantityTxt.setText(list.get(position).original);
            holder.ingredientQuantityTxt.setSelected(true);
        }
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/"+list.get(position).image).into(holder.ingredientsImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class AllIngredientsViewHolder extends RecyclerView.ViewHolder {
    TextView ingredientQuantityTxt,ingredientNameTxt;
    ImageView ingredientsImage;

    public AllIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);

        ingredientQuantityTxt = itemView.findViewById(R.id.ingredientQuantityTxt);
        ingredientNameTxt = itemView.findViewById(R.id.ingredientNameTxt);
        ingredientsImage = itemView.findViewById(R.id.ingredientsImage);
    }
}
