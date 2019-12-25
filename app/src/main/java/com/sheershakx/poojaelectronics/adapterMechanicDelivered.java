package com.sheershakx.poojaelectronics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapterMechanicDelivered extends RecyclerView.Adapter<adapterMechanicDelivered.ViewHolder> {

    private ArrayList<String> uid;
    private ArrayList<String> itemtype;
    private ArrayList<String> date;
    private ArrayList<String> status;



    public adapterMechanicDelivered(ArrayList<String> uid, ArrayList<String> itemtype, ArrayList<String> date,ArrayList<String> status) {
        this.uid = uid;
        this.itemtype = itemtype;
        this.date = date;
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.mechanic_pending_layout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();

        final String Uid = uid.get(position);
        final String Delivereddate = date.get(position);
        final String Status = status.get(position);

        holder.uid.setText(Uid);
        holder.date.setText(Delivereddate);
        if (Status!=null && Status.equals("2")){
            holder.status.setTextColor(Color.parseColor("#ff0000"));
            holder.status.setText("Returned");
        }
        if (Status!=null && Status.equals("3")){
            holder.status.setTextColor(Color.parseColor("#00ff00"));
            holder.status.setText("Delivered");
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, mechanic_list_details.class);
                intent.putExtra("uid", Uid);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return uid.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView uid;
        TextView date;
        TextView itemtype;
        TextView status;


        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relative_layout_mechanic);
            status = itemView.findViewById(R.id.status_mechanic);
            uid = itemView.findViewById(R.id.uid_mechanicstatus);
            date = itemView.findViewById(R.id.date_mechanicstatus);


        }
    }
}