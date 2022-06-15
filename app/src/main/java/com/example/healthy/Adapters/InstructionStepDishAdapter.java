package com.example.healthy.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthy.Models.Step;
import com.example.healthy.R;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;

import java.util.List;
import java.util.Locale;

public class InstructionStepDishAdapter extends RecyclerView.Adapter<InstructionStepDishViewHolder>{

    Context context;
    List<Step> list;

    public InstructionStepDishAdapter(Context context, List<Step> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionStepDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionStepDishViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions_steps,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionStepDishViewHolder holder, int position) {
        holder.instructionStepNumberTxt.setText(String.valueOf(list.get(position).number));
        String country = Locale.getDefault().getCountry();
        if (country.equals("RU")){
            TranslateAPI translateAPI = new TranslateAPI(Language.ENGLISH, Language.RUSSIAN , list.get(position).step);
            translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    Log.d("idcn", "onSuccess: "+translatedText);
                    holder.instructionStepTitleTxt.setText(translatedText);
                }
                @Override
                public void onFailure(String ErrorText) {
                    Log.d("idcn", "onFailure: "+ErrorText);
                }
            });
        }else{
            holder.instructionStepTitleTxt.setText(list.get(position).step);
        }

        holder.instructionIngredientsRecycler.setHasFixedSize(true);
        holder.instructionIngredientsRecycler.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
        InstructionIngredientsAdapter instructionIngredientsAdapter = new InstructionIngredientsAdapter(context, list.get(position).ingredients);
        holder.instructionIngredientsRecycler.setAdapter(instructionIngredientsAdapter);

        holder.instructionEquipmentsRecycler.setHasFixedSize(true);
        holder.instructionEquipmentsRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        InstructionEquipmentAdapter instructionEquipmentAdapter = new InstructionEquipmentAdapter(context, list.get(position).equipment);
        holder.instructionEquipmentsRecycler.setAdapter(instructionEquipmentAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionStepDishViewHolder extends RecyclerView.ViewHolder {
    TextView instructionStepNumberTxt,instructionStepTitleTxt;
    RecyclerView instructionEquipmentsRecycler,instructionIngredientsRecycler;

    public InstructionStepDishViewHolder(@NonNull View itemView) {
        super(itemView);
        instructionStepNumberTxt = itemView.findViewById(R.id.instructionStepNumberTxt);
        instructionStepTitleTxt = itemView.findViewById(R.id.instructionStepTitleTxt);
        instructionEquipmentsRecycler = itemView.findViewById(R.id.instructionEquipmentsRecycler);
        instructionIngredientsRecycler = itemView.findViewById(R.id.instructionIngredientsRecycler);
    }
}
