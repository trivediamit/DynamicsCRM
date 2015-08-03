package com.dynamics.crm.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dynamics.crm.R;
import com.dynamics.crm.model.Lead;

import java.util.ArrayList;

public class LeadDetailsAdapter extends RecyclerView.Adapter<LeadDetailsAdapter.LeadDetailViewHolder> {

    private LayoutInflater layoutInflater;

    private String[] leadTitleStrings = {"Topic", "First Name", "Last Name", "Company Name", "Email", "Business Phone", "Owner", "Status"};
    private ArrayList<String> leadValueStrings;

    public LeadDetailsAdapter(Context context, ArrayList<String> leadValueStrings) {
        this.leadValueStrings = leadValueStrings;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public LeadDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.lead_detail_item, viewGroup, false);
        return new LeadDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeadDetailViewHolder viewHolder, int position) {
        viewHolder.mTitle.setText(leadTitleStrings[position]);
        viewHolder.mValue.setText(leadValueStrings.get(position));
    }

    @Override
    public int getItemCount() {
        return leadTitleStrings.length;
    }

    class LeadDetailViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mValue;

        public LeadDetailViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.item_title);
            mValue = (TextView) itemView.findViewById(R.id.item_value);
        }
    }
}
