package com.dynamics.crm.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dynamics.crm.R;
import com.dynamics.crm.model.Lead;

import java.util.ArrayList;

public class LeadsListAdapter extends RecyclerView.Adapter<LeadsListAdapter.LeadViewHolder> {

    private String[] leadTitleStrings = {"Topic", "First Name", "Last Name", "Company Name", "Email", "Owner", "Status"};

    private ArrayList<Lead> mDataSet;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    public LeadsListAdapter(Context context, ArrayList<Lead> myDataSet) {
        this.mDataSet = myDataSet;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public LeadsListAdapter.LeadViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.lead_item, viewGroup, false);
        return new LeadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeadsListAdapter.LeadViewHolder viewHolder, int position) {
        Lead lead = mDataSet.get(position);
        viewHolder.mName.setText(lead.getFirstName() + " " + lead.getLastName());
        viewHolder.mName.setTypeface(Typeface.DEFAULT_BOLD);
        viewHolder.mTopic.setText(lead.getTopic());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class LeadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mName;
        TextView mTopic;

        public LeadViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.lead_name);
            mTopic = (TextView) itemView.findViewById(R.id.lead_topic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.itemClick(view, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void itemClick(View view, int position);
    }
}
