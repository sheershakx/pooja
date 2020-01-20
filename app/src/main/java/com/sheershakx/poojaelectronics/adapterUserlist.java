package com.sheershakx.poojaelectronics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapterUserlist extends RecyclerView.Adapter<adapterUserlist.ViewHolder> {

    private ArrayList<String> name;
    private ArrayList<String> mobile;
    private ArrayList<String> type;
    private ArrayList<String> activestatus;
    private ArrayList<String> id;


    public adapterUserlist(ArrayList<String> name, ArrayList<String> mobile, ArrayList<String> type,ArrayList<String> activestatus,ArrayList<String> id) {
        this.name = name;
        this.mobile = mobile;
        this.type = type;
        this.activestatus = activestatus;
        this.id = id;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.userlist_layout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();

        final String Name = name.get(position);
        final String Id = id.get(position);
        final String Mobile = mobile.get(position);
        final String Type = type.get(position);
        final String Activestatus = activestatus.get(position);


        holder.name.setText(Name);
        holder.mobile.setText(Mobile);

        if (Type!=null && Type.equals("1")){
            holder.type.setText("Mechanic");
        }
        else  if (Type!=null && Type.equals("2")){
            holder.type.setText("Client");
        }

        if (Activestatus!=null && Activestatus.equals("0")){        //if enabled
            holder.enable.setVisibility(View.GONE);
            holder.disable.setVisibility(View.VISIBLE);
        }
        else  if (Activestatus!=null && Activestatus.equals("1")){      //if disabled
            holder.disable.setVisibility(View.GONE);
            holder.enable.setVisibility(View.VISIBLE);
        }

        holder.enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 new change_userstatus().execute(Id,"0");
                Toast.makeText(context, "User Enabled", Toast.LENGTH_SHORT).show();
                ((userlist)context).finish();
            }
        });

        holder.disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new change_userstatus().execute(Id,"1");
                Toast.makeText(context, "User Disabled", Toast.LENGTH_SHORT).show();
                ((userlist)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mobile.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView mobile;
        TextView type;
        TextView status;
        Button enable;
        Button disable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_user);
            mobile = itemView.findViewById(R.id.mobile_user);
            type = itemView.findViewById(R.id.type_user);
            enable = itemView.findViewById(R.id.enable_user);
            disable = itemView.findViewById(R.id.disable_user);


        }
    }
}