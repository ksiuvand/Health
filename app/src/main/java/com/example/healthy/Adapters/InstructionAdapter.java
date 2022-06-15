package com.example.healthy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthy.Models.InstructionsResponse;
import com.example.healthy.R;

import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionViewHolder>{

    Context context;
    List<InstructionsResponse> list;

    public InstructionAdapter(Context context, List<InstructionsResponse> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionViewHolder(LayoutInflater.from(context).inflate(R.layout.instruction_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {
        holder.instructionNameTxt.setText(list.get(position).name);
        holder.instructionStepsRecycler.setHasFixedSize(true);
        holder.instructionStepsRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        InstructionStepDishAdapter stepAdapter = new InstructionStepDishAdapter(context, list.get(position).steps);
        holder.instructionStepsRecycler.setAdapter(stepAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionViewHolder extends RecyclerView.ViewHolder {

    TextView instructionNameTxt;
    RecyclerView instructionStepsRecycler;
    public InstructionViewHolder(@NonNull View itemView) {
        super(itemView);
        instructionNameTxt = itemView.findViewById(R.id.instructionNameTxt);
        instructionStepsRecycler = itemView.findViewById(R.id.instructionStepsRecycler);
    }
}
