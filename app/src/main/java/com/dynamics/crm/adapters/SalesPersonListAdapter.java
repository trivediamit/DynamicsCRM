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
import com.dynamics.crm.model.SalesPerson;

import java.util.ArrayList;

public class SalesPersonListAdapter extends RecyclerView.Adapter<SalesPersonListAdapter.LeadViewHolder> {

    private ArrayList<SalesPerson> mDataSet;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    public SalesPersonListAdapter(Context context, ArrayList<SalesPerson> myDataSet) {
        this.mDataSet = myDataSet;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public SalesPersonListAdapter.LeadViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.lead_item, viewGroup, false);
        return new LeadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SalesPersonListAdapter.LeadViewHolder viewHolder, int position) {
        SalesPerson salesPerson = mDataSet.get(position);
        viewHolder.mName.setText(salesPerson.getName());
        viewHolder.mName.setTypeface(Typeface.DEFAULT_BOLD);
        viewHolder.mEmailId.setText(salesPerson.getEmailId());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class LeadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mName;
        TextView mEmailId;

        public LeadViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.lead_name);
            mEmailId = (TextView) itemView.findViewById(R.id.lead_topic);
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
