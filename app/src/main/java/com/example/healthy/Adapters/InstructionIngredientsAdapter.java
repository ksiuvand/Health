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

import com.example.healthy.Models.Ingredient;
import com.example.healthy.R;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class InstructionIngredientsAdapter extends RecyclerView.Adapter<InstructionIngredientsViewHolder>{

    Context context;
    List<Ingredient> list;

    public InstructionIngredientsAdapter(Context context, List<Ingredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionIngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.instructions_step_one_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull InstructionIngredientsViewHolder holder, int position) {
        String country = Locale.getDefault().getCountry();
        if (country.equals("RU")){
            TranslateAPI translateAPI = new TranslateAPI(Language.ENGLISH, Language.RUSSIAN , list.get(position).name);
            translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    holder.instructionStepItemTxt.setText(translatedText);
                    holder.instructionStepItemTxt.setSelected(true);
                }
                @Override
                public void onFailure(String ErrorText) {
                    Log.d("idcn", "onFailure: "+ErrorText);
                }
            });
        }else{
            holder.instructionStepItemTxt.setText(list.get(position).name);
            holder.instructionStepItemTxt.setSelected(true);
        }
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/"+list.get(position).image).into(holder.instructionStepItemsImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


class InstructionIngredientsViewHolder extends RecyclerView.ViewHolder {

    TextView instructionStepItemTxt;
    ImageView instructionStepItemsImage;
    public InstructionIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        instructionStepItemTxt = itemView.findViewById(R.id.instructionStepItemTxt);
        instructionStepItemsImage = itemView.findViewById(R.id.instructionStepItemsImage);
    }
}
