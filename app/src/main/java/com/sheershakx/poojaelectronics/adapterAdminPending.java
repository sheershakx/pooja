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

public class adapterAdminPending extends RecyclerView.Adapter<adapterAdminPending.ViewHolder> {

    private ArrayList<String> clientid;
    private ArrayList<String> uid;
    private ArrayList<String> itemtype;
    private ArrayList<String> date;
    private ArrayList<String> name;
    private ArrayList<String> status;
    private ArrayList<String> astatus;
    private ArrayList<String> mstatus;
    private ArrayList<String> groupid;


    public adapterAdminPending(ArrayList<String> clientiid, ArrayList<String> uid, ArrayList<String> itemtype, ArrayList<String> date, ArrayList<String> name, ArrayList<String> status, ArrayList<String> astatus, ArrayList<String> mstatus, ArrayList<String> groupid) {
        this.clientid = clientiid;
        this.uid = uid;
        this.itemtype = itemtype;
        this.date = date;
        this.name = name;
        this.status = status;
        this.astatus = astatus;
        this.mstatus = mstatus;
        this.groupid = groupid;
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
        final String Status = status.get(position);
        final String Astatus = astatus.get(position);
        final String Mstatus = mstatus.get(position);
        final String Groupid = groupid.get(position);

        if (Groupid != null && Groupid.equals("0")) {
            holder.name.setText(Name);
            holder.itemtype.setText(Itemtype);
            holder.date.setText(Date);
        }

        if (Groupid != null && !Groupid.equals("0")) {
            holder.name.setText(Name);
            holder.itemtype.setText("Multiple Items");
            holder.date.setText(Date);

        }
        if (Status != null && Status.equals("0")) {
            holder.status.setTextColor(Color.parseColor("#ff0000"));
            holder.status.setText("Pending");
        } else if (Status != null && Status.equals("1") && Astatus != null && Astatus.equals("9")) {
            holder.status.setTextColor(Color.parseColor("#00ff00"));
            holder.status.setText("Accepted");
        } else if (Status != null && Status.equals("2")) {
            holder.status.setTextColor(Color.parseColor("#ff0000"));
            holder.status.setText("Delivered");
        } else if (Status != null && Status.equals("3")) {
            holder.status.setTextColor(Color.parseColor("#00ff00"));
            holder.status.setText("Received");
        } else if (Status != null && Status.equals("4")) {
            holder.status.setTextColor(Color.parseColor("#ff0000"));
            holder.status.setText("Rejected");
        }

        if (Status != null && Astatus != null && Status.equals("1") && Astatus.equals("0") && Mstatus.equals("0")) {
            holder.status.setTextColor(Color.parseColor("#ff0000"));
            holder.status.setText("Forwarded");
        }
        if (Status != null && Astatus != null && Status.equals("1") && Astatus.equals("2") && Mstatus.equals("4")) {
            holder.status.setTextColor(Color.parseColor("#ff0000"));
            holder.status.setText("Mechanic\nRejected");
        }

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Groupid != null && Groupid.equals("0")) {
                        Intent intent = new Intent(context, admin_pending_details.class);
                        intent.putExtra("uid", Uid);
                        intent.putExtra("clientid", Clientid);
                        intent.putExtra("status", Status);
                        intent.putExtra("astatus", Astatus);
                        intent.putExtra("mstatus", Mstatus);
                        context.startActivity(intent);
                        ((admin_pending_list) context).finish();
                    } else if (Groupid != null && !Groupid.equals("0")) {
                        Intent intent = new Intent(context, multiple_pending_view.class);
                        intent.putExtra("groupid", Groupid);
                        context.startActivity(intent);
                        ((admin_pending_list) context).finish();

                    }


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
        TextView status;


        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relative_layout_admin_pending);
            name = itemView.findViewById(R.id.name_admin_pending);
            itemtype = itemView.findViewById(R.id.itemtype_admin_pending);
            date = itemView.findViewById(R.id.date_admin_pending);
            status = itemView.findViewById(R.id.status_admin_pending);


        }
    }
}