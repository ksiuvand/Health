package com.example.healthy.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthy.Listeners.RecipeClickListener;
import com.example.healthy.Models.Recipe;
import com.example.healthy.R;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class RandomDishAdaptor extends RecyclerView.Adapter<RandomRecipeViewHolder>{

    Context context;
    List<Recipe> list;
    RecipeClickListener listener;

    public RandomDishAdaptor(Context context, List<Recipe> list, RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }


    @NonNull
    @Override
    public RandomRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_card,parent,false));
    }

    public String getLikechislo(int n) {
        int result = n % 100;
        if (result >=10 && result <= 20) {
            // если окончание 11 - 20
            return context.getResources().getString(R.string.like);
        }
        // смотрим одну последнюю цифру
        result = n % 10;
        if (result == 0 || result > 4) {
            return context.getResources().getString(R.string.like);
        }
        if (result > 1) {
            return context.getResources().getString(R.string.like2);
        } if (result == 1) {
            return context.getResources().getString(R.string.like3);
        }
        return null;
    }
    public String getServingchislo(int n) {
        int result = n % 100;
        if (result >=10 && result <= 20) {
            // если окончание 11 - 20
            return context.getResources().getString(R.string.servings);
        }
        // смотрим одну последнюю цифру
        result = n % 10;
        if (result == 0 || result > 4) {
            return context.getResources().getString(R.string.servings);
        }
        if (result > 1) {
            return context.getResources().getString(R.string.servings2);
        } if (result == 1) {
            return context.getResources().getString(R.string.servings3);
        }
        return null;
    }
    public String getMinutechislo(int n) {
        int result = n % 100;
        if (result >=10 && result <= 20) {
            return context.getResources().getString(R.string.minute);
        }
        result = n % 10;
        if (result == 0 || result > 4) {
            return context.getResources().getString(R.string.minute);
        }
        if (result > 1) {
            return context.getResources().getString(R.string.minute2);
        } if (result == 1) {
            return context.getResources().getString(R.string.minute3);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RandomRecipeViewHolder holder, int position) {
        String country = Locale.getDefault().getCountry();
        if (country.equals("RU")){
            Log.d("ennnng", "onSuccess: "+list.get(position).title);
            TranslateAPI translateAPI = new TranslateAPI(Language.ENGLISH, Language.RUSSIAN , list.get(position).title);
            translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    Log.d("idcn", "onSuccess: "+translatedText);
                    holder.titleTxt.setText(translatedText);
                    holder.titleTxt.setSelected(true);
                }
                @Override
                public void onFailure(String ErrorText) {
                    Log.d("idcn", "onFailure: "+ErrorText);
                }
            });
        }else{
            holder.titleTxt.setText(list.get(position).title);
            holder.titleTxt.setSelected(true);
        }
        holder.likesTxt.setText(list.get(position).aggregateLikes+" "+ getLikechislo(list.get(position).aggregateLikes));
        holder.personsTxt.setText(list.get(position).servings+" " + getServingchislo(list.get(position).servings));
        holder.timeTxt.setText(list.get(position).readyInMinutes+" " + getMinutechislo(list.get(position).readyInMinutes));
        Picasso.get().load(list.get(position).image).transform(new RoundedCornersTransform()).into(holder.dishImage);

        holder.dishListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class RandomRecipeViewHolder extends RecyclerView.ViewHolder{
    CardView dishListCard;
    TextView titleTxt,personsTxt,likesTxt,timeTxt;
    ImageView dishImage;

    public RandomRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        dishListCard = itemView.findViewById(R.id.dishListCard);
        titleTxt = itemView.findViewById(R.id.titleTxt);
        personsTxt = itemView.findViewById(R.id.personsTxt);
        likesTxt = itemView.findViewById(R.id.likesTxt);
        timeTxt = itemView.findViewById(R.id.timeTxt);
        dishImage = itemView.findViewById(R.id.dishImage);
    }
}
