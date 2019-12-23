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

public class adapterAdminReport extends RecyclerView.Adapter<adapterAdminReport.ViewHolder> {

    private ArrayList<String> clientid;
    private ArrayList<String> uid;
    private ArrayList<String> itemtype;
    private ArrayList<String> date;
    private ArrayList<String> name;


    public adapterAdminReport(ArrayList<String> clientiid, ArrayList<String> uid, ArrayList<String> itemtype, ArrayList<String> date, ArrayList<String> name) {
        this.clientid = clientiid;
        this.uid = uid;
        this.itemtype = itemtype;
        this.date = date;
        this.name = name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.admin_pending_layout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();

        final String Uid = uid.get(position);
        final String Itemtype = itemtype.get(position);
        final String Clientid = clientid.get(position);
        final String Date = date.get(position);
        final String Name = name.get(position);


        holder.name.setText(Name);
        holder.itemtype.setText(Itemtype);
        holder.date.setText(Date);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, admin_report_detail.class);
                intent.putExtra("uid", Uid);
                intent.putExtra("clientid", Clientid);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return uid.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView itemtype;
        TextView date;


        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relative_layout_admin_pending);
            name = itemView.findViewById(R.id.name_admin_pending);
            itemtype = itemView.findViewById(R.id.itemtype_admin_pending);
            date = itemView.findViewById(R.id.date_admin_pending);


        }
    }
}