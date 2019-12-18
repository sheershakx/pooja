package com.sheershakx.poojaelectronics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapterClientStatus extends RecyclerView.Adapter<adapterClientStatus.ViewHolder> {

    private ArrayList<String> uid;
    private ArrayList<String> status;


    public adapterClientStatus(ArrayList<String> uid, ArrayList<String> status) {
        this.uid = uid;
        this.status = status;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.client_status_layout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();

        final String Uid = uid.get(position);
        final String Status = status.get(position);


        holder.uid.setText(Uid);
        if (Status.equals("0")) {
            holder.status.setText("Pending");
        }
        if (Status.equals("1")) {
            holder.status.setText("Approved");
        }
        if (Status.equals("2")) {
            holder.status.setText("Delivered");
        }
        if (Status.equals("3")) {
            holder.status.setText("Received");
        }


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, client_status_detail.class);
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
        TextView status;


        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relative_layout_cstatus);
            status = itemView.findViewById(R.id.client_status);
            uid = itemView.findViewById(R.id.uid_clientstatus);


        }
    }
}