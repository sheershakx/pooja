package com.sheershakx.poojaelectronics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapterPostTable extends RecyclerView.Adapter<adapterPostTable.ViewHolder> {

    private ArrayList<String> itemtype;
    private ArrayList<String> serialno;
    private ArrayList<String> size;
    private ArrayList<String> model;
    private ArrayList<String> prob;
    private ArrayList<String> spec;


    public adapterPostTable(ArrayList<String> itemtype, ArrayList<String> serialno, ArrayList<String> size, ArrayList<String> model, ArrayList<String> prob, ArrayList<String> spec) {
        this.itemtype = itemtype;
        this.serialno = serialno;
        this.size = size;
        this.model = model;
        this.prob = prob;
        this.spec = spec;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tbl_postlayout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();


        final String Itemtype = itemtype.get(position);
        final String Serialno = serialno.get(position);
        final String Size = size.get(position);
        final String Model = model.get(position);
        final String Problem = prob.get(position);
        final String Spec = spec.get(position);


        holder.itemgroup.setText(Itemtype);
        holder.specs.setText(Spec);
        holder.problem.setText(Problem);

    }

    @Override
    public int getItemCount() {
        return itemtype.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView itemgroup;
        TextView specs;
        TextView problem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemgroup = itemView.findViewById(R.id.grpname_post);
            specs = itemView.findViewById(R.id.specs_post);
            problem = itemView.findViewById(R.id.problem_post);


        }
    }
}